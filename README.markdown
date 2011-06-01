ShimbashiShelf
======================
ShimbashiShelf is a document management system.
There are main features included:

 * Full-text search for various type documents (doc / xls / ppt / pdf / etc.)
 * Auto version management (by Git)
 * Calendar view (easily search documents you edited yesterday)
 * Executable Jar (not need to install)

commiters
----------------------
 * @suer
 * @mallowlabs
 * @mzp
 * @banjun

for developer
----------------------

* setup develop environment

    $ cp bleis/* .git/hooks

* build ShimbashiShelf

    $ sbt embed
    $ sbt jar

* setup

    $ export SHIMBASHI_SHELF_HOME=/path/to/something
    $ mkdir -p $SHIMBASHI_SHELF_HOME
    $ cp -r home/* $SHIMBASHI_SHELF_HOME

* start webserver

    $ java -jar ./target/scala_2.8.1/shimbashi-shelf_2.8.1-embedded-0.1.jar

* run by scripts (for debugging)

    $ ./scripts/shimbashishelf.sh <command> <args..>

commands:
    - search <word>         : search documents by a <word>
    - index <filepath>      : index a file
    - index-all <directory> : index all files under <directory>
    - search-by-path        : search by a absolute path (enclosed phrase)
    - commit <filepath>     : commit a file to a version control system (repository directory is ./files/)
    - history               : show history
    - monitor               : monitor repository direcotry ( auto commit / remove )

License
----------------------

The MIT License

Copyright (C) 2011 codefirst.org. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Credits
----------------------
ShimbashiShelf depends on:

 * Apache PDFBox - The Apache License, Version 2.0
 * Apache POI - The Apache License, Version 2.0
 * Apache Lucene - The Apache License, Version 2.0
 * Apache log4j - The Apache License, Version 2.0
 * Apache Commons JCI - The Apache License, Version 2.0
 * JGit - Eclipse Distribution License - v 1.0
 * jChardet - Mozilla Public License 1.1
 * Winstone - the Common Development and Distribution License (CDDL)

codefirst.org thanks to these open source software.
