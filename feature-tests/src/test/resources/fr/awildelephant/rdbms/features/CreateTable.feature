Feature: Create table

  Scenario: I create an empty table with a single integer column

    When I execute the query
    """
    CREATE TABLE z (y INTEGER)
    """

    Then table z should be
      | y       |
      | INTEGER |