@todo
Feature: Scalar subquery

  Scenario: I execute a query with a scalar subquery in the where clause

    Given the table table1
      | a       |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

    And the table table2
      | b       |
      | INTEGER |
      | 2       |
      | 3       |

    When I execute the query
    """
    SELECT * FROM table1 WHERE a = (SELECT min(b) FROM table2)
    """

    Then I expect the result set
      | a       |
      | INTEGER |
      | 2       |
