Feature: I group by some columns

  Background: a table and some data

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 0       | 0       |
      | 0       | 1       |
      | 1       | 0       |
      | 1       | 1       |

  Scenario: I count the rows grouped by a

    When I execute the query
      """
      SELECT COUNT(*) FROM test GROUP BY a
      """

    Then I expect the result set
      | count(*) |
      | INTEGER  |
      | 2        |
      | 2        |
