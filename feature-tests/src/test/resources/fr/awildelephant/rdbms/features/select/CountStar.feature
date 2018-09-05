Feature: Count star

  Background: a table with some data

    Given the table test
      | c       |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

  Scenario: I count the number of rows in a table

    When I execute the query
    """
    SELECT COUNT(*) FROM test
    """

    Then I expect the result set
      | COUNT(*) |
      | INTEGER  |
      | 3        |

  Scenario: I output several count star

    When I execute the query
    """
    SELECT COUNT(*) AS countA, COUNT(*) AS countB FROM test
    """

    Then I expect the result set
      | countA  | countB  |
      | INTEGER | INTEGER |
      | 3       | 3       |

    @debug
  Scenario: I subtract one to the number of rows in the table

    When I execute the query
    """
    SELECT COUNT(*) - 1 FROM test
    """

    Then I expect the result set
      | count(*) - 1 |
      | INTEGER      |
      | 2            |