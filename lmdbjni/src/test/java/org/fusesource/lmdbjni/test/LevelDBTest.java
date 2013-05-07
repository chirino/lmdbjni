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

package org.fusesource.lmdbjni.test;

import junit.framework.TestCase;
import org.fusesource.lmdbjni.Database;
import org.fusesource.lmdbjni.Env;
import org.fusesource.lmdbjni.leveldb.LMDB;
import org.fusesource.lmdbjni.leveldb.LMDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.iq80.leveldb.*;

import java.nio.ByteBuffer;
import java.util.*;


import static org.fusesource.lmdbjni.leveldb.LMDBFactory.*;

/**
 * A Unit test for the LevelDB API interface to LMDB.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class LevelDBTest extends TestCase {


    DBFactory factory = LMDBFactory.factory;

    static public void assertEquals(byte[] arg1, byte[] arg2) {
        assertTrue(Arrays.equals(arg1, arg2));
    }

    static File getTestDirectory(String name) throws IOException {
        File rc = new File(new File("test-data"), name);
        rc.mkdirs();
        return rc;
    }

    @Test
    public void testOpen() throws IOException {

        Options options = new Options().createIfMissing(true);

        File path = getTestDirectory(getName());
        DB db = factory.open(path, options);

        db.close();

        // Try again.. this time we expect a failure since it exists.
        options = new Options().errorIfExists(true);
        try {
            factory.open(path, options);
            fail("Expected exception.");
        } catch (IOException e) {
        }

    }

    @Test
    public void testCRUD() throws IOException, DBException {

        Options options = new Options().createIfMissing(true);

        File path = getTestDirectory(getName());
        DB db = factory.open(path, options);

        WriteOptions wo = new WriteOptions().sync(false);
        ReadOptions ro = new ReadOptions().fillCache(true).verifyChecksums(true);

        db.put(bytes("Tampa"), bytes("green"));
        db.put(bytes("London"), bytes("red"));
        db.put(bytes("New York"), bytes("blue"));

        assertEquals(db.get(bytes("Tampa"), ro), bytes("green"));
        assertEquals(db.get(bytes("London"), ro), bytes("red"));
        assertEquals(db.get(bytes("New York"), ro), bytes("blue"));

        db.delete(bytes("New York"), wo);
        assertNull(db.get(bytes("New York"), ro));

        // leveldb does not consider deleting something that does not exist an error.
        db.delete(bytes("New York"), wo);

        db.close();
    }

    @Test
    public void testIterator() throws IOException, DBException {

        Options options = new Options().createIfMissing(true);

        File path = getTestDirectory(getName());
        DB db = factory.open(path, options);

        db.put(bytes("a"), bytes("av"));
        db.put(bytes("c"), bytes("cv"));
        db.put(bytes("e"), bytes("ev"));

        ArrayList<String> expecting = new ArrayList<String>();
        expecting.add("av");
        expecting.add("cv");
        expecting.add("ev");

        ArrayList<String> actual = new ArrayList<String>();

        DBIterator iterator = db.iterator();
        for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
            actual.add(asString(iterator.peekNext().getValue()));
        }
        iterator.close();

        assertEquals(expecting, actual);

        expecting = new ArrayList<String>();
        expecting.add("cv");
        expecting.add("ev");

        actual = new ArrayList<String>();

        iterator = db.iterator();
        for (iterator.seek(bytes("b")); iterator.hasNext(); iterator.next()) {
            actual.add(asString(iterator.peekNext().getValue()));
        }
        iterator.close();

        iterator = db.iterator();
        for (iterator.seek(bytes("b")); iterator.hasPrev(); iterator.prev()) {
            actual.add(asString(iterator.peekPrev().getValue()));
        }
        iterator.close();

        db.close();
    }

    @Test
    public void testSnapshot() throws IOException, DBException {

        Options options = new Options().createIfMissing(true);

        File path = getTestDirectory(getName());
        DB db = factory.open(path, options);

        db.put(bytes("Tampa"), bytes("green"));
        db.put(bytes("London"), bytes("red"));
        db.delete(bytes("New York"));

        ReadOptions ro = new ReadOptions().snapshot(db.getSnapshot());

        db.put(bytes("New York"), bytes("blue"));

        assertEquals(db.get(bytes("Tampa"), ro), bytes("green"));
        assertEquals(db.get(bytes("London"), ro), bytes("red"));

        // Should not be able to get "New York" since it was added
        // after the snapshot
        assertNull(db.get(bytes("New York"), ro));

        ro.snapshot().close();

        // Now try again without the snapshot..
        ro.snapshot(null);
        assertEquals(db.get(bytes("New York"), ro), bytes("blue"));

        db.close();
    }

    @Test
    public void testWriteBatch() throws IOException, DBException {

        Options options = new Options().createIfMissing(true);

        File path = getTestDirectory(getName());
        DB db = factory.open(path, options);

        db.put(bytes("NA"), bytes("Na"));

        WriteBatch batch = db.createWriteBatch();
        batch.delete(bytes("NA"));
        batch.put(bytes("Tampa"), bytes("green"));
        batch.put(bytes("London"), bytes("red"));
        batch.put(bytes("New York"), bytes("blue"));
        db.write(batch);
        batch.close();

        ArrayList<String> expecting = new ArrayList<String>();
        expecting.add("London");
        expecting.add("New York");
        expecting.add("Tampa");

        ArrayList<String> actual = new ArrayList<String>();

        DBIterator iterator = db.iterator();
        for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
            actual.add(asString(iterator.peekNext().getKey()));
        }
        iterator.close();
        assertEquals(expecting, actual);

        db.close();
    }

    @Test
    public void testIssue26() throws IOException {

        LMDBFactory.pushMemoryPool(1024 * 512);
        try {
            Options options = new Options();
            options.createIfMissing(true);

            LMDB db = (LMDB) factory.open(getTestDirectory(getName()), options);
            WriteOptions op = new WriteOptions();
            for (int i = 0; i < 1024 * 1024; i++) {
                if( i % (1024*1024/10) == 0) {
                    System.out.println("at: " + i);
                    System.out.println(db.getEnv().info());
                    System.out.println(db.getEnv().stat());
                    System.out.println(db.getDatabase().stat());
                }
                byte[] key = ByteBuffer.allocate(4).putInt(i).array();
                byte[] value = ByteBuffer.allocate(1024).putInt(-i).array();
                db.put(key, value, op);
                assertTrue(Arrays.equals(db.get(key), value));
            }
            db.close();
        } finally {
            LMDBFactory.popMemoryPool();
        }

    }

    @Test
    public void testIssue27() throws IOException {

        Options options = new Options();
        options.createIfMissing(true);
        DB db = factory.open(getTestDirectory(getName()), options);
        db.close();

        try {
            db.iterator();
            fail("Expected a DBException");
        } catch(DBException e) {
        }

    }

}
