Feature: Simple multiplication

  Background: a table and some data

    Given the table test
      | i       | d       |
      | INTEGER | DECIMAL |
      | null    | null    |
      | 1       | 1.0     |
      | 2       | 2.0     |
      | 3       | 3.0     |
      | 5       | 5.0     |

  Scenario: I multiply an integer column by 2

    When I execute the query
      """
      SELECT i * 2 FROM test
      """

    Then I expect the result set
      | i * 2   |
      | INTEGER |
      | null    |
      | 2       |
      | 4       |
      | 6       |
      | 10      |

  Scenario: I multiply an integer column by decimal 2

    When I execute the query
      """
      SELECT i * 2.0 FROM test
      """

    Then I expect the result set
      | i * 2.0 |
      | DECIMAL |
      | null    |
      | 2.0     |
      | 4.0     |
      | 6.0     |
      | 10.0    |

  Scenario: I multiply a decimal column by 2

    When I execute the query
      """
      SELECT d * 2 FROM test
      """

    Then I expect the result set
      | d * 2   |
      | DECIMAL |
      | null    |
      | 2.0     |
      | 4.0     |
      | 6.0     |
      | 10.0    |
