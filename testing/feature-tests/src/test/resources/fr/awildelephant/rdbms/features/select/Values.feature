Feature: Values

  Scenario: I create an table using the values constructor

    When I execute the query
      """
      VALUES (1, 2, 3), (2 + 2, 5, 6)
      """

    Then I expect the result set
      | column1 | column2 | column3 |
      | INTEGER | INTEGER | INTEGER |
      | 1       | 2       | 3       |
      | 4       | 5       | 6       |

  Scenario: I try to use the values constructor with rows that don't have the same number of columns

    When I execute the query
      """
      VALUES (1), (2, 3)
      """

    Then I expect an error with the message
      """
      Column count does not match
      """

  Scenario: I construct a table with no column using the values constructor

    When I execute the query
      """
      VALUES (), (), ()
      """

    Then I expect a result set with no column and 3 rows