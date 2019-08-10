@todo
Feature: TPC-H Q5

  Background: TPC-H dataset

    Given I load customer scale factor 1
    And I load lineitem scale factor 1
    And I load nation scale factor 1
    And I load orders scale factor 1
    And I load region scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q5

    When I execute the query
    """
    SELECT
      n_name,
      sum(l_extendedprice * (1 - l_discount)) as revenue
    FROM
      customer,
      orders,
      lineitem,
      supplier,
      nation,
      region
     WHERE
      c_custkey = o_custkey
      AND l_orderkey = o_orderkey
      AND l_suppkey = s_suppkey
      AND c_nationkey = s_nationkey
      AND s_nationkey = n_nationkey
      AND n_regionkey = r_regionkey
      AND r_name = 'ASIA'
      AND o_orderdate >= date '1994-01-01'
      AND o_orderdate < date '1994-01-01' + interval '1' year
    GROUP BY
      n_name
    ORDER BY
      revenue desc;
    """

    Then I expect the result
      | n_name    | revenue     |
      | INDONESIA | 55502041.17 |