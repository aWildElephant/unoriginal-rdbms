Feature: Coalesce

  Scenario: I use the coalesce function on text type

    Given the table t1
      | column1 | column2 |
      | TEXT    | TEXT    |
      | hello   | there   |
      | null    | there   |
      | null    | null    |

    When I execute the query
    """
    SELECT COALESCE(column1, column2, 'general kenobi') AS coalesce_result FROM t1
    """

    Then I expect the result set
      | coalesce_result |
      | TEXT            |
      | hello           |
      | there           |
      | general kenobi  |