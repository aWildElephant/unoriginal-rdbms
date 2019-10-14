Feature: I select a constant value from a table

  @todo # Remove system.nothing. Support "VALUES ()"?
  Scenario: I select an integer constant from an empty table

    When I execute the query
      """
      SELECT 42 FROM system.nothing
      """

    Then I expect the result set
      | 42      |
      | INTEGER |

  Scenario: I select an integer constant from a table with several rows

    When I execute the query
      """
      SELECT 42 FROM (VALUES ('hello'), ('there'))
      """

    Then I expect the result set
      | 42      |
      | INTEGER |
      | 42      |
      | 42      |

  Scenario: I select a text constant from a table with several rows

    When I execute the query
      """
      SELECT 'fight' from (VALUES ('general'), ('kenobi'))
      """

    Then I expect the result set
      | fight |
      | TEXT  |
      | fight |
      | fight |

  Scenario: I can add a dot after an integer value to make it a decimal

    When I execute the query
      """
      SELECT 2. FROM (VALUES ('meh'))
      """

    Then I expect the result set
      | 2       |
      | DECIMAL |
      | 2       |

  Scenario: I select true from a table with several rows

    When I execute the query
      """
      SELECT true FROM (VALUES ('I like working'), ('on this project'))
      """

    Then I expect the result set
      | true    |
      | BOOLEAN |
      | true    |
      | true    |

  Scenario: I select false from a table with several rows

    When I execute the query
      """
      SELECT false FROM (VALUES ('I am'), ('a successful man'))
      """

    Then I expect the result set
      | false   |
      | BOOLEAN |
      | false   |
      | false   |
