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
import org.fusesource.lmdbjni.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static org.fusesource.lmdbjni.Constants.FIRST;
import static org.fusesource.lmdbjni.Constants.NEXT;
import static org.fusesource.lmdbjni.Constants.*;

/**
 * Unit tests for the LMDB API.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class EnvTest extends TestCase {

    static public void assertEquals(byte[] arg1, byte[] arg2) {
        assertTrue(Arrays.equals(arg1, arg2));
    }

    static File getTestDirectory(String name) throws IOException {
        File rc = new File(new File("test-data"), name);
        rc.mkdirs();
        return rc;
    }

    @Test
    public void testCRUD() throws IOException {

        String path = getTestDirectory(getName()).getCanonicalPath();
        Env env = new Env();
        env.open(path);
        Database db = env.openDatabase("foo");

        assertNull(db.put(bytes("Tampa"), bytes("green")));
        assertNull(db.put(bytes("London"), bytes("red")));

        assertNull(db.put(bytes("New York"), bytes("gray")));
        assertNull(db.put(bytes("New York"), bytes("blue")));
        assertEquals(db.put(bytes("New York"), bytes("silver"), NOOVERWRITE), bytes("blue"));

        assertEquals(db.get(bytes("Tampa")), bytes("green"));
        assertEquals(db.get(bytes("London")), bytes("red"));
        assertEquals(db.get(bytes("New York")), bytes("blue"));


        Transaction tx = env.createTransaction();
        Cursor cursor = db.openCursor(tx);

        // Lets verify cursoring works..
        LinkedList<String> keys = new LinkedList<String>();
        LinkedList<String> values = new LinkedList<String>();
        for( Entry entry = cursor.get(FIRST); entry !=null; entry = cursor.get(NEXT) ) {
            keys.add(string(entry.getKey()));
            values.add(string(entry.getValue()));
        }
        tx.commit();
        assertEquals(Arrays.asList(new String[]{"London", "New York", "Tampa"}), keys);
        assertEquals(Arrays.asList(new String[]{"red", "blue", "green"}), values);

        assertTrue(db.delete(bytes("New York")));
        assertNull(db.get(bytes("New York")));

        // We should not be able to delete it again.
        assertFalse(db.delete(bytes("New York")));

        // put /w readonly transaction should fail.
        tx = env.createTransaction(true);
        try {
            db.put(tx, bytes("New York"), bytes("silver"));
            fail("Expected LMDBException");
        } catch (LMDBException e) {
            assertEquals( LMDBException.EACCES, e.getErrorCode());
        }

        db.close();
        env.close();
    }


}
