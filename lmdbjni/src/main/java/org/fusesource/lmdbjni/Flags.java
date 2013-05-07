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

import static org.fusesource.lmdbjni.JNI.*;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Flags {

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

    public static final CursorOp FIRST          = CursorOp.FIRST          ;
    public static final CursorOp FIRST_DUP      = CursorOp.FIRST_DUP      ;
    public static final CursorOp GET_BOTH       = CursorOp.GET_BOTH       ;
    public static final CursorOp GET_BOTH_RANGE = CursorOp.GET_BOTH_RANGE ;
    public static final CursorOp GET_CURRENT    = CursorOp.GET_CURRENT    ;
    public static final CursorOp GET_MULTIPLE   = CursorOp.GET_MULTIPLE   ;
    public static final CursorOp LAST           = CursorOp.LAST           ;
    public static final CursorOp LAST_DUP       = CursorOp.LAST_DUP       ;
    public static final CursorOp NEXT           = CursorOp.NEXT           ;
    public static final CursorOp NEXT_DUP       = CursorOp.NEXT_DUP       ;
    public static final CursorOp NEXT_MULTIPLE  = CursorOp.NEXT_MULTIPLE  ;
    public static final CursorOp NEXT_NODUP     = CursorOp.NEXT_NODUP     ;
    public static final CursorOp PREV           = CursorOp.PREV           ;
    public static final CursorOp PREV_DUP       = CursorOp.PREV_DUP       ;
    public static final CursorOp PREV_NODUP     = CursorOp.PREV_NODUP     ;
    public static final CursorOp SET            = CursorOp.SET            ;
    public static final CursorOp SET_KEY        = CursorOp.SET_KEY        ;
    public static final CursorOp SET_RANGE      = CursorOp.SET_RANGE      ;

}
