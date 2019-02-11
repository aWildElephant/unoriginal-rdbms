Feature: Values

  Scenario: I create an explicit table using values

    When I execute the query
    """
    VALUES (1, 2, 3), (2 + 2, 5, 6)
    """

    Then I expect the result set
    | column1 | column2 | column3 |
    | INTEGER | INTEGER | INTEGER |
    | 1       | 2       | 3       |
    | 4       | 5       | 6       |
