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
import static org.fusesource.lmdbjni.Util.checkErrorCode;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class Transaction extends NativeObject {

    private final Env env;

    Transaction(Env env, long self) {
        super(self);
        this.env = env;
    }

    public void renew() {
        checkErrorCode(mdb_txn_renew(pointer()));
    }

    public void commit() {
        if( self != 0  ) {
            checkErrorCode(mdb_txn_commit(self));
            self = 0;
        }
    }

    public void reset() {
        checkAllocated();
        mdb_txn_reset(pointer());
    }

    public void abort() {
        if( self != 0  ) {
            mdb_txn_abort(self);
            self = 0;
        }
    }

}
