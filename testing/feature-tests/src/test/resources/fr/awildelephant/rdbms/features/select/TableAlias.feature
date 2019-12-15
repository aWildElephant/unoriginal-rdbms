Feature: Table alias

  Background: a table

    Given the table test
      | a    |
      | TEXT |
      | ok   |

  Scenario: I select from an aliased table

    When I execute the query
    """
    SELECT alias.a FROM test AS alias
    """

    Then I expect the result set
      | a    |
      | TEXT |
      | ok   |

  Scenario: I select from an aliased table without the AS keyword

    When I execute the query
    """
    SELECT alias.a FROM test alias
    """

    Then I expect the result set
      | a    |
      | TEXT |
      | ok   |

  Scenario: I select from an table with both table and column aliases

    When I execute the query
    """
    SELECT table_alias.column_alias FROM test AS table_alias (column_alias)
    """

    Then I expect the result set
      | column_alias |
      | TEXT         |
      | ok           |
