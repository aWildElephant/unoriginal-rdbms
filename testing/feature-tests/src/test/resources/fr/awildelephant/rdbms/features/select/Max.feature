Feature: Max aggregate

  Scenario: I get the max value in an integer column with no null value

    When I execute the query
    """
    SELECT MAX(column1) AS max_value
    FROM (VALUES (1), (2), (0), (-3))
    """

    Then I expect the result set
      | max_value |
      | INTEGER   |
      | 2         |
