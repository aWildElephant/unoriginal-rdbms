Feature: Unique constraint error

  Scenario: I try to add an unique constraint on a column that does not exist

    When I execute the query
    """
    CREATE TABLE test (a INTEGER, UNIQUE(b))
    """

    Then I expect an error with the message
    """
    Column not found: b
    """
