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
      | a             | b                |
      | TEXT NOT NULL | INTEGER NOT NULL |
      | b             | 2                |
      | a             | 1                |
      | b             | 3                |
      | b             | 4                |

    When I execute the query
      """
      SELECT a, count(*) FROM test GROUP BY a ORDER BY a
      """

    Then I expect the result set
      | a    | count(*) |
      | TEXT | INTEGER  |
      | a    | 1        |
      | b    | 3        |

  # TODO: implement NULLS FIRST/NULLS LAST ? (sql 2003)
  @todo
  Scenario: I order by an integer column with a null value

    Given the table test
      | a       |
      | INTEGER |
      | 3       |
      | 2       |
      | null    |
      | 0       |

    When I execute the query
    """
    SELECT a FROM test ORDER BY a ASC
    """

    Then I expect the result set
      | a       |
      | INTEGER |
      | null    |
      | 0       |
      | 2       |
      | 3       |

  Scenario: I order by a bigint column

    Given the table test
      | a      |
      | BIGINT |
      | 3      |
      | 1      |
      | 2      |

    When I execute the query
      """
      SELECT a FROM test ORDER BY a DESC
      """

    Then I expect the result set
      | a      |
      | BIGINT |
      | 3      |
      | 2      |
      | 1      |
