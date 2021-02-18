@todo # Internal error "column not found"
Feature: TPC-H Q21

  Background: TPC-H dataset

    Given I load lineitem scale factor 1
    And I load nation scale factor 1
    And I load orders scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q21

    When I execute the query
    """
    SELECT
      s_name,
      COUNT(*) AS numwait
    FROM
      supplier,
      lineitem l1,
      orders,
      nation
    WHERE
      s_suppkey = l1.l_suppkey
      AND o_orderkey = l1.l_orderkey
      AND o_orderstatus = 'F'
      AND l1.l_receiptdate > l1.l_commitdate
      AND EXISTS (
        SELECT
          *
        FROM
          lineitem l2
        WHERE
          l2.l_orderkey = l1.l_orderkey
          AND l2.l_suppkey <> l1.l_suppkey
      )
      AND NOT EXISTS (
        SELECT
          *
        FROM
          lineitem l3
        WHERE
          l3.l_orderkey = l1.l_orderkey
          AND l3.l_suppkey <> l1.l_suppkey
          AND l3.l_receiptdate > l3.l_commitdate
      )
      AND s_nationkey = n_nationkey
      AND n_name = 'SAUDI ARABIA'
      GROUP BY
        s_name
      ORDER BY
        numwait DESC,
        s_name;
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q21.test
    Then I expect the result
      | s_name             | numwait |
      | TEXT               | INTEGER |
      | Supplier#000002829 | 20      |
      | Supplier#000005808 | 18      |
      | Supplier#000000262 | 17      |
      | Supplier#000000496 | 17      |
      | Supplier#000002160 | 17      |
      | Supplier#000002301 | 17      |
      | Supplier#000002540 | 17      |
      | Supplier#000003063 | 17      |
      | Supplier#000005178 | 17      |
      | Supplier#000008331 | 17      |
      | Supplier#000002005 | 16      |
      | Supplier#000002095 | 16      |
      | Supplier#000005799 | 16      |
      | Supplier#000005842 | 16      |
      | Supplier#000006450 | 16      |
      | Supplier#000006939 | 16      |
      | Supplier#000009200 | 16      |
      | Supplier#000009727 | 16      |
      | Supplier#000000486 | 15      |
      | Supplier#000000565 | 15      |
      | Supplier#000001046 | 15      |
      | Supplier#000001047 | 15      |
      | Supplier#000001161 | 15      |
      | Supplier#000001336 | 15      |
      | Supplier#000001435 | 15      |
      | Supplier#000003075 | 15      |
      | Supplier#000003335 | 15      |
      | Supplier#000005649 | 15      |
      | Supplier#000006027 | 15      |
      | Supplier#000006795 | 15      |
      | Supplier#000006800 | 15      |
      | Supplier#000006824 | 15      |
      | Supplier#000007131 | 15      |
      | Supplier#000007382 | 15      |
      | Supplier#000008913 | 15      |
      | Supplier#000009787 | 15      |
      | Supplier#000000633 | 14      |
      | Supplier#000001960 | 14      |
      | Supplier#000002323 | 14      |
      | Supplier#000002490 | 14      |
      | Supplier#000002993 | 14      |
      | Supplier#000003101 | 14      |
      | Supplier#000004489 | 14      |
      | Supplier#000005435 | 14      |
      | Supplier#000005583 | 14      |
      | Supplier#000005774 | 14      |
      | Supplier#000007579 | 14      |
      | Supplier#000008180 | 14      |
      | Supplier#000008695 | 14      |
      | Supplier#000009224 | 14      |
      | Supplier#000000357 | 13      |
      | Supplier#000000436 | 13      |
      | Supplier#000000610 | 13      |
      | Supplier#000000788 | 13      |
      | Supplier#000000889 | 13      |
      | Supplier#000001062 | 13      |
      | Supplier#000001498 | 13      |
      | Supplier#000002056 | 13      |
      | Supplier#000002312 | 13      |
      | Supplier#000002344 | 13      |
      | Supplier#000002596 | 13      |
      | Supplier#000002615 | 13      |
      | Supplier#000002978 | 13      |
      | Supplier#000003048 | 13      |
      | Supplier#000003234 | 13      |
      | Supplier#000003727 | 13      |
      | Supplier#000003806 | 13      |
      | Supplier#000004472 | 13      |
      | Supplier#000005236 | 13      |
      | Supplier#000005906 | 13      |
      | Supplier#000006241 | 13      |
      | Supplier#000006326 | 13      |
      | Supplier#000006384 | 13      |
      | Supplier#000006394 | 13      |
      | Supplier#000006624 | 13      |
      | Supplier#000006629 | 13      |
      | Supplier#000006682 | 13      |
      | Supplier#000006737 | 13      |
      | Supplier#000006825 | 13      |
      | Supplier#000007021 | 13      |
      | Supplier#000007417 | 13      |
      | Supplier#000007497 | 13      |
      | Supplier#000007602 | 13      |
      | Supplier#000008134 | 13      |
      | Supplier#000008234 | 13      |
      | Supplier#000009435 | 13      |
      | Supplier#000009436 | 13      |
      | Supplier#000009564 | 13      |
      | Supplier#000009896 | 13      |
      | Supplier#000000379 | 12      |
      | Supplier#000000673 | 12      |
      | Supplier#000000762 | 12      |
      | Supplier#000000811 | 12      |
      | Supplier#000000821 | 12      |
      | Supplier#000001337 | 12      |
      | Supplier#000001916 | 12      |
      | Supplier#000001925 | 12      |
      | Supplier#000002039 | 12      |
      | Supplier#000002357 | 12      |
      | Supplier#000002483 | 12      |
