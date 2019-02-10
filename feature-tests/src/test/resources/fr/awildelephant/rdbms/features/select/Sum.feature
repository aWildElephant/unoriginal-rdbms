Feature: Sum

  Background: a table with some data

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 1       | 1       |
      | 2       | 4       |
      | 3       | 8       |

  Scenario: I sum the column

    When I execute the query
    """
    SELECT SUM(a) FROM test
    """

    Then I expect the result set
    | sum(a)  |
    | INTEGER |
    | 6       |

  Scenario: I sum a map on several columns

    When I execute the query
    """
    SELECT SUM(a*b) FROM test
    """

    Then I expect the result set
    | sum(a * b)   |
    | INTEGER      |
    | 33           |
