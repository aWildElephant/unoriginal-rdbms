Feature: Truncate

  Background: a table

    Given the table t1
      | c1      |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

  @todo
  Scenario: I insert a single row in the table

    When I execute the query
      """
      TRUNCATE t1;
      """

    Then table t1 should be
      | c1      |
      | INTEGER |
