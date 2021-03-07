Feature: Scalar subquery

  @todo # ça morche pu
  Scenario: I execute a query with a scalar subquery that has too many columns

    Given the table test
      | a       |
      | INTEGER |

    When I execute the query
    """
    SELECT * FROM test WHERE a = (SELECT 1, 2 FROM test)
    """

    Then I expect an error with the message
    """
    Scalar subquery cannot have more than one column
    """

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
