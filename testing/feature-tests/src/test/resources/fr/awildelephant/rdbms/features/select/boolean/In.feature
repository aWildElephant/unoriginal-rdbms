Feature: In predicate

  Scenario: I execute a query with the in predicate

    When I execute the query
      """
      SELECT column1 IN ('a', 'b') AS result FROM (VALUES
        ('a'),
        ('b'),
        ('c'),
        (null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | true    |
      | false   |
      | null    |
