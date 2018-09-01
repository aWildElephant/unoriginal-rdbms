# Row-based in-memory java relational database management system

This project will not have any distinguishing feature. Its sole purpose is to allow me to implement or try to implement every part of an RDBMS.

## What's the plan?

* Implement arithmetic operations
* Support count(*) aggregate without a group by clause (in progress)
* Support loading from csv
* Have a test loading the TPC-H SF1 dataset, asserting count(*) should be enough
* Implement the select operator
* Implement grouping
* Implement aggregation
* Some small stuff to be able to execute TPC-H Q1
* Have a test executing TPC-H Q1 SF1

At this point I see too choices:

* TPC-H Q1 performance is not shocking. Add sql support for other queries (joins as a starter)
* TPC-H Q1 is shocking. A heuristic-based optimizer will be enough to improve its performance, or try to parallelize the computation

After TPC-H, support TPC-DS and/or TPC-C.

## F.A.Q.

### Has anyone ever asked you a question on this project?
No.