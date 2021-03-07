@todo # Broken by subquery unnesting refactoring
Feature: TPC-H Q4

  Background: TPC-H dataset

    Given I load lineitem scale factor 1
    And I load orders scale factor 1

  Scenario: I execute TPC-H Q4

    When I execute the query
    """
    SELECT
      o_orderpriority,
      COUNT(*) AS order_count
    FROM
      orders
    WHERE
      o_orderdate >= DATE '1993-07-01'
      AND o_orderdate < DATE '1993-07-01' + INTERVAL '3' MONTH
      AND EXISTS (
        SELECT *
        FROM
          lineitem
        WHERE
          l_orderkey = o_orderkey
          AND l_commitdate < l_receiptdate
      )
    GROUP BY
      o_orderpriority
    ORDER BY
      o_orderpriority;
    """

    Then I expect the result
      | o_orderpriority | order_count |
      | TEXT            | INTEGER     |
      | 1-URGENT        | 10594       |
      | 2-HIGH          | 10476       |
      | 3-MEDIUM        | 10410       |
      | 4-NOT SPECIFIED | 10556       |
      | 5-LOW           | 10487       |
