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

import org.fusesource.hawtjni.runtime.*;

import static org.fusesource.hawtjni.runtime.ClassFlag.STRUCT;
import static org.fusesource.hawtjni.runtime.ClassFlag.TYPEDEF;
import static org.fusesource.hawtjni.runtime.FieldFlag.CONSTANT;
import static org.fusesource.hawtjni.runtime.MethodFlag.CONSTANT_GETTER;
import static org.fusesource.hawtjni.runtime.MethodFlag.CONSTANT_INITIALIZER;
import static org.fusesource.hawtjni.runtime.ArgFlag.*;

/**
 * This class holds all the native constant, structure and function mappings.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
@JniClass
class JNI {

    public static final Library LIBRARY = new Library("lmdbjni", JNI.class);

    static {
        JNI.LIBRARY.load();
        init();
    }

    @JniMethod(flags = {CONSTANT_INITIALIZER})
    private static final native void init();

    ///////////////////////////////////////////////////////////////////////
    //
    // Posix APIs:
    //
    ///////////////////////////////////////////////////////////////////////

    @JniMethod(flags={CONSTANT_GETTER})
    public static final native int errno();

    @JniMethod(cast="char *")
    public static final native long strerror(int errnum);

    public static final native int strlen(
            @JniArg(cast="const char *")long s);

    @JniMethod(cast="void *")
    public static final native long malloc(
            @JniArg(cast="size_t") long size);

    public static final native void free(
            @JniArg(cast="void *") long self);

    ///////////////////////////////////////////////////////////////////////
    //
    // Additional Helpers
    //
    ///////////////////////////////////////////////////////////////////////
    public static final native void buffer_copy (
            @JniArg(cast="const void *", flags={NO_OUT, CRITICAL}) byte[] src,
            @JniArg(cast="size_t") long srcPos,
            @JniArg(cast="void *") long dest,
            @JniArg(cast="size_t") long destPos,
            @JniArg(cast="size_t") long length);

    public static final native void buffer_copy (
            @JniArg(cast="const void *") long src,
            @JniArg(cast="size_t") long srcPos,
            @JniArg(cast="void *", flags={NO_IN, CRITICAL}) byte[] dest,
            @JniArg(cast="size_t") long destPos,
            @JniArg(cast="size_t") long length);

    ///////////////////////////////////////////////////////////////////////
    //
    // The lmdb API
    //
    ///////////////////////////////////////////////////////////////////////

    //====================================================//
    // Version Info
    //====================================================//
    @JniField(flags = {CONSTANT})
    static public int MDB_VERSION_MAJOR;
    @JniField(flags = {CONSTANT})
    static public int MDB_VERSION_MINOR;
    @JniField(flags = {CONSTANT})
    static public int MDB_VERSION_PATCH;
    @JniField(flags = {CONSTANT})
    static public int MDB_VERSION_FULL;

    @JniField(cast = "const char *", flags = {CONSTANT})
    static long MDB_VERSION_DATE;
    @JniField(cast = "const char *", flags = {CONSTANT})
    static long MDB_VERSION_STRING;

    //====================================================//
    // Environment Flags
    //====================================================//
    @JniField(flags = {CONSTANT})
    static public int MDB_FIXEDMAP;
    @JniField(flags = {CONSTANT})
    static public int MDB_NOSUBDIR;
    @JniField(flags = {CONSTANT})
    static public int MDB_NOSYNC;
    @JniField(flags = {CONSTANT})
    static public int MDB_RDONLY;
    @JniField(flags = {CONSTANT})
    static public int MDB_NOMETASYNC;
    @JniField(flags = {CONSTANT})
    static public int MDB_WRITEMAP;
    @JniField(flags = {CONSTANT})
    static public int MDB_MAPASYNC;
    @JniField(flags = {CONSTANT})
    static public int MDB_NOTLS;

    //====================================================//
    // Database Flags
    //====================================================//
    @JniField(flags = {CONSTANT})
    static public int MDB_REVERSEKEY;
    @JniField(flags = {CONSTANT})
    static public int MDB_DUPSORT;
    @JniField(flags = {CONSTANT})
    static public int MDB_INTEGERKEY;
    @JniField(flags = {CONSTANT})
    static public int MDB_DUPFIXED;
    @JniField(flags = {CONSTANT})
    static public int MDB_INTEGERDUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_REVERSEDUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_CREATE;

    //====================================================//
    // Write Flags
    //====================================================//
    @JniField(flags = {CONSTANT})
    static public int MDB_NOOVERWRITE;
    @JniField(flags = {CONSTANT})
    static public int MDB_NODUPDATA;
    @JniField(flags = {CONSTANT})
    static public int MDB_CURRENT;
    @JniField(flags = {CONSTANT})
    static public int MDB_RESERVE;
    @JniField(flags = {CONSTANT})
    static public int MDB_APPEND;
    @JniField(flags = {CONSTANT})
    static public int MDB_APPENDDUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_MULTIPLE;

    //====================================================//
    // enum MDB_cursor_op:
    //====================================================//
    @JniField(flags = {CONSTANT})
    static public int MDB_FIRST;
    @JniField(flags = {CONSTANT})
    static public int MDB_FIRST_DUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_GET_BOTH;
    @JniField(flags = {CONSTANT})
    static public int MDB_GET_BOTH_RANGE;
    @JniField(flags = {CONSTANT})
    static public int MDB_GET_CURRENT;
    @JniField(flags = {CONSTANT})
    static public int MDB_GET_MULTIPLE;
    @JniField(flags = {CONSTANT})
    static public int MDB_LAST;
    @JniField(flags = {CONSTANT})
    static public int MDB_LAST_DUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_NEXT;
    @JniField(flags = {CONSTANT})
    static public int MDB_NEXT_DUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_NEXT_MULTIPLE;
    @JniField(flags = {CONSTANT})
    static public int MDB_NEXT_NODUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_PREV;
    @JniField(flags = {CONSTANT})
    static public int MDB_PREV_DUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_PREV_NODUP;
    @JniField(flags = {CONSTANT})
    static public int MDB_SET;
    @JniField(flags = {CONSTANT})
    static public int MDB_SET_KEY;
    @JniField(flags = {CONSTANT})
    static public int MDB_SET_RANGE;

    //====================================================//
    // Return Codes
    //====================================================//
    @JniField(flags = {CONSTANT})
    static public int MDB_SUCCESS;
    @JniField(flags = {CONSTANT})
    static public int MDB_KEYEXIST;
    @JniField(flags = {CONSTANT})
    static public int MDB_NOTFOUND;
    @JniField(flags = {CONSTANT})
    static public int MDB_PAGE_NOTFOUND;
    @JniField(flags = {CONSTANT})
    static public int MDB_CORRUPTED;
    @JniField(flags = {CONSTANT})
    static public int MDB_PANIC;
    @JniField(flags = {CONSTANT})
    static public int MDB_VERSION_MISMATCH;
    @JniField(flags = {CONSTANT})
    static public int MDB_INVALID;
    @JniField(flags = {CONSTANT})
    static public int MDB_MAP_FULL;
    @JniField(flags = {CONSTANT})
    static public int MDB_DBS_FULL;
    @JniField(flags = {CONSTANT})
    static public int MDB_READERS_FULL;
    @JniField(flags = {CONSTANT})
    static public int MDB_TLS_FULL;
    @JniField(flags = {CONSTANT})
    static public int MDB_TXN_FULL;
    @JniField(flags = {CONSTANT})
    static public int MDB_CURSOR_FULL;
    @JniField(flags = {CONSTANT})
    static public int MDB_PAGE_FULL;
    @JniField(flags = {CONSTANT})
    static public int MDB_MAP_RESIZED;
    @JniField(flags = {CONSTANT})
    static public int MDB_INCOMPATIBLE;
    @JniField(flags = {CONSTANT})
    static public int MDB_BAD_RSLOT;
    @JniField(flags = {CONSTANT})
    static public int MDB_LAST_ERRCODE;

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#structMDB__envinfo">details</a>
     */
   @JniClass(flags = {STRUCT, TYPEDEF})
   static public class MDB_envinfo {
       @JniField(cast = "void *")
       public long me_mapaddr;
       @JniField(cast = "size_t")
       public long me_mapsize;
       @JniField(cast = "size_t")
       public long me_last_pgno;
       @JniField(cast = "size_t")
       public long me_last_txnid;
       @JniField(cast = "unsigned int")
       public long me_maxreaders;
       @JniField(cast = "unsigned int")
       public long me_numreaders;
   }

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#structMDB_stat">details</a>
     */
    @JniClass(flags = {STRUCT, TYPEDEF})
    public static class MDB_stat {
        @JniField(cast = "unsigned int")
        public long ms_psize;
        @JniField(cast = "unsigned int")
        public long ms_depth;
        @JniField(cast = "size_t")
        public long ms_branch_pages;
        @JniField(cast = "size_t")
        public long ms_leaf_pages;
        @JniField(cast = "size_t")
        public long ms_overflow_pages;
        @JniField(cast = "size_t")
        public long ms_entries;
    }

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#structMDB_val">details</a>
     */
    @JniClass(flags = {STRUCT, TYPEDEF})
    public static class MDB_val {
        @JniField(cast = "size_t")
        public long mv_size;
        @JniField(cast = "void *")
        public long mv_data;
    }

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga569e66c1e3edc1a6016b86719ee3d098">details</a>
     */
    @JniMethod(cast="char *")
    public static final native long mdb_strerror(int err);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#gaad6be3d8dcd4ea01f8df436f41d158d4">details</a>
     */
    @JniMethod
    public static final native int mdb_env_create(
            @JniArg(cast = "MDB_env **", flags={NO_IN}) long[] env);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga32a193c6bf4d7d5c5d579e71f22e9340">details</a>
     */
    @JniMethod
    public static final native int mdb_env_open(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "const char *") String path,
            @JniArg(cast = "unsigned int") int flags,
            @JniArg(cast = "mdb_mode_t") int mode
    );

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga5d51d6130325f7353db0955dbedbc378">details</a>
     */
    @JniMethod
    public static final native int mdb_env_copy(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "const char *") String path);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#gaf881dca452050efbd434cd16e4bae255">details</a>
     */
    @JniMethod
    public static final native int mdb_env_stat(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "MDB_stat *", flags = {NO_IN}) MDB_stat stat);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga18769362c7e7d6cf91889a028a5c5947">details</a>
     */
    @JniMethod
    public static final native int mdb_env_info(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = " MDB_envinfo *", flags = {NO_IN}) MDB_envinfo stat);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga85e61f05aa68b520cc6c3b981dba5037">details</a>
     */
    @JniMethod
    public static final native int mdb_env_sync(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "int") int force);


    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga4366c43ada8874588b6a62fbda2d1e95">details</a>
     */
    @JniMethod
    public static final native void mdb_env_close(
            @JniArg(cast = "MDB_env *") long env);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga83f66cf02bfd42119451e9468dc58445">details</a>
     */
    @JniMethod
    public static final native int mdb_env_set_flags(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "unsigned int") int flags,
            @JniArg(cast = "int") int onoff);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#ga2733aefc6f50beb49dd0c6eb19b067d9">details</a>
     */
    @JniMethod
    public static final native int mdb_env_get_flags(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "unsigned int *") long[] flags);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#gac699fdd8c4f8013577cb933fb6a757fe">details</a>
     */
    @JniMethod
    public static final native int mdb_env_get_path(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "const char **", flags={NO_IN}) long[] path);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#gaa2506ec8dab3d969b0e609cd82e619e5">details</a>
     */
    @JniMethod
    public static final native int mdb_env_set_mapsize(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "size_t") long size);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#gae687966c24b790630be2a41573fe40e2">details</a>
     */
    @JniMethod
    public static final native int mdb_env_set_maxreaders(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "unsigned int") long readers);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_env_get_maxreaders(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "unsigned int *") long[] readers);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_env_set_maxdbs(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "unsigned int") long dbs);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_txn_begin(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "MDB_txn *") long parent,
            @JniArg(cast = "unsigned int") long flags,
            @JniArg(cast = "MDB_txn **", flags={NO_IN}) long[] txn);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_txn_commit(
            @JniArg(cast = "MDB_txn *") long txn);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native void mdb_txn_abort(
            @JniArg(cast = "MDB_txn *") long txn);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native void mdb_txn_reset(
            @JniArg(cast = "MDB_txn *") long txn);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_txn_renew(
            @JniArg(cast = "MDB_txn *") long txn);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_dbi_open(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "const char *") String name,
            @JniArg(cast = "unsigned int") long flags,
            @JniArg(cast = "unsigned int *") long[] dbi);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_stat(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int ") long dbi,
            @JniArg(cast = "MDB_stat *", flags = {NO_IN}) MDB_stat stat);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native void mdb_dbi_close(
            @JniArg(cast = "MDB_env *") long env,
            @JniArg(cast = "unsigned int ") long dbi);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_drop(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int ") long dbi,
            int del);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_set_compare(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int ") long dbi,
            @JniArg(cast = "MDB_cmp_func *") long cmp);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_set_dupsort(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int ") long dbi,
            @JniArg(cast = "MDB_cmp_func *") long cmp);


    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_get(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int ") long dbi,
            @JniArg(cast = "MDB_val *", flags={NO_OUT}) MDB_val key,
            @JniArg(cast = "MDB_val *", flags={NO_IN}) MDB_val data);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_put(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int ") long dbi,
            @JniArg(cast = "MDB_val *", flags={NO_OUT}) MDB_val key,
            @JniArg(cast = "MDB_val *") MDB_val data,
            @JniArg(cast = "unsigned int") int flags);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_del(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int ") long dbi,
            @JniArg(cast = "MDB_val *", flags={NO_OUT}) MDB_val key,
            @JniArg(cast = "MDB_val *", flags={NO_OUT}) MDB_val data);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_cursor_open(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int") long dbi,
            @JniArg(cast = "MDB_cursor **", flags={NO_IN}) long[] cursor);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native void mdb_cursor_close(
            @JniArg(cast = "MDB_cursor *") long cursor);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_cursor_renew(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "MDB_cursor *") long cursor);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod(cast = "MDB_txn *")
    public static final native long mdb_cursor_txn(
            @JniArg(cast = "MDB_cursor *") long cursor);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod(cast = "unsigned int")
    public static final native long mdb_cursor_dbi(
            @JniArg(cast = "MDB_cursor *") long cursor);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_cursor_get(
            @JniArg(cast = "MDB_cursor *") long cursor,
            @JniArg(cast = "MDB_val *") MDB_val key,
            @JniArg(cast = "MDB_val *") MDB_val data,
            @JniArg(cast = "MDB_cursor_op") int op);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_cursor_put(
            @JniArg(cast = "MDB_cursor *") long cursor,
            @JniArg(cast = "MDB_val *", flags = {NO_OUT}) MDB_val key,
            @JniArg(cast = "MDB_val *", flags = {NO_OUT}) MDB_val data,
            @JniArg(cast = "unsigned int ") int flags);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_cursor_del(
            @JniArg(cast = "MDB_cursor *") long cursor,
            @JniArg(cast = "unsigned int ") int flags);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_cursor_count(
            @JniArg(cast = "MDB_cursor *") long cursor,
            @JniArg(cast = "size_t *") long[] countp);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_cmp(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int") long dbi,
            @JniArg(cast = "MDB_val *", flags = {NO_OUT}) MDB_val a,
            @JniArg(cast = "MDB_val *", flags = {NO_OUT}) MDB_val b);

    /**
     * <a href="http://symas.com/mdb/doc/group__mdb.html#">details</a>
     */
    @JniMethod
    public static final native int mdb_dcmp(
            @JniArg(cast = "MDB_txn *") long txn,
            @JniArg(cast = "unsigned int") long dbi,
            @JniArg(cast = "MDB_val *", flags = {NO_OUT}) MDB_val a,
            @JniArg(cast = "MDB_val *", flags = {NO_OUT}) MDB_val b);


}
