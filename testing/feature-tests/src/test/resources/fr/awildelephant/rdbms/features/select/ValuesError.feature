Feature: Values error

  Scenario: I reference an unknown column in a table constructor

    When I execute the query
    """
    VALUES (zoinks)
    """

    Then I expect an error with the message
    """
    Column not found: zoinks
    """
