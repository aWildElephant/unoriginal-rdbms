Feature: TPC-H Q6

  Background: TPC-H dataset

    Given I load lineitem scale factor 1

  Scenario: I execute TPC-H Q6

    When I execute the query
    """
    SELECT
      sum(l_extendedprice * l_discount) AS revenue
    FROM
      lineitem
    WHERE
      l_shipdate >= date '1994-01-01'
      AND l_shipdate < date '1994-01-01' + interval '1' year
      AND l_discount between 0.06 - 0.01 and 0.06 + 0.01
      AND l_quantity < 24;
    """

    Then I expect the result
      | revenue      |
      | DECIMAL      |
      | 123141078.23 |
