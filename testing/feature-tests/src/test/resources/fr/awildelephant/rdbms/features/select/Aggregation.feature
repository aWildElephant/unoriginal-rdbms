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