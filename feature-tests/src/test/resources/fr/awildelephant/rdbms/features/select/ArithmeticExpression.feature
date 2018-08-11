@todo
Feature: Arithmetic expression

  Background: a table and some data

    Given the table test
      | value   |
      | INTEGER |
      | null    |
      | 1       |
      | 2       |
      | 3       |
      | 5       |

  Scenario: I add one to a column

    When I execute the query
    """
    SELECT value + 1 FROM test
    """

    Then I expect the result set
      | value   |
      | INTEGER |
      | null    |
      | 2       |
      | 3       |
      | 4       |
      | 6       |

  Scenario: I remove on from a column

    When I execute the query
      """
      SELECT value - 1 FROM test
      """

    Then I expect the result set
      | value   |
      | INTEGER |
      | null    |
      | 0       |
      | 1       |
      | 2       |
      | 4       |

  Scenario: I multiply a column by 2

    When I execute the query
    """
    SELECT value * 2 FROM test
    """

    Then I expect the result set
      | value   |
      | INTEGER |
      | null    |
      | 2       |
      | 4       |
      | 6       |
      | 10      |



