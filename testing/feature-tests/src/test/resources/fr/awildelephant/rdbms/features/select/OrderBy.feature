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

  Scenario: I order a table by a single not null integer column in descending order

    Given the table test
      | a                |
      | INTEGER NOT NULL |
      | 4                |
      | 1                |
      | 2                |
      | 3                |

    When I execute the query
      """
      SELECT * FROM test ORDER BY a DESC
      """

    Then I expect the result set
      | a       |
      | INTEGER |
      | 4       |
      | 3       |
      | 2       |
      | 1       |

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

  Scenario: I order and group by the same column

    Given the table test
      | a    | b       |
      | TEXT | INTEGER |
      | b    | 2       |
      | a    | 1       |
      | b    | 3       |
      | b    | 4       |

    When I execute the query
      """
      SELECT a, count(*) FROM test GROUP BY a ORDER BY a
      """

    Then I expect the result set
      | a    | count(*) |
      | TEXT | INTEGER  |
      | a    | 1        |
      | b    | 3        |
