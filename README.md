AIPS
=====
Article Indexing & Processing System
# Building
Clone the repository

```git clone git@github.com:jmahony/AIPS.git```

Build with maven

```mvn package```

The uber jar can be found in
```target/build/AIPS-VERSION-uber.jar```

# Databases
A MongoDB and MySQL instance will need to be running.

MySQL settings can be added to config.json

```javascript
{
    "database": {
        "db_name": "jdbc:mysql://localhost:3306/dbname?rewriteBatchedStatements=true",
        "db_user" : "cnewsbites",
        "db_pass": "Q4eWahIpitP8"
    }
}```

It is advised to keep "?rewriteBatchedStatements=true" flag to minimise database round trips.

TODO: Add MongoDB config to config.json

# Running
## Normal
java -jar AIPS-1.0-SNAPSHOT-uber.jar

## Logging
java -Dlog4j.configurationFile=log4j2.xml -jar AIPS-1.0-SNAPSHOT-uber.jar

# Lucene
The lucene index will be creasted in lucene/index.

# Misc
To send the lucene index to the API

```scp * ubuntu@api.cnewsbit.es:/lucene/index```

Starting MongoDB instance
```~/utils/mongodb-linux-x86_64-2.6.0/bin/mongod --dbpath ~/data/db```
