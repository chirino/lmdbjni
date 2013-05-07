/**
 * Copyright (C) 2013, RedHat, Inc.
 *
 *    http://www.redhat.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.lmdbjni;

import org.fusesource.hawtjni.runtime.PointerMath;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A NativeBuffer allocates a native buffer on the heap.  It supports
 * creating sub slices/views of that buffer and manages reference tracking
 * so that the the native buffer is freed once all NativeBuffer views
 * are deleted.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
class NativeBuffer extends NativeObject {

    private static class Allocation extends NativeObject {
        private final AtomicInteger retained = new AtomicInteger(0);

        private Allocation(long size) {
            super(JNI.malloc(size));
        }

        void retain() {
            checkAllocated();
            retained.incrementAndGet();
        }

        void release() {
            checkAllocated();
            int r = retained.decrementAndGet();
            if( r < 0 ) {
                throw new Error("The object has already been deleted.");
            } else if( r==0 ) {
                JNI.free(self);
                self = 0;
            }
        }
    }

    private static class Pool {
        private final NativeBuffer.Pool prev;
        Allocation allocation;
        long pos;
        long remaining;
        int chunk;

        public Pool(int chunk, Pool prev) {
            this.chunk = chunk;
            this.prev = prev;
        }

        NativeBuffer create(long size) {
            if( size >= chunk ) {
                Allocation allocation = new Allocation(size);
                return new NativeBuffer(allocation, allocation.self, size);
            }

            if( remaining < size ) {
                delete();
            }

            if( allocation == null ) {
                allocate();
            }

            NativeBuffer rc = new NativeBuffer(allocation, pos, size);
            pos = PointerMath.add(pos, size);
            remaining -= size;
            return rc;
        }

        private void allocate() {
            allocation = new NativeBuffer.Allocation(chunk);
            allocation.retain();
            remaining = chunk;
            pos = allocation.self;
        }

        public void delete() {
            if( allocation!=null ) {
                allocation.release();
                allocation = null;
            }
        }
    }

    private final Allocation allocation;
    private final long capacity;

    static final private ThreadLocal<Pool> CURRENT_POOL = new ThreadLocal<Pool>();

    static public NativeBuffer create(long capacity) {
        Pool pool = CURRENT_POOL.get();
        if( pool == null ) {
            Allocation allocation = new Allocation(capacity);
            return new NativeBuffer(allocation, allocation.self, capacity);
        } else {
            return pool.create(capacity);
        }
    }


    public static void pushMemoryPool(int size) {
        Pool original = CURRENT_POOL.get();
        Pool next = new Pool(size, original);
        CURRENT_POOL.set(next);
    }

    public static void popMemoryPool() {
        Pool next = CURRENT_POOL.get();
        next.delete();
        if( next.prev == null ) {
            CURRENT_POOL.remove();
        } else {
            CURRENT_POOL.set(next.prev);
        }
    }

    static public NativeBuffer create(byte[] data) {
        if( data == null ) {
            return null;
        } else {
            return create(data, 0 , data.length);
        }
    }
    
    static public NativeBuffer create(String data) {
        return create(cbytes(data));
    }

    static public NativeBuffer create(byte[] data, int offset, int length) {
        NativeBuffer rc = create(length);
        rc.write(0, data, offset, length);
        return rc;
    }

    static public NativeBuffer create(long pointer, int length) {
        return new NativeBuffer(null, pointer, length);
    }

    private NativeBuffer(Allocation allocation, long self, long capacity) {
        super(self);
        this.capacity = capacity;
        this.allocation = allocation;
        if( allocation!=null ) {
            allocation.retain();
        }
    }

    public NativeBuffer slice(long offset, long length) {
        checkAllocated();
        if( length < 0 ) throw new IllegalArgumentException("length cannot be negative");
        if( offset < 0 ) throw new IllegalArgumentException("offset cannot be negative");
        if( offset+length >= capacity) throw new ArrayIndexOutOfBoundsException("offset + length exceed the length of this buffer");
        return new NativeBuffer(allocation, PointerMath.add(self, offset), length);
    }
    
    static byte[] cbytes(String strvalue) {
        byte[] value = strvalue.getBytes();
        // expand by 1 so we get a null at the end.
        byte[] rc = new byte[value.length+1];
        System.arraycopy(value, 0, rc, 0, value.length);
        return rc;
    }

    public NativeBuffer head(long length) {
        return slice(0, length);
    }

    public NativeBuffer tail(long length) {
        if( capacity-length < 0) throw new ArrayIndexOutOfBoundsException("capacity-length cannot be less than zero");
        return slice(capacity-length, length);
    }

    public void delete() {
        allocation.release();
    }

    public long capacity() {
        return capacity;
    }

    public void write(long at, byte []source, int offset, int length) {
        checkAllocated();
        if( length < 0 ) throw new IllegalArgumentException("length cannot be negative");
        if( offset < 0 ) throw new IllegalArgumentException("offset cannot be negative");
        if( at < 0 ) throw new IllegalArgumentException("at cannot be negative");
        if( at+length > capacity ) throw new ArrayIndexOutOfBoundsException("at + length exceeds the capacity of this object");
        if( offset+length > source.length) throw new ArrayIndexOutOfBoundsException("offset + length exceed the length of the source buffer");
        JNI.buffer_copy(source, offset, self, at, length);
    }

    public void read(long at, byte []target, int offset, int length) {
        checkAllocated();
        if( length < 0 ) throw new IllegalArgumentException("length cannot be negative");
        if( offset < 0 ) throw new IllegalArgumentException("offset cannot be negative");
        if( at < 0 ) throw new IllegalArgumentException("at cannot be negative");
        if( at+length > capacity ) throw new ArrayIndexOutOfBoundsException("at + length exceeds the capacity of this object");
        if( offset+length > target.length) throw new ArrayIndexOutOfBoundsException("offset + length exceed the length of the target buffer");
        JNI.buffer_copy(self, at, target, offset, length);
    }

    public byte[] toByteArray() {
        if( capacity > Integer.MAX_VALUE ) {
            throw new OutOfMemoryError("Native buffer larger than the largest allowed Java byte[]");
        }
        byte [] rc = new byte[(int) capacity];
        read(0, rc, 0, rc.length);
        return rc;
    }
}
