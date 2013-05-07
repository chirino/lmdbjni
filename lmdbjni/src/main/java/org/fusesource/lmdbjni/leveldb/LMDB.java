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

package org.fusesource.lmdbjni.leveldb;

import org.fusesource.lmdbjni.*;
import org.iq80.leveldb.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implements the LevelDB API using LMDB.
 */
public class LMDB implements DB {

    final Env env;
    final Database db;
    final AtomicBoolean closed = new AtomicBoolean(false);

    public LMDB(File path, Options options) throws IOException {
        try {
            env = new Env();
            env.setMapSize(1024*1024*1024*1024);
            if (options instanceof LMDBOptions) {
                LMDBOptions o = ((LMDBOptions) options);
                env.setMaxReaders(o.maxReaders());
                env.setMapSize(o.mapSize());
                env.addFlags(o.openFlags);
            }
            env.open(path.getCanonicalPath());
            db = env.openDatabase("x");
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public void close() throws IOException {
        try {
            if (closed.compareAndSet(false, true)) {
                db.close();
                env.close();
            }
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public byte[] get(byte[] key) throws DBException {
        try {
            return db.get(key);
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public byte[] get(byte[] key, ReadOptions readOptions) throws DBException {
        try {
            if (readOptions.snapshot() == null) {
                return db.get(key);
            } else {
                Transaction tx = ((LMDBSnapshot) readOptions.snapshot()).tx;
                return db.get(tx, key);
            }
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public DBIterator iterator() {
        try {
            Transaction tx = env.createTransaction(true);
            return new LMDBIterator(tx, db.openCursor(tx));
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public DBIterator iterator(ReadOptions readOptions) {
        try {
            if (readOptions.snapshot() == null) {
                return iterator();
            } else {
                Transaction tx = ((LMDBSnapshot) readOptions.snapshot()).tx;
                return new LMDBIterator(null, db.openCursor(tx));
            }
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }


    public void put(byte[] bytes, byte[] value) throws DBException {
        try {
            db.put(bytes, value);
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public Snapshot put(byte[] key, byte[] value, WriteOptions writeOptions) throws DBException {
        try {
            put(key, value);
            return processWriteOptions(writeOptions);
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    private Snapshot processWriteOptions(WriteOptions writeOptions) {
        if (writeOptions.sync()) {
            env.sync(true);
        }
        if (writeOptions.snapshot()) {
            return getSnapshot();
        } else {
            return null;
        }
    }

    public void delete(byte[] key) throws DBException {
        try {
            db.delete(key);
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public Snapshot delete(byte[] key, WriteOptions writeOptions) throws DBException {
        try {
            delete(key);
            return processWriteOptions(writeOptions);
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    class LMDBSnapshot implements Snapshot {
        private final Transaction tx;

        public LMDBSnapshot(Transaction tx) {
            this.tx = tx;
        }

        public void close() throws IOException {
            try {
                tx.commit();
            } catch (LMDBException e) {
                throw new DBException(e.getMessage(), e);
            }
        }
    }

    public Snapshot getSnapshot() {
        try {
            return new LMDBSnapshot(env.createTransaction(true));
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }


    static class LMDBWriteBatch implements WriteBatch {

        static abstract class Op {
            abstract public void apply(Database db, Transaction tx);
        }

        static class PutOp extends Op {
            private final byte[] key;
            private final byte[] value;

            public PutOp(byte[] key, byte[] value) {
                this.key = key;
                this.value = value;
            }

            public void apply(Database db, Transaction tx) {
                db.put(tx, key, value);
            }
        }

        static class DeleteOp extends Op {
            private final byte[] key;

            public DeleteOp(byte[] key) {
                this.key = key;
            }

            public void apply(Database db, Transaction tx) {
                db.delete(tx, key);
            }
        }

        private ArrayList<Op> ops = new ArrayList<Op>();

        public void apply(Database db, Transaction tx) {
            for (Op op : ops) {
                op.apply(db, tx);
            }
        }

        public void close() throws IOException {
            ops = null;
        }

        public WriteBatch put(byte[] key, byte[] value) {
            ops.add(new PutOp(key, value));
            return this;
        }

        public WriteBatch delete(byte[] key) {
            ops.add(new DeleteOp(key));
            return this;
        }
    }

    public WriteBatch createWriteBatch() {
        return new LMDBWriteBatch();
    }

    public void write(WriteBatch writeBatch) throws DBException {
        try {
            Transaction tx = env.createTransaction(false);
            try {
                ((LMDBWriteBatch) writeBatch).apply(db, tx);
            } finally {
                tx.commit();
            }
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    public Snapshot write(WriteBatch writeBatch, WriteOptions writeOptions) throws DBException {
        try {
            write(writeBatch);
            return processWriteOptions(writeOptions);
        } catch (LMDBException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    static class LMDBIterator implements DBIterator {

        private final Transaction tx;
        private final Cursor cursor;
        Entry next;
        Entry prev;

        public LMDBIterator(Transaction tx, Cursor cursor) {
            this.tx = tx;
            this.cursor = cursor;
        }

        public void close() throws IOException {
            cursor.close();
            if (tx != null) {
                tx.commit();
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        public boolean hasPrev() {
            return prev != null;
        }

        public Map.Entry<byte[], byte[]> peekNext() {
            return next;
        }

        public Map.Entry<byte[], byte[]> peekPrev() {
            return prev;
        }

        public void seekToFirst() {
            try {
                prev = null;
                next = cursor.get(CursorOp.FIRST);
            } catch (LMDBException e) {
                throw new DBException(e.getMessage(), e);
            }
        }

        public void seekToLast() {
            try {
                next = null;
                prev = cursor.get(CursorOp.LAST);
            } catch (LMDBException e) {
                throw new DBException(e.getMessage(), e);
            }
        }

        public void seek(byte[] bytes) {
            try {
                next = cursor.seek(bytes, CursorOp.SET_RANGE);
                prev = cursor.seek(bytes, CursorOp.PREV);
                cursor.seek(bytes, CursorOp.NEXT);
            } catch (LMDBException e) {
                throw new DBException(e.getMessage(), e);
            }
        }

        public Map.Entry<byte[], byte[]> prev() {
            if (!hasPrev()) {
                throw new NoSuchElementException();
            }
            try {
                Entry rc = prev;
                next = prev;
                prev = cursor.get(CursorOp.PREV);
                return rc;
            } catch (LMDBException e) {
                throw new DBException(e.getMessage(), e);
            }

        }

        public Map.Entry<byte[], byte[]> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                Entry rc = next;
                prev = next;
                next = cursor.get(CursorOp.NEXT);
                return rc;
            } catch (LMDBException e) {
                throw new DBException(e.getMessage(), e);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public long[] getApproximateSizes(Range... ranges) {
        throw new UnsupportedOperationException();
    }

    public String getProperty(String s) {
        throw new UnsupportedOperationException();
    }

    public void suspendCompactions() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public void resumeCompactions() {
        throw new UnsupportedOperationException();
    }
}
