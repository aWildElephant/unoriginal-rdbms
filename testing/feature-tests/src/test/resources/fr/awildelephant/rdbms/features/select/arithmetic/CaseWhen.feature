Feature: Case when expression

  Scenario: I execute a query with a case when expression

    When I execute the query
    """
    SELECT CASE WHEN age >= 18 THEN 'can drink' ELSE 'cannot drink' END AS result
    FROM (
      SELECT column1 AS age FROM (VALUES (17), (18), (19), (null))
    );
    """

    Then I expect the result set
      | result       |
      | TEXT         |
      | cannot drink |
      | can drink    |
      | can drink    |
      | cannot drink |
