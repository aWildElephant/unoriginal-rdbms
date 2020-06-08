@todo
Feature: TPC-H Q17

  Background: TPC-H dataset

    Given I load lineitem scale factor 1
    And I load part scale factor 1

  Scenario: I execute TPC-H Q17

    When I execute the query
    """
    SELECT
      SUM(l_extendedprice) / 7.0 AS avg_yearly
    FROM
      lineitem,
      part
    WHERE
      p_partkey = l_partkey
      AND p_brand = 'Brand#23'
      AND p_container = 'MED BOX'
      AND l_quantity < (
        SELECT
          0.2 * AVG(l_quantity)
        FROM
          lineitem
        WHERE
          l_partkey = p_partkey
      );
    """

    Then I expect the result
      | avg_yearly |
      | DECIMAL    |
      | 348406.05  |
