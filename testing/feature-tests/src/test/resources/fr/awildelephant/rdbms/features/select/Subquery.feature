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

    Then I expect the result set
      | sum(x)  |
      | INTEGER |
      | 10      |

  @todo
  Scenario: I select employees with a salary higher than the average of their department using a correlated subquery

    Given the table Employee
      | name     | department | salary  |
      | TEXT     | TEXT       | DECIMAL |
      | Richard  | IT         | 50000   |
      | Peterson | IT         | 40000   |
      | Sophia   | IT         | 33000   |
      | Mickael  | Sales      | 55000   |
      | Peter    | Sales      | 45000   |

    When I execute the query
    """
    SELECT *
    FROM employee e1
    WHERE salary > (SELECT AVG(salary) FROM employee e2 WHERE e1.department = e2.department)
    """

    Then I expect the result set
      | name    | department | salary  |
      | TEXT    | TEXT       | DECIMAL |
      | Richard | IT         | 50000   |
      | Mickael | Sales      | 55000   |
