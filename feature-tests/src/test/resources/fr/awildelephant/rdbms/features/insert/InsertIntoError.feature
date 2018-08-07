Feature: Insert Into errors

  Scenario: I try to insert in a table that doesn't exist

    When I execute the query
    """
    INSERT INTO UnknownTable VALUES (1);
    """

    Then I expect an error with the message
    """
    Table not found: unknowntable
    """
