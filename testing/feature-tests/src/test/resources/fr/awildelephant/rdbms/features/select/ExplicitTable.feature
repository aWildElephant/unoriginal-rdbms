Feature: Explicit table

  Scenario: I query the whole table with just its name

    Given the table employee
      | name    | age     |
      | TEXT    | INTEGER |
      | Nicolas | 28      |
      | Etienne | 26      |

    When I execute the query
      """
      TABLE employee
      """

    Then I expect the result set
      | name    | age     |
      | TEXT    | INTEGER |
      | Nicolas | 28      |
      | Etienne | 26      |
