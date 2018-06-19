Feature: Select errors

  Scenario: I select a column that doesn't exist in the table

    Given the table z
      | y       |
      | INTEGER |
      | 1       |

    When I execute the query
      """
      SELECT x FROM z
      """

    Then I expect an error with the message
      """
      Column not found: x
      """

  Scenario: I query a table that doesn't exist

    When I execute the query
      """
      SELECT * FROM UnknownTable
      """

    Then I expect an error with the message
      """
      Table not found: unknowntable
      """
