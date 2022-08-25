Feature: Bigint

  Scenario: I create a table with a single bigint column

    When I execute the query
    """
    CREATE TABLE test (a BIGINT);
    """

    Then table test should be
      | a      |
      | BIGINT |