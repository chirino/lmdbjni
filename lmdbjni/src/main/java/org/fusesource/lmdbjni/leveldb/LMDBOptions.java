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

import org.iq80.leveldb.Options;

/**
 * Additional options which you can use with the LMDB based databases.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class LMDBOptions extends Options {

    long maxReaders = -1;
    long mapSize = -1;
    int openFlags = 0;

    public long mapSize() {
        return mapSize;
    }

    public LMDBOptions mapSize(long mapSize) {
        this.mapSize = mapSize;
        return this;
    }

    public long maxReaders() {
        return maxReaders;
    }

    public LMDBOptions maxReaders(long maxReaders) {
        this.maxReaders = maxReaders;
        return this;
    }

    public int openFlags() {
        return openFlags;
    }

    public LMDBOptions openFlags(int openFlags) {
        this.openFlags = openFlags;
        return this;
    }
}
