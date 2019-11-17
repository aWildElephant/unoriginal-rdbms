Feature: Count

  Background: a table with some data

    Given the table test
      | a       |
      | INTEGER |
      | 1       |
      | 2       |
      | 2       |
      | null    |

  Scenario: I count the values of a column

    When I execute the query
    """
    SELECT COUNT(a) FROM test
    """

    Then I expect the result set
      | count(a) |
      | INTEGER  |
      | 3        |

  Scenario: I count the distinct values of a column

    When I execute the query
    """
    SELECT COUNT(DISTINCT a) FROM test
    """

    Then I expect the result set
      | count(distinct a) |
      | INTEGER           |
      | 2                 |
