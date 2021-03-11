Feature: TPC-H Q15

  Background: TPC-H dataset

    Given I load lineitem scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q15

    When I execute the query
    """
    CREATE VIEW revenue_view (supplier_no, total_revenue) AS
      SELECT
        l_suppkey,
        SUM(l_extendedprice * (1 - l_discount))
      FROM
        lineitem
      WHERE
        l_shipdate >= DATE '1996-01-01'
        AND l_shipdate < DATE '1996-01-01' + INTERVAL '3' MONTH
      GROUP BY
        l_suppkey
    """

    And I execute the query
    """
    SELECT
      s_suppkey,
      s_name,
      s_address,
      s_phone,
      total_revenue
    FROM
      supplier,
      revenue_view
    WHERE
      s_suppkey = supplier_no
      AND total_revenue = (SELECT MAX(total_revenue) FROM revenue_view)
    ORDER BY
      s_suppkey
    """

    Then I expect the result
      | s_suppkey | s_name             | s_address         | s_phone         | total_revenue |
      | INTEGER   | TEXT               | TEXT              | TEXT            | DECIMAL       |
      | 8449      | Supplier#000008449 | Wp34zim9qYFbVctdW | 20-469-856-8873 | 1772627.2087  |
