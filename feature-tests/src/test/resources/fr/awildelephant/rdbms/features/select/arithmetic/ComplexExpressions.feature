Feature: Complex expressions

  Scenario: I execute complex arithmetic expressions

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 0       | 2       |
      | 3       | 1       |

    When I execute the query
    """
    SELECT 1 + (a + 1)/(b * 2.0) FROM test
    """

    Then I expect the result set
      | 1 + (a + 1) / (b * 2.0) |
      | DECIMAL                 |
      | 1.25                    |
      | 3                       |