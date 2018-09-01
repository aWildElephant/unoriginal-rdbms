Feature: Simple division

  Background: a table and some data

    Given the table test
      | i       | d       |
      | INTEGER | DECIMAL |
      | null    | null    |
      | 1       | 1.0     |
      | 2       | 2.0     |
      | 3       | 3.0     |
      | 5       | 5.0     |

  Scenario: I divide an integer column by 2

    When I execute the query
    """
    SELECT i / 2 FROM test
    """

    Then I expect the result set
      | i / 2   |
      | INTEGER |
      | null    |
      | 0       |
      | 1       |
      | 1       |
      | 2       |

  Scenario: I divide an integer column by decimal 2

    When I execute the query
    """
    SELECT i / 2.0 FROM test
    """

    Then I expect the result set
      | i / 2   |
      | DECIMAL |
      | null    |
      | 0.5     |
      | 1       |
      | 1.5     |
      | 2.5     |

  Scenario: I divide a decimal column by 2

    When I execute the query
    """
    SELECT d / 2 FROM test
    """

    Then I expect the result set
      | d / 2   |
      | DECIMAL |
      | null    |
      | 0.5     |
      | 1.0     |
      | 1.5     |
      | 2.5     |

  Scenario: I divide a column by zero

    When I execute the query
    """
    SELECT i / 0 FROM test
    """

    Then I expect an error with the message
    """
    Division by zero
    """
