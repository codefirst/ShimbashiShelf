ShimbashiShelf
======================


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

* run by scripts

    $ ./scripts/shimbashishelf.sh <command> <args..>

commands
- search <word>         : search documents by a <word>
- index <filepath>      : index a file
- index-all <directory> : index all files under <directory>
- search-by-path        : search by a absolute path (enclosed phrase)
- commit <filepath>     : commit a file to a version control system (repository directory is ./files/)
- history               : show history
- monitor               : monitor repository direcotry ( auto commit / remove )
