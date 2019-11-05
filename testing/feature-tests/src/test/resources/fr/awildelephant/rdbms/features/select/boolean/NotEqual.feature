Feature: Not equal comparison

  Scenario: I test the equality of integer values

    When I execute the query
      """
      SELECT column1 <> column2 AS result FROM (VALUES
        (1, 0),
        (0, 0),
        (1, null),
        (null, 1),
        (null, null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | false   |
      | null    |
      | null    |
      | null    |

  Scenario: I test the equality of string values

    When I execute the query
      """
      SELECT column1 <> column2 AS result FROM (VALUES
        ('hello', 'bonjour'),
        ('bonjour', 'bonjour'),
        ('hello', null),
        (null, 'hello'),
        (null, null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | false   |
      | null    |
      | null    |
      | null    |
