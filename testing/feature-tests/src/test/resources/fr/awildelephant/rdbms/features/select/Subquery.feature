Feature: Subquery

  Scenario: I execute a subquery with an table constructor in a subquery

    When I execute the query
      """
      SELECT column1 FROM (VALUES (1), (2), (3))
      """

    Then I expect the result set
      | column1 |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

  Scenario: I execute a subquery with a nested select

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 1       | 8       |
      | 0       | 4       |
      | 1       | 2       |

    When I execute the query
      """
      SELECT SUM(x) FROM (SELECT a*b AS x FROM test)
      """
