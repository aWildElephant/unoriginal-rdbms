Feature: Aggregation

  Scenario: I execute a query with several aggregates

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 1       | 2       |
      | 2       | 4       |

    When I execute the query
      """
      SELECT sum(a), sum(b) FROM test
      """

    Then I expect the result set
      | sum(a)  | sum(b)  |
      | DECIMAL | DECIMAL |
      | 3       | 6       |

  Scenario: I execute a query with an aggregate and a breakdown over an empty table

    Given the table test
      | a       |
      | INTEGER |

    When I execute the query
    """
    SELECT a, count(*) FROM test GROUP BY a
    """

    Then I expect the result set
      | a       | count(*) |
      | INTEGER | INTEGER  |
