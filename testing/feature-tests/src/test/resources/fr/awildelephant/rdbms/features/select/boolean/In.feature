Feature: In predicate

  Scenario: I execute a query with the in predicate in the where clause

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

  Scenario: I execute a query with the in predicate in the output columns

    When I execute the query
    """
    SELECT column1 IN ('a', 'b') AS result
    FROM (VALUES
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
      | unknown |
