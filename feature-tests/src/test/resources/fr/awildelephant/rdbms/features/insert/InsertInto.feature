Feature: Insert into

  Scenario: I insert an integer and a string into a table

    Given the table people
      | id      | name |
      | INTEGER | TEXT |

    And I execute the query
    """
    INSERT INTO people VALUES (1, 'bernard');
    """

    Then table people should be
      | id      | name    |
      | INTEGER | TEXT    |
      | 1       | bernard |

