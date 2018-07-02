Feature: Standalone unique constraint definition

  Scenario: I create a table with a standalone unique constraint

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
    Unique constraint violation: column a already contains value 1
    """