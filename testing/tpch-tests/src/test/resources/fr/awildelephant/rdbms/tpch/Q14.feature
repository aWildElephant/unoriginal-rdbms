Feature: TPC-H Q14

  Background: TPC-H dataset

    Given I load lineitem scale factor 1
    And I load part scale factor 1

  Scenario: I execute TPC-H Q14

    When I execute the query
    """
    SELECT
      100.00 * SUM(CASE WHEN p_type LIKE 'PROMO%'
        THEN l_extendedprice * (1 - l_discount)
        ELSE 0
        END) / SUM(l_extendedprice * (1 - l_discount)) AS promo_revenue
    FROM
      lineitem,
      part
    WHERE
      l_partkey = p_partkey
      AND l_shipdate >= DATE '1995-09-01'
      AND l_shipdate < DATE '1995-09-01' + INTERVAL '1' MONTH
    """

    Then I expect the result
      | promo_revenue |
      | DECIMAL       |
      | 16.38         |
