Feature: Exists predicate

  @todo # Broken by subquery unnesting refactoring
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

  @todo
  Scenario: I execute a query with a not exists predicate with a correlated subquery

    Given the table t1
      | a       |
      | INTEGER |
      | 1       |
      | 2       |

    And the table t2
      | b       |
      | INTEGER |
      | 2       |

    When I execute the query
      """
      SELECT * FROM t1 WHERE NOT EXISTS (SELECT * FROM t2 WHERE a = b)
      """

    Then I expect the result set
      | a       |
      | INTEGER |
      | 1       |
