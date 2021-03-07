@todo # Broken by subquery unnesting refactoring
Feature: TPC-H Q16

  Background: TPC-H dataset

    Given I load part scale factor 1
    And I load partsupp scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q16

    When I execute the query
    """
    SELECT
      p_brand,
      p_type,
      p_size,
      COUNT(DISTINCT ps_suppkey) AS supplier_cnt
    FROM
      partsupp,
      part
    WHERE
      p_partkey = ps_partkey
      AND p_brand <> 'Brand#45'
      AND p_type NOT LIKE 'MEDIUM POLISHED%'
      AND p_size IN (49, 14, 23, 45, 19, 3, 36, 9)
      AND ps_suppkey NOT IN (
        SELECT
          s_suppkey
        FROM
          supplier
        WHERE
          s_comment LIKE '%Customer%Complaints%'
      )
    GROUP BY
      p_brand,
      p_type,
      p_size
    ORDER BY
      supplier_cnt DESC,
      p_brand,
      p_type,
      p_size;
    """

    Then I expect the result described in q16-sf1.csv
