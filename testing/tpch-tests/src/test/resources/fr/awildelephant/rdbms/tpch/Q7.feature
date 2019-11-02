Feature: TPC-H Q7

  Background: TPC-H dataset

    Given I load nation scale factor 1
    And I load customer scale factor 1
    And I load lineitem scale factor 1
    And I load orders scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q7

    When I execute the query
    """
    SELECT
      supp_nation,
      cust_nation,
      l_year,
      SUM(volume) AS revenue
    FROM (
      SELECT
        n1.n_name AS supp_nation,
        n2.n_name AS cust_nation,
        EXTRACT(YEAR FROM l_shipdate) AS l_year,
        l_extendedprice * (1 - l_discount) AS volume
      FROM
        supplier,
        lineitem,
        orders,
        customer,
        nation n1,
        nation n2
      WHERE
        s_suppkey = l_suppkey
        AND o_orderkey = l_orderkey
        AND c_custkey = o_custkey
        AND s_nationkey = n1.n_nationkey
        AND c_nationkey = n2.n_nationkey
        AND (
          (n1.n_name = 'FRANCE' AND n2.n_name = 'GERMANY')
          OR (n1.n_name = 'GERMANY' AND n2.n_name = 'FRANCE')
        )
        AND l_shipdate BETWEEN DATE '1995-01-01' AND DATE '1996-12-31'
    ) as shipping
    GROUP BY
      supp_nation,
      cust_nation,
      l_year
    ORDER BY
      supp_nation,
      cust_nation,
      l_year
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q7.test
    Then I expect the result
      | supp_nation | cust_nation | l_year  | revenue       |
      | TEXT        | TEXT        | INTEGER | DECIMAL       |
      | FRANCE      | GERMANY     | 1995    | 54639732.7336 |
      | FRANCE      | GERMANY     | 1996    | 54633083.3076 |
      | GERMANY     | FRANCE      | 1995    | 52531746.6697 |
      | GERMANY     | FRANCE      | 1996    | 52520549.0224 |
