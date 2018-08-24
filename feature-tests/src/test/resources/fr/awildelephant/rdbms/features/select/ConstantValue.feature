Feature: I select a constant value from a table

  Scenario: I select an integer constant from an empty table

    Given the table test
      | weOnlyCareAboutTheNumberOfRows |
      | TEXT                           |

    When I execute the query
    """
    SELECT 42 FROM test
    """

    Then I expect the result set
      | 42      |
      | INTEGER |

  Scenario: I select an integer constant from a table with several rows

    Given the table test
      | weOnlyCareAboutTheNumberOfRows |
      | TEXT                           |
      | hello                          |
      | null                           |

    When I execute the query
    """
    SELECT 42 FROM test
    """

    Then I expect the result set
      | 42      |
      | INTEGER |
      | 42      |
      | 42      |

  Scenario: I select a text constant from a table with several rows

    Given the table test
      | weOnlyCareAboutTheNumberOfRows |
      | TEXT                           |
      | hello                          |
      | null                           |

    When I execute the query
      """
      SELECT 'world' from test
      """

    Then I expect the result set
      | world |
      | TEXT  |
      | world |
      | world |
