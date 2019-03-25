Feature: Order by

  Scenario: I order a table by a single not null integer column

    Given the table test
      | a                |
      | INTEGER NOT NULL |
      | 2                |
      | 1                |
      | 3                |

    When I execute the query
      """
      SELECT * FROM test ORDER BY a
      """

    Then I expect the result set
      | a       |
      | INTEGER |
      | 1       |
      | 2       |
      | 3       |

  Scenario: I order a table by a single not null text column

    Given the table test
      | a             |
      | TEXT NOT NULL |
      | France        |
      | Armenia       |
      | Lebanon       |
      | Ireland       |

    When I execute the query
      """
      SELECT * FROM test ORDER BY a
      """

    Then I expect the result set
      | a       |
      | TEXT    |
      | Armenia |
      | France  |
      | Ireland |
      | Lebanon |
