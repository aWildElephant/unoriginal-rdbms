Feature: Simple subtraction

  Background: a table and some data

    Given the table test
      | i       | d       |
      | INTEGER | DECIMAL |
      | null    | null    |
      | 1       | 1.0     |
      | 2       | 2.0     |
      | 3       | 3.0     |
      | 5       | 5.0     |

  Scenario: I subtract one to a column

    When I execute the query
    """
    SELECT i - 1 FROM test
    """

    Then I expect the result set
      | i - 1   |
      | INTEGER |
      | null    |
      | 0       |
      | 1       |
      | 2       |
      | 4       |

  Scenario: I subtract a decimal to an integer column

    When I execute the query
    """
    SELECT i - 1.0 FROM test
    """

    Then I expect the result set
      | i - 1.0 |
      | DECIMAL |
      | null    |
      | 0.0     |
      | 1.0     |
      | 2.0     |
      | 4.0     |

  Scenario: I subtract an integer to a decimal column

    When I execute the query
    """
    SELECT d - 1 FROM test
    """

    Then I expect the result set
      | d - 1   |
      | DECIMAL |
      | null    |
      | 0.0     |
      | 1.0     |
      | 2.0     |
      | 4.0     |
