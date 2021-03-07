@todo # Broken by the subquery unnesting refactoring
Feature: TPC-H Q13

  Background: TPC-H dataset

    Given I load customer scale factor 1
    And I load orders scale factor 1

  Scenario: I execute TPC-H Q13

    When I execute the query
    """
    SELECT
      c_count,
      COUNT(*) AS custdist
    FROM (
      SELECT
        c_custkey,
        COUNT(o_orderkey) AS c_count
      FROM
        customer LEFT OUTER JOIN orders ON (
          c_custkey = o_custkey
          AND o_comment NOT LIKE '%special%requests%'
        )
      GROUP BY
        c_custkey
      ) AS c_orders (c_custkey, c_count)
    GROUP BY
      c_count
    ORDER BY
      custdist DESC,
      c_count DESC
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q13.test
    Then I expect the result
      | c_count | custdist |
      | INTEGER | INTEGER  |
      | 0       | 50005    |
      | 9       | 6641     |
      | 10      | 6532     |
      | 11      | 6014     |
      | 8       | 5937     |
      | 12      | 5639     |
      | 13      | 5024     |
      | 19      | 4793     |
      | 7       | 4687     |
      | 17      | 4587     |
      | 18      | 4529     |
      | 20      | 4516     |
      | 15      | 4505     |
      | 14      | 4446     |
      | 16      | 4273     |
      | 21      | 4190     |
      | 22      | 3623     |
      | 6       | 3265     |
      | 23      | 3225     |
      | 24      | 2742     |
      | 25      | 2086     |
      | 5       | 1948     |
      | 26      | 1612     |
      | 27      | 1179     |
      | 4       | 1007     |
      | 28      | 893      |
      | 29      | 593      |
      | 3       | 415      |
      | 30      | 376      |
      | 31      | 226      |
      | 32      | 148      |
      | 2       | 134      |
      | 33      | 75       |
      | 34      | 50       |
      | 35      | 37       |
      | 1       | 17       |
      | 36      | 14       |
      | 38      | 5        |
      | 37      | 5        |
      | 40      | 4        |
      | 41      | 2        |
      | 39      | 1        |
