Feature: I execute somewhat complex queries with arithmetic formulas

  Scenario: I execute a query with two arithmetic formulas

    Given the table test
      | a       |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

    When I execute the query
      """
      SELECT a + 1, a - 1 FROM test
      """

    Then I expect the result set
      | a + 1   | a - 1   |
      | INTEGER | INTEGER |
      | 2       | 0       |
      | 3       | 1       |
      | 4       | 2       |