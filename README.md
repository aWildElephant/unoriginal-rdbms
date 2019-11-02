# Row-based in-memory java relational database management system

This project will not have any distinguishing feature. Its sole purpose is to allow me to implement or try to implement 
every part of an RDBMS.

## Build

Use `mvn clean install` to build the project.

You can then find:
* the embedded driver `embedded/target/rdbms-embedded-1.0-SNAPSHOT-jar-with-dependencies.jar`
* the standalone server `server/standalone/target/rdbms-standalone-server-1.0-SNAPSHOT-jar-with-dependencies.jar`
* the driver for the standalone server `server/driver/target/rdbms-driver-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Run the TPC-H tests

The database can run TPC-H Q1 and Q3 at the moment. You can run these queries on the scale factor 1 dataset with:
`mvn clean test -Ptpch`

You need to set the `TPCH_DATA_DIRECTORY` environment variable, and place the gzip compressed files in 
`<TPCH_DATA_DIRECTORY>/1/`, with the `.tbl.gz` extension.

## Milestones
- prior to 2019-04-14: support stuff for TPC-H Q1, load TPC-H scale factor 1 dataset, improve muscle mass, etc.
- 2019-04-14: TPC-H Q1 support (ran in around 13s)
- 2019-08-10: TPC-H Q3 support
- 2019-11-01: TPC-H Q5 support
- 2019-11-02: TPC-H Q6 support

## Next steps

- Fix TODOs
- Support concurrent queries, probably by having a global queue and executing them one at a time
- Improve loading speed of the TPC-H scale factor 1 dataset (currently around 2 minute)
- Support TPC-H Q2

## F.A.Q.

### Has anyone ever asked you a question on this project?
No.