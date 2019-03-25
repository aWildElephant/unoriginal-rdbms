Feature: Simple addition

  Background: a table and some data

    Given the table test
      | i       | d       |
      | INTEGER | DECIMAL |
      | null    | null    |
      | 1       | 1.0     |
      | 2       | 2.0     |
      | 3       | 3.0     |
      | 5       | 5.0     |

  Scenario: I add one to a column

    When I execute the query
      """
      SELECT i + 1 FROM test
      """

    Then I expect the result set
      | i + 1   |
      | INTEGER |
      | null    |
      | 2       |
      | 3       |
      | 4       |
      | 6       |

  Scenario: I add a decimal to an integer column

    When I execute the query
      """
      SELECT i + 1.0 FROM test
      """

    Then I expect the result set
      | i + 1.0 |
      | DECIMAL |
      | null    |
      | 2.0     |
      | 3.0     |
      | 4.0     |
      | 6.0     |

  Scenario: I add an integer to a decimal column

    When I execute the query
      """
      SELECT d + 1 FROM test
      """

    Then I expect the result set
      | d + 1   |
      | DECIMAL |
      | null    |
      | 2.0     |
      | 3.0     |
      | 4.0     |
      | 6.0     |

  Scenario: I add null to an integer column

    When I execute the query
      """
      SELECT i + null FROM test
      """

    Then I expect the result set
      | i + null |
      | INTEGER  |
      | null     |
      | null     |
      | null     |
      | null     |
      | null     |
