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
public enum GetOp {

    FIRST          (MDB_FIRST         ) ,
    FIRST_DUP      (MDB_FIRST_DUP     ) ,
    GET_BOTH       (MDB_GET_BOTH      ) ,
    GET_BOTH_RANGE (MDB_GET_BOTH_RANGE) ,
    GET_CURRENT    (MDB_GET_CURRENT   ) ,
    GET_MULTIPLE   (MDB_GET_MULTIPLE  ) ,
    LAST           (MDB_LAST          ) ,
    LAST_DUP       (MDB_LAST_DUP      ) ,
    NEXT           (MDB_NEXT          ) ,
    NEXT_DUP       (MDB_NEXT_DUP      ) ,
    NEXT_MULTIPLE  (MDB_NEXT_MULTIPLE ) ,
    NEXT_NODUP     (MDB_NEXT_NODUP    ) ,
    PREV           (MDB_PREV          ) ,
    PREV_DUP       (MDB_PREV_DUP      ) ,
    PREV_NODUP     (MDB_PREV_NODUP    );

    private final int value;

    GetOp(int value) {

        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
