Feature: Table alias

  Scenario: I select from an aliased table

    Given the table test
      | a    |
      | TEXT |
      | ok   |

    When I execute the query
    """
    SELECT alias.a FROM test AS alias
    """

    Then I expect the result set
      | a    |
      | TEXT |
      | ok   |
