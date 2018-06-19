Feature: Select star

  Scenario: I select all the columns from a table

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 4       | 2       |
      | 6       | 9       |

    When I execute the query
    """
    SELECT * FROM test
    """

    Then I expect the result set
      | a       | b       |
      | INTEGER | INTEGER |
      | 4       | 2       |
      | 6       | 9       |