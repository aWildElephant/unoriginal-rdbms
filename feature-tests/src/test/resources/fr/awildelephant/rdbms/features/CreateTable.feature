Feature: Create table

  Scenario: I create an empty table with a single integer column

    When I execute the update
    """
    CREATE TABLE z (y INTEGER);
    """

    And I execute the query
    """
    SELECT y FROM z
    """

    Then I expect the result set
      | y       |
      | INTEGER |