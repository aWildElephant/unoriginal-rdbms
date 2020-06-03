Feature: In predicate

  Scenario: I execute a query with the in predicate

    When I execute the query
      """
      SELECT column1 AS result
      FROM (VALUES
        ('a'),
        ('b'),
        ('c'),
        (null)
      )
      WHERE column1 IN ('a', 'b');
      """

    Then I expect the result set
      | result |
      | TEXT   |
      | a      |
      | b      |
