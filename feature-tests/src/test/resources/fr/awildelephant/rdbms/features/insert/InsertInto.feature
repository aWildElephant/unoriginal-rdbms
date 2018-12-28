Feature: Insert into

  Background: a table with two columns of different types
    Given the table people
      | id      | name |
      | INTEGER | TEXT |

  Scenario: I insert a single row in the table

    When I execute the query
    """
    INSERT INTO people VALUES (1, 'bernard');
    """

    Then table people should be
      | id      | name    |
      | INTEGER | TEXT    |
      | 1       | bernard |

  Scenario: I insert two rows in the table

    When I execute the query
    """
    INSERT INTO people VALUES (1, 'bernard'), (2, 'nicolas');
    """

    Then table people should be
      | id      | name    |
      | INTEGER | TEXT    |
      | 1       | bernard |
      | 2       | nicolas |
