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

import java.io.UnsupportedEncodingException;

import static org.fusesource.lmdbjni.JNI.*;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Constants {

    public static final int FIXEDMAP    = MDB_FIXEDMAP    ;
    public static final int NOSUBDIR    = MDB_NOSUBDIR    ;
    public static final int NOSYNC      = MDB_NOSYNC      ;
    public static final int RDONLY      = MDB_RDONLY      ;
    public static final int NOMETASYNC  = MDB_NOMETASYNC  ;
    public static final int WRITEMAP    = MDB_WRITEMAP    ;
    public static final int MAPASYNC    = MDB_MAPASYNC    ;
    public static final int NOTLS       = MDB_NOTLS       ;

    //====================================================//
    // Database Flags
    //====================================================//
    public static final int REVERSEKEY  = MDB_REVERSEKEY  ;
    public static final int DUPSORT     = MDB_DUPSORT     ;
    public static final int INTEGERKEY  = MDB_INTEGERKEY  ;
    public static final int DUPFIXED    = MDB_DUPFIXED    ;
    public static final int INTEGERDUP  = MDB_INTEGERDUP  ;
    public static final int REVERSEDUP  = MDB_REVERSEDUP  ;
    public static final int CREATE      = MDB_CREATE      ;

    //====================================================//
    // Write Flags
    //====================================================//
    public static final int NOOVERWRITE = MDB_NOOVERWRITE ;
    public static final int NODUPDATA   = MDB_NODUPDATA   ;
    public static final int CURRENT     = MDB_CURRENT     ;
    public static final int RESERVE     = MDB_RESERVE     ;
    public static final int APPEND      = MDB_APPEND      ;
    public static final int APPENDDUP   = MDB_APPENDDUP   ;
    public static final int MULTIPLE    = MDB_MULTIPLE    ;

    public static final GetOp FIRST          = GetOp.FIRST          ;
    public static final GetOp FIRST_DUP      = GetOp.FIRST_DUP      ;
    public static final GetOp GET_BOTH       = GetOp.GET_BOTH       ;
    public static final GetOp GET_BOTH_RANGE = GetOp.GET_BOTH_RANGE ;
    public static final GetOp GET_CURRENT    = GetOp.GET_CURRENT    ;
    public static final GetOp GET_MULTIPLE   = GetOp.GET_MULTIPLE   ;
    public static final GetOp LAST           = GetOp.LAST           ;
    public static final GetOp LAST_DUP       = GetOp.LAST_DUP       ;
    public static final GetOp NEXT           = GetOp.NEXT           ;
    public static final GetOp NEXT_DUP       = GetOp.NEXT_DUP       ;
    public static final GetOp NEXT_MULTIPLE  = GetOp.NEXT_MULTIPLE  ;
    public static final GetOp NEXT_NODUP     = GetOp.NEXT_NODUP     ;
    public static final GetOp PREV           = GetOp.PREV           ;
    public static final GetOp PREV_DUP       = GetOp.PREV_DUP       ;
    public static final GetOp PREV_NODUP     = GetOp.PREV_NODUP     ;

    public static final SeekOp KEY        = SeekOp.KEY;
    public static final SeekOp RANGE      = SeekOp.RANGE;

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

    public static String string(byte value[]) {
        if( value == null) {
            return null;
        }
        try {
            return new String(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
