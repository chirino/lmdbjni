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

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
class Value extends JNI.MDB_val {

    public Value() {
    }

    public Value(long data, long length) {
        this.mv_data = data;
        this.mv_size = length;
    }

    public Value(NativeBuffer buffer) {
        this(buffer.pointer(), buffer.capacity());
    }

    public static Value create(NativeBuffer buffer) {
        if(buffer == null ) {
            return null;
        } else {
            return new Value(buffer);
        }
    }

    public byte[] toByteArray() {
        if( mv_data == 0  ) {
            return null;
        }
        if( mv_size > Integer.MAX_VALUE ) {
            throw new ArrayIndexOutOfBoundsException("Native slice is larger than the maximum Java array");
        }
        byte []rc = new byte[(int) mv_size];
        JNI.buffer_copy(mv_data, 0, rc, 0, rc.length);
        return rc;
    }

}
