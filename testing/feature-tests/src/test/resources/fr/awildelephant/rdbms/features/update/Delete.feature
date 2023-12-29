Feature: Delete

  Background: a table

    Given the table t1
      | c1      |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |
      | 4       |
      | 5       |

  @todo
  Scenario: I delete one row from a table

    When I execute the query
      """
      DELETE FROM t1 WHERE c1 = 2;
      """

    Then table t1 should be
      | c1      |
      | INTEGER |
      | 1       |
      | 3       |
      | 4       |
      | 5       |
