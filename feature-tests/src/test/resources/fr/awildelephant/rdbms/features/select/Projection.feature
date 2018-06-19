Feature: Projection

  Scenario: I project on a proper subset of the columns of the table

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 1       | 2       |

    When I execute the query
    """
    SELECT b FROM test
    """

    Then I expect the result set
      | b       |
      | INTEGER |
      | 2       |