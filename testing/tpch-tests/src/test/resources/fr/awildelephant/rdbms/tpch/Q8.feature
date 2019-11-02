@todo
Feature: TPC-H Q8

  Background: TPC-H dataset

    Given I load nation scale factor 1
    And I load customer scale factor 1
    And I load lineitem scale factor 1
    And I load orders scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q8

    When I execute the query
    """
    SELECT
      o_year,
      SUM(CASE WHEN nation = 'BRAZIL'
          THEN volume
          ELSE 0
          END) / SUM(volume) AS mkt_share
    FROM (
      SELECT
        EXTRACT(YEAR FROM o_orderdate) AS o_year,
        l_extendedprice * (1 - l_discount) AS volume,
        n2.n_name AS nation
      FROM
        part,
        supplier,
        lineitem,
        orders,
        customer,
        nation n1,
        nation n2,
        region
      WHERE
      p_partkey = l_partkey
      AND s_suppkey = l_suppkey
      AND l_orderkey = o_orderkey
      AND o_custkey = c_custkey
      AND c_nationkey = n1.n_nationkey
      AND n1.n_regionkey = r_regionkey
      AND r_name = 'AMERICA'
      AND s_nationkey = n2.n_nationkey
      AND o_orderdate BETWEEN DATE '1995-01-01' AND DATE '1996-12-31'
      AND p_type = 'ECONOMY ANODIZED STEEL'
    ) AS all_nations
    GROUP BY
      o_year
    ORDER BY
      o_year;
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q8.test
    Then I expect the result
      | o_year  | mkt_share |
      | INTEGER | DECIMAL   |
      | 1995    | 0.034436  |
      | 1996    | 0.041486  |
