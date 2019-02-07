Feature: Sum

  Background: a table with some data

    Given the table test
      | c       |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

  Scenario: I sum the column

    When I execute the query
    """
    SELECT SUM(c) FROM test
    """

    Then I expect the result set
    | c       |
    | INTEGER |
    | 6       |
