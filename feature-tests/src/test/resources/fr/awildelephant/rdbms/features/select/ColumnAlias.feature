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
