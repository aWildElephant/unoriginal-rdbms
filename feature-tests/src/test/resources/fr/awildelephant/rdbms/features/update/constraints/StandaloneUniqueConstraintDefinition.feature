Feature: Standalone unique constraint definition

  Scenario: I create a table with a standalone unique constraint on a single column

    Given I execute the query
    """
    CREATE TABLE test (a INTEGER, UNIQUE(a))
    """

    When I execute the query
    """
    INSERT INTO test VALUES (1)
    """

    And I execute the query
    """
    INSERT INTO test VALUES (1)
    """

    Then I expect an error with the message
    """
    Unique constraint violation
    """

  Scenario: I create a table with a standalone unique constraint on several columns

    Given I execute the query
    """
    CREATE TABLE test (a INTEGER, b INTEGER, UNIQUE(a, b))
    """

    When I execute the query
    """
    INSERT INTO test VALUES (0, 0)
    """

    And I execute the query
    """
    INSERT INTO test VALUES (0, 0)
    """

    Then I expect an error with the message
    """
    Unique constraint violation
    """
