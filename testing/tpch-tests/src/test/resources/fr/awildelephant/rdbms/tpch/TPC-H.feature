Feature: TPC-H queries

  Background: TPC-H dataset

    Given I create the TPC-H lineitem table
    And I load lineitem scale factor 1 data

  Scenario: I execute TPC-H Q1

    When I execute the query
    """
    SELECT
      l_returnflag,
      l_linestatus,
      SUM(l_quantity) AS sum_qty,
      SUM(l_extendedprice) as sum_base_price,
      SUM(l_extendedprice * (1 - l_discount)) AS sum_disc_price,
      SUM(l_extendedprice * (1 - l_discount) * (1 + l_tax)) AS sum_charge,
      AVG(l_quantity) AS avg_qty,
      AVG(l_extendedprice) AS avg_price,
      AVG(l_discount) AS avg_disc,
      COUNT(*) AS count_order
    FROM lineitem
    WHERE l_shipdate <= date('1998-12-01') - INTERVAL '90' DAY (3)
    GROUP BY l_returnflag, l_linestatus
    ORDER BY l_returnflag, l_linestatus;
    """

    Then I expect the result
      | l_returnflag | l_linestatus | sum_qty     | sum_base_price  | sum_disc_price  | sum_charge      | avg_qty | avg_price | avg_disc | count_order |
      | TEXT         | TEXT         | DECIMAL     | DECIMAL         | DECIMAL         | DECIMAL         | DECIMAL | DECIMAL   | DECIMAL  | INTEGER     |
      | A            | F            | 37734107.00 | 56586554400.73  | 53758257134.87  | 55909065222.83  | 25.52   | 38273.13  | 0.05     | 1478493     |
      | N            | F            | 991417.00   | 1487504710.38   | 1413082168.05   | 1469649223.19   | 25.52   | 38284.47  | 0.05     | 38854       |
      | N            | O            | 74476040.00 | 111701729697.74 | 106118230307.61 | 110367043872.50 | 25.50   | 38249.12  | 0.05     | 2920374     |
      | R            | F            | 37719753.00 | 56568041380.90  | 53741292684.60  | 55889619119.83  | 25.51   | 38250.85  | 0.05     | 1478870     |
