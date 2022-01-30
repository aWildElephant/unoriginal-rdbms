# TODO: cucumber.publish.quiet=true in cucumber.properties
Feature: Explain

  Scenario: I explain a simple values query

    When I execute the query
    """
    EXPLAIN VALUES (1)
    """

    Then I expect the result set
      | explain |
      | TEXT    |
      | Matrix  |

  Scenario: I explain a join between two tables

    Given the table t1
      | c1      | c2   |
      | INTEGER | TEXT |

    And the table t2
      | c1      | c2   |
      | INTEGER | TEXT |

    When I execute the query
    """
    EXPLAIN SELECT * FROM t1 INNER JOIN t2 ON t1.c1 = t2.c1 WHERE t1.c2 = 'test'
    """

    Then I expect the result set
      | explain                  |
      | TEXT                     |
      | Inner join inputs:[1, 3] |
      | Filter inputs:[2]        |
      | Base table [t1]          |
      | Base table [t2]          |