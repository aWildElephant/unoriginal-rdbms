Feature: Insert into

  Scenario: I insert a single integer value into a table

    Given I execute the query
    """
    CREATE TABLE z (y INTEGER)
    """

    And I execute the query
    """
    INSERT INTO z VALUES (1)
    """

    Then table z should be
      | y       |
      | INTEGER |
      | 1       |

  Scenario: I insert an integer and a string into a table

    Given I execute the query
    """
    CREATE TABLE people (id INTEGER, name TEXT)
    """

    And I execute the query
    """
    INSERT INTO people VALUES (1, 'bernard');
    """

    Then table people should be
      | id      | name    |
      | INTEGER | TEXT    |
      | 1       | bernard |