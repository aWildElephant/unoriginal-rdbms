Feature: Aliasing a column

  Scenario: I alias a column reference

    Given the table test
      | noalias |
      | INTEGER |
      | 1       |

    When I execute the query
      """
      SELECT noalias AS alias FROM test
      """

    Then I expect the result set
      | alias   |
      | INTEGER |
      | 1       |

  Scenario: I swap the column names of a table

    Given the table test
      | a       | b       |
      | INTEGER | INTEGER |
      | 1       | 2       |

    When I execute the query
      """
      SELECT b AS a, a AS b FROM test
      """

    Then I expect the result set
      | a       | b       |
      | INTEGER | INTEGER |
      | 2       | 1       |

  @todo
  Scenario: I alias the same column several times

    Given the table test
      | a       |
      | INTEGER |
      | 1       |

    When I execute the query
    """
    SELECT a AS b, a AS c FROM test
    """

    Then I expect the result set
      | b       | c       |
      | INTEGER | INTEGER |
      | 1       | 1       |

  Scenario: I alias two columns with the same name from two different tables

    Given the table zero
      | a       |
      | INTEGER |
      | 0       |

    Given the table one
      | a       |
      | INTEGER |
      | 1       |

    When I execute the query
    """
    SELECT zero.a AS zero, one.a AS one FROM zero, one
    """

    Then I expect the result set
      | zero    | one     |
      | INTEGER | INTEGER |
      | 0       | 1       |
