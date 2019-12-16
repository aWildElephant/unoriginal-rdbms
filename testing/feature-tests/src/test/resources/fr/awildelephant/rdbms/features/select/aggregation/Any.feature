Feature: Any aggregate

  Scenario: I query if any of the values is true

    When I execute the query
    """
    SELECT column1 AS group_value, any(column2) as any_value
    FROM (VALUES (1, false), (1, true), (1, null), (2, false), (2, null))
    GROUP BY column1
    """

    Then I expect the result set
      | group_value | any_value |
      | INTEGER     | BOOLEAN   |
      | 1           | true      |
      | 2           | false     |

  Scenario: Any should return null if there is no input

    Given the table test
      | b       |
      | BOOLEAN |

    When I execute the query
    """
    SELECT ANY(b) FROM test
    """

    Then I expect the result set
      | any(b)  |
      | BOOLEAN |
      | null    |
