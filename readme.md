# LMDB JNI

## Description

LMDB JNI gives you a Java interface to the 
[OpenLDAP Lightning Memory-Mapped Database](http://symas.com/mdb/) library
which is a fast key-value storage library written for OpenLDAP project
that provides an ordered mapping from string keys to string values.

## Using Prebuilt Jar

The prebuilt binary jars only work on 64 bit OS X or Linux machines.

### License

This project is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) but the binary jar it produces also includes the the `liblmdb` library of the OpenLDAP project which is licensed under the [The OpenLDAP Public License](http://www.openldap.org/software/release/license.html).

### Downloading the Jar

Just add the following jar to your java project:
[lmdbjni-all-99-master-SNAPSHOT.jar](http://repo.fusesource.com/nexus/service/local/artifact/maven/redirect?r=snapshots&g=org.fusesource.lmdbjni&a=lmdbjni-all&v=99-master-SNAPSHOT&e=jar)

### Using as a Maven Dependency

You just nee to add the following dependency and repository to your Maven `pom.xml`.

    <dependencies>
      <dependency>
        <groupId>org.fusesource.lmdbjni</groupId>
        <artifactId>lmdbjni-all</artifactId>
        <version>99-master-SNAPSHOT</version>
      </dependency>
    </dependencies>
    ...
    <repositories>
        <repository>
          <id>fusesource.nexus.snapshot</id>
          <name>FuseSource Community Snapshot Repository</name>
          <url>http://repo.fusesource.com/nexus/content/groups/public-snapshots</url>
        </repository>
    </repositories>

## API Usage:

The [Javadocs](http://lmdbjni.fusesource.org/maven/99-master-SNAPSHOT/apidocs/org/fusesource/lmdbjni/package-summary.html) 
dont' have too many details yet.  Please send patches to improve them!

Recommended Package imports:

    import org.fusesource.lmdbjni.*;
    import static org.fusesource.lmdbjni.Constants.*;

Opening and closing the database.

    Env env = new Env();
    try {
      env.open("/tmp/mydb");
      Database db = env.openDatabase("foo");
      
      ... // use the db
      db.close();
    } finally {
      // Make sure you close the env to avoid resource leaks.
      env.close();
    }

Putting, Getting, and Deleting key/values.

    db.put(bytes("Tampa"), bytes("rocks"));
    String value = string(db.get(bytes("Tampa")));
    db.delete(bytes("Tampa"));

Performing Atomic/Transacted Updates:

    Transaction tx = env.createTransaction();
    boolean ok = false;
    try {
      db.delete(tx, bytes("Denver"));
      db.put(tx, bytes("Tampa"), bytes("green"));
      db.put(tx, bytes("London"), bytes("red"));
      ok = true;
    } finally {
      // Make sure you either commit or rollback to avoid resource leaks.
      if( ok ) {
        tx.commit();
      } else {
        tx.abort();
      }
    }

Working against a Snapshot view of the Database:

    // cerate a read-only transaction...
    Transaction tx = env.createTransaction(true);
    try {
      
      // All read operations will now use the same 
      // consistent view of the data.
      ... = db.db.openCursor(tx);
      ... = db.get(tx, bytes("Tampa"));

    } finally {
      // Make sure you commit the transaction to avoid resource leaks.
      tx.commit();
    }

Iterating key/values:

    Transaction tx = env.createTransaction(true);
    try {
      Cursor cursor = db.openCursor(tx);
      try {
        for( Entry entry = cursor.get(FIRST); entry !=null; entry = cursor.get(NEXT) ) {
            String key = string(entry.getKey());
            String value = string(entry.getValue());
            System.out.println(key+" = "+value);
        }
      } finally {
        // Make sure you close the cursor to avoid leaking reasources.
        cursor.close();
      }

    } finally {
      // Make sure you commit the transaction to avoid resource leaks.
      tx.commit();
    }

Using a memory pool to make native memory allocations more efficient:

    Env.pushMemoryPool(1024 * 512);
    try {
        // .. work with the DB in here, 
    } finally {
        Env.popMemoryPool();
    }

## Optional LevelDB API Facade

This project provides an optional implementation of the [LevelDB APIs](https://github.com/dain/leveldb).
The LevelDB API is simpler than the LMDB API.  If you can live with the restricted features the LevelDB API 
provides you might want to use the LevelDB API instead. 

### Additional Maven Dependencies

    <dependency>
      <groupId>org.iq80.leveldb</groupId>
      <artifactId>leveldb-api</artifactId>
      <version>0.5</version>
    </dependency>

### Creating a Database using the LevelDB APIs:

    import org.iq80.leveldb.*;
    import static org.fusesource.lmdbjni.leveldb.LMDBFactory.*;
    import java.io.*;
    ...
    Options options = new Options();
    options.createIfMissing(true);
    DB db = factory.open(new File("example"), options);
    try {
      // Use the db in here....
    } finally {
      // Make sure you close the db to shutdown the 
      // database and avoid resource leaks.
      db.close();
    }


