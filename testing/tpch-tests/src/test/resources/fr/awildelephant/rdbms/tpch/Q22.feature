Feature: TPC-H Q22

  Background: TPC-H dataset

    Given I load customer scale factor 1
    And I load orders scale factor 1

  Scenario: I execute TPC-H Q22

    When I execute the query
    """
    SELECT
      cntrycode,
      COUNT(*) AS numcust,
      SUM(c_acctbal) AS totacctbal
    FROM (
      SELECT
        SUBSTRING(c_phone FROM 1 FOR 2) AS cntrycode,
        c_acctbal
      FROM
        customer
      WHERE
        SUBSTRING(c_phone FROM 1 FOR 2) IN ('13','31','23','29','30','18','17')
        AND c_acctbal > (
          SELECT
            AVG(c_acctbal)
          FROM
            customer
          WHERE
          c_acctbal > 0.00
          AND SUBSTRING(c_phone FROM 1 FOR 2) IN ('13','31','23','29','30','18','17')
        )
        AND NOT EXISTS (
          SELECT
            *
          FROM
            orders
          WHERE
            o_custkey = c_custkey
        )
    ) AS custsale
    GROUP BY
      cntrycode
    ORDER BY
      cntrycode;
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q22.test
    Then I expect the result
      | cntrycode | numcust | totacctbal |
      | TEXT      | INTEGER | DECIMAL    |
      | 13        | 888     | 6737713.99 |
      | 17        | 861     | 6460573.72 |
      | 18        | 964     | 7236687.40 |
      | 23        | 892     | 6701457.95 |
      | 29        | 948     | 7158866.63 |
      | 30        | 909     | 6808436.13 |
      | 31        | 922     | 6806670.18 |
