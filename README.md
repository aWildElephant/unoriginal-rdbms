
# Column-based in-memory java relational database management system

This project will not have any distinguishing feature. Its sole purpose is to allow me to implement or try to implement 
every part of an RDBMS.

## Build

Use `mvn clean install` to build the project.

You can then find:
* the embedded driver `embedded/target/rdbms-embedded-1.0-SNAPSHOT-jar-with-dependencies.jar`
* the standalone server `server/standalone/target/rdbms-standalone-server-1.0-SNAPSHOT-jar-with-dependencies.jar`
* the driver for the standalone server `server/driver/target/rdbms-driver-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Run the TPC-H tests

The database can run some TPC-H queries. You can run them on the scale factor 1 dataset with:
`mvn clean test -Ptpch`

You need to set the `TPCH_DATA_DIRECTORY` environment variable, and place the gzip compressed files in 
`<TPCH_DATA_DIRECTORY>/1/`, with the `.tbl.gz` extension.

## Milestones
- prior to 2019-04-14: support stuff for TPC-H Q1, load TPC-H scale factor 1 dataset, improve muscle mass, etc.
- 2019-04-14: TPC-H Q1 support (ran in around 13s)
- 2019-08-10: TPC-H Q3 support
- 2019-11-01: TPC-H Q5 support
- 2019-11-02: TPC-H Q6, Q7 and Q8 support
- 2019-11-03: TPC-H Q10 support
- 2019-12-19: TPC-H Q4 (supported decorrelating its subquery so that it doesn't take hours)
- 2021-03-17: Support all TPC-H queries (implemented what was needed for unnesting using <u>Unnesting Arbitrary Queries</u> by Thomas Neumann and Alfons Kemper)

## Next steps

- Fix TODOs
- Implement UPDATE
- Implement explain analyze to debug latency including parsing/optimizing
- Implement prepared update
- Improve error message when a feature test fail
- Improve evaluation of formulas: filter and map nodes are a lot slower than they should be

## F.A.Q.

### Has anyone ever asked you a question on this project?
No.