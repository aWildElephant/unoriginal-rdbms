Feature: Create view

  Scenario: I create a view and query it

    Given the table integers
      | x       |
      | INTEGER |
      | -1      |
      | 0       |
      | 1       |
      | 2       |
      | 3       |

    When I execute the query
    """
    CREATE VIEW squares (x_squared) AS
      SELECT x * x FROM integers
    """

    And I execute the query
    """
    SELECT * FROM squares
    """

    Then I expect the result set
      | x_squared |
      | INTEGER   |
      | 1         |
      | 0         |
      | 1         |
      | 4         |
      | 9         |
