Feature: TPC-H Q3

  Background: TPC-H dataset

    Given I load customer scale factor 1
    And I load lineitem scale factor 1
    And I load orders scale factor 1

  Scenario: I execute TPC-H Q3

    When I execute the query
    """
    SELECT
      l_orderkey,
      SUM(l_extendedprice * (1 - l_discount)) AS revenue,
      o_orderdate,
      o_shippriority
    FROM
      customer,
      orders,
      lineitem
    WHERE
      c_mktsegment = 'BUILDING'
      AND c_custkey = o_custkey
      AND l_orderkey = o_orderkey
      AND o_orderdate < date '1995-03-15'
      AND l_shipdate > date '1995-03-15'
    GROUP BY
      l_orderkey,
      o_orderdate,
      o_shippriority
    ORDER BY
      revenue desc,
      o_orderdate
    LIMIT 10
    """

    Then I expect the result
      | l_orderkey | revenue   | o_orderdate | o_shippriority |
      | INTEGER    | DECIMAL   | DATE        | INTEGER        |
      | 2456423    | 406181.01 | 1995-03-05  | 0              |
      | 3459808    | 405838.70 | 1995-03-04  | 0              |
      | 492164     | 390324.06 | 1995-02-19  | 0              |
      | 1188320    | 384537.94 | 1995-03-09  | 0              |
      | 2435712    | 378673.06 | 1995-02-26  | 0              |
      | 4878020    | 378376.80 | 1995-03-12  | 0              |
      | 5521732    | 375153.92 | 1995-03-13  | 0              |
      | 2628192    | 373133.31 | 1995-02-22  | 0              |
      | 993600     | 371407.46 | 1995-03-05  | 0              |
      | 2300070    | 367371.15 | 1995-03-13  | 0              |
