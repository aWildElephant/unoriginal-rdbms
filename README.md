# Row-based in-memory java relational database management system

This project will not have any distinguishing feature. Its sole purpose is to allow me to implement or try to implement every part of an RDBMS.

## What's the plan?

1) Implement foreign key constraint with one or two columns
3) Load the TPC-H dataset
4) Implement the select operator
5) Implement grouping 
6) Implement aggregation
7) Implement arithmetic operations
8) Some small stuff to be able to execute TPC-H Q1

At this point I see too choices:

9.1) TPC-H Q1 performance is not shocking. Add sql support for other queries (joins as a starter)
9.2) TPC-H Q1 is shocking. A heuristic-based optimizer will be enough to improve its performance, or try to parallelize the computation 

After TPC-H, support TPC-DS and/or TPC-C.

## F.A.Q.

### Has anyone ever asked you a question on this project?
No.