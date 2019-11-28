Feature: Exists predicate

  Scenario: I execute a query with an exists predicate with a correlated subquery

    Given the table t1
      | a       |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

    And the table t2
      | b       |
      | INTEGER |
      | 1       |
      | 3       |

    When I execute the query
    """
    SELECT * FROM t1 WHERE EXISTS(SELECT * FROM t2 WHERE a = b + 1)
    """

    Then I expect the result set
      | a       |
      | INTEGER |
      | 2       |
