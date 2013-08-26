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

import org.fusesource.hawtjni.runtime.JniField;

import static org.fusesource.hawtjni.runtime.FieldFlag.CONSTANT;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class LMDBException extends RuntimeException {

    public static final int EINVAL = JNI.EINVAL;
    public static final int EACCES = JNI.EACCES;
    public static final int KEYEXIST = JNI.MDB_KEYEXIST;
    public static final int NOTFOUND = JNI.MDB_NOTFOUND;
    public static final int PAGE_NOTFOUND = JNI.MDB_PAGE_NOTFOUND;
    public static final int CORRUPTED = JNI.MDB_CORRUPTED;
    public static final int PANIC = JNI.MDB_PANIC;
    public static final int VERSION_MISMATCH = JNI.MDB_VERSION_MISMATCH;
    public static final int INVALID = JNI.MDB_INVALID;
    public static final int MAP_FULL = JNI.MDB_MAP_FULL;
    public static final int DBS_FULL = JNI.MDB_DBS_FULL;
    public static final int READERS_FULL = JNI.MDB_READERS_FULL;
    public static final int TLS_FULL = JNI.MDB_TLS_FULL;
    public static final int TXN_FULL = JNI.MDB_TXN_FULL;
    public static final int CURSOR_FULL = JNI.MDB_CURSOR_FULL;
    public static final int PAGE_FULL = JNI.MDB_PAGE_FULL;
    public static final int MAP_RESIZED = JNI.MDB_MAP_RESIZED;
    public static final int INCOMPATIBLE = JNI.MDB_INCOMPATIBLE;
    public static final int BAD_RSLOT = JNI.MDB_BAD_RSLOT;

    int errorCode;

    public LMDBException() {
    }

    public LMDBException(String message) {
        super(message);
    }

    public LMDBException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
