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

import org.fusesource.lmdbjni.Env;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;

import java.io.*;

/**
 * Factory class for the LevelDB API.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class LMDBFactory implements DBFactory {

    public static final LMDBFactory factory = new LMDBFactory();

    private static final String LIBLMDB_VERSION;
    static {
        LIBLMDB_VERSION = Env.version();
    }

    public static final String VERSION;
    static {
        String v="unknown";
        InputStream is = Env.class.getResourceAsStream("version.txt");
        try {
            v = new BufferedReader(new InputStreamReader(is, "UTF-8")).readLine();
        } catch (Throwable e) {
        } finally {
            try {
                is.close();
            } catch (Throwable e) {
            }
        }
        VERSION = v;
    }

    public static byte[] bytes(String value) {
        if( value == null) {
            return null;
        }
        try {
            return value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String asString(byte value[]) {
        if( value == null) {
            return null;
        }
        try {
            return new String(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public DB open(File path, Options options) throws IOException {
        checkArgNotNull(path, "path");
        checkArgNotNull(options, "options");
        File datafile = new File(path, "data.mdb");
        if(options.errorIfExists() && datafile.exists() ) {
            throw new IOException("Database already exists.");
        }
        if(!options.createIfMissing() && !datafile.exists() ) {
            throw new IOException("Database does not exist.");
        }
        return new LMDB(path, options);
    }

    public void destroy(File path, Options options) throws IOException {
        File datafile = new File(path, "data.mdb");
        datafile.delete();
        File lockfile = new File(path, "lock.mdb");
        lockfile.delete();
    }

    public void repair(File path, Options options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format("lmdbjni version %s", VERSION);
    }

    public static void pushMemoryPool(int size) {
        Env.pushMemoryPool(size);
    }

    public static void popMemoryPool() {
        Env.popMemoryPool();
    }

    public static void checkArgNotNull(Object value, String name) {
        if(value==null) {
            throw new IllegalArgumentException("The "+name+" argument cannot be null");
        }
    }

}
