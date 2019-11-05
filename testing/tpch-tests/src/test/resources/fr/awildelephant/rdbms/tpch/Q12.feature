@todo
Feature: TPC-H Q12

  Background: TPC-H dataset

    Given I load lineitem scale factor 1
    And I load orders scale factor 1

  Scenario: I execute TPC-H Q12

    When I execute the query
    """
    SELECT
      l_shipmode,
      SUM (
        CASE WHEN
          o_orderpriority = '1-URGENT'
          OR o_orderpriority = '2-HIGH'
        THEN 1
        ELSE 0
        END) AS high_line_count,
      SUM (
        CASE WHEN
          o_orderpriority <> '1-URGENT'
          AND o_orderpriority <> '2-HIGH'
        THEN 1
        ELSE 0
        END) AS low_line_count
    FROM
      orders,
      lineitem
    WHERE
      o_orderkey = l_orderkey
      AND l_shipmode IN ('MAIL', 'SHIP')
      AND l_commitdate < l_receiptdate
      AND l_shipdate < l_commitdate
      AND l_receiptdate >= DATE '1994-01-01'
      AND l_receiptdate < DATE '1994-01-01' + INTERVAL '1' YEAR
    GROUP BY
      l_shipmode
    ORDER BY
      l_shipmode
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q12.test
    Then I expect the result
      | l_shipmode | high_line_count | low_line_count |
      | TEXT       | INTEGER         | INTEGER        |
      | MAIL       | 6202            | 9324           |
      | SHIP       | 6200            | 9262           |
