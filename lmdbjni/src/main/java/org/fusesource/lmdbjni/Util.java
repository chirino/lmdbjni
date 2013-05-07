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

import static org.fusesource.lmdbjni.JNI.mdb_strerror;
import static org.fusesource.lmdbjni.JNI.strlen;

/**
 * Some miscellaneous utility functions.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
class Util {

    public  static int errno() {
        return errno();
    }

    public  static String strerror() {
        return string(JNI.strerror(errno()));
    }

    public static String string(long ptr) {
        if( ptr == 0 )
            return null;
        return new String(NativeBuffer.create(ptr, strlen(ptr)).toByteArray());
    }

    public static void checkErrorCode(int rc) {
        if( rc != 0 ) {
            String msg = string(mdb_strerror(rc));
            throw new LMDBException(msg, rc);
        }
    }

    public static void checkArgNotNull(Object value, String name) {
        if(value==null) {
            throw new IllegalArgumentException("The "+name+" argument cannot be null");
        }
    }

}
