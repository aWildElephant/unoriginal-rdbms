Feature: Limit

  Scenario: I limit the result of a query

    When I execute the query
    """
    SELECT *
    FROM (VALUES (1), (2))
    LIMIT 1
    """

    Then I expect the result set
      | column1 |
      | INTEGER |
      | 1       |
