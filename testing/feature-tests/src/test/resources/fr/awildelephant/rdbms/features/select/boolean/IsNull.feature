Feature: Is null predicate

  Scenario: I execute a query with the is null predicate in the where clause

    When I execute the query
    """
    SELECT column1 IS NULL AS result
    FROM (VALUES
      (true),
      (false),
      (null)
    )
    """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | false   |
      | false   |
      | true    |

  Scenario: I execute a query with the is not null predicate in the where clause

    When I execute the query
    """
    SELECT column1 IS NOT NULL AS result
    FROM (VALUES
      (true),
      (false),
      (null)
    );
    """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | true    |
      | false   |
