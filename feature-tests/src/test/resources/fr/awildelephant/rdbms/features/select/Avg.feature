Feature: Sum

  Background: a table with some data

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 1       | 1       |
      | 1       | 2       |
      | 2       | 8       |
      | 2       | 4       |
      | 3       | 8       |
      | 3       | null    |
      | 2       | 6       |

  Scenario: I avg after grouping

    When I execute the query
      """
      SELECT A, AVG(b) FROM test GROUP BY A
      """

    Then I expect the result set
      | a       | avg(b)  |
      | INTEGER | DECIMAL |
      | 1       | 1.5     |
      | 2       | 6       |
      | 3       | 8       |
