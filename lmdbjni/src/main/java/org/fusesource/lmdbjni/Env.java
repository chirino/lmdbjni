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

import java.io.Closeable;

import static org.fusesource.lmdbjni.JNI.*;
import static org.fusesource.lmdbjni.Util.*;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Env extends NativeObject implements Closeable {

    public static String version() {
        return string(JNI.MDB_VERSION_STRING);
    }

    public Env() {
        super(create());
        setMaxDbs(1);
    }

    private static long create() {
        long env_ptr[] = new long[1];
        checkErrorCode(mdb_env_create(env_ptr));
        return env_ptr[0];
    }

    public void open(String path) {
        open(path, 0);
    }

    public void open(String path, int flags) {
        open(path, flags, 0644);
    }

    public void open(String path, int flags, int mode) {
        int rc = mdb_env_open(pointer(), path, flags, mode);
        if( rc!=0 ) {
            close();
        }
        checkErrorCode(rc);
    }

    public void close() {
        if( self!=0 ) {
            mdb_env_close(self);
            self=0;
        }
    }

    public void copy(String path) {
        checkArgNotNull(path, "path");
        checkErrorCode(mdb_env_copy(pointer(), path));
    }

    public void sync(boolean force) {
        checkErrorCode(mdb_env_sync(pointer(), force ? 1 : 0));
    }


    public void setMapSize(long size) {
        checkErrorCode(mdb_env_set_mapsize(pointer(), size));
    }

    public void setMaxDbs(long size) {
        checkErrorCode(mdb_env_set_maxdbs(pointer(), size));
    }

    public long getMaxReaders() {
        long rc[] = new long[1];
        checkErrorCode(mdb_env_get_maxreaders(pointer(), rc));
        return rc[0];
    }
    public void setMaxReaders(long size) {
        checkErrorCode(mdb_env_set_maxreaders(pointer(), size));
    }

    public int getFlags() {
        long[] flags = new long[1];
        checkErrorCode(mdb_env_get_flags(pointer(), flags));
        return (int) flags[0];
    }

    public void addFlags(int flags) {
        checkErrorCode(mdb_env_set_flags(pointer(), flags, 1));
    }

    public void removeFlags(int flags) {
        checkErrorCode(mdb_env_set_flags(pointer(), flags, 0));
    }

    public MDB_envinfo info() {
        MDB_envinfo rc = new MDB_envinfo();
        mdb_env_info(pointer(), rc);
        return rc;
    }

    public MDB_stat stat() {
        MDB_stat rc = new MDB_stat();
        mdb_env_stat(pointer(), rc);
        return rc;
    }

    public Transaction createTransaction() {
        return createTransaction(null, false);
    }
    public Transaction createTransaction(boolean readOnly) {
        return createTransaction(null, readOnly);
    }
    public Transaction createTransaction(Transaction parent) {
        return createTransaction(parent, false);
    }

    public Transaction createTransaction(Transaction parent, boolean readOnly) {
        long txpointer [] = new long[1];
        checkErrorCode(mdb_txn_begin(pointer(), parent==null ? 0 : parent.pointer(), readOnly ? MDB_RDONLY : 0, txpointer));
        return new Transaction(this, txpointer[0]);
    }

    public Database openDatabase(Transaction tx, String name, int flags) {
        checkArgNotNull(tx, "tx");
        checkArgNotNull(name, "name");
        long dbi[] = new long[1];
        checkErrorCode(mdb_dbi_open(tx.pointer(), name, flags, dbi));
        return new Database(this, dbi[0]);
    }

    public Database openDatabase(String name) {
        checkArgNotNull(name, "name");
        return openDatabase(name, Constants.CREATE);
    }
    public Database openDatabase(String name, int flags) {
        checkArgNotNull(name, "name");
        Transaction tx = createTransaction();
        try {
            return openDatabase(tx, name, flags);
        } finally {
            tx.commit();
        }
    }

    public static void pushMemoryPool(int size) {
        NativeBuffer.pushMemoryPool(size);
    }

    public static void popMemoryPool() {
        NativeBuffer.popMemoryPool();
    }
}
