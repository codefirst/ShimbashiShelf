ShimbashiShelf
======================


for developer
----------------------

* setup develop environment

    $ cp bleis/* .git/hooks

* build ShimbashiShelf

    $ sbt update
    $ sbt compile
    $ sbt package
    $ sbt jar

* start webserver

    $ sbt jettry-run

* run by scripts

    $ ./scripts/shimbashishelf.sh <command> <args..>

commands
- search <word>         : search documents by a <word>
- index <filepath>      : index a file
- index-all <directory> : index all files under <directory>
- search-by-path        : search by a absolute path (enclosed phrase)
- commit <filepath>     : commit a file to a version control system (repository directory is ./files/)
