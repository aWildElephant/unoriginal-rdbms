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

  Scenario: I can add a dot after an integer value to make it a decimal

    Given the table test
      | weOnlyCareAboutTheNumberOfRows |
      | TEXT                           |
      | meh                            |

    When I execute the query
    """
    SELECT 2. FROM test
    """

    Then I expect the result set
      | 2       |
      | DECIMAL |
      | 2       |

  Scenario: I select true from a table with several rows

    Given the table test
      | weOnlyCareAboutTheNumberOfRows |
      | TEXT                           |
      | hello                          |
      | world                          |

    When I execute the query
    """
    SELECT true FROM test
    """

    Then I expect the result set
      | true    |
      | BOOLEAN |
      | true    |
      | true    |

  Scenario: I select false from a table with several rows

    Given the table test
      | weOnlyCareAboutTheNumberOfRows |
      | TEXT                           |
      | hello                          |
      | world                          |

    When I execute the query
    """
    SELECT false FROM test
    """

    Then I expect the result set
      | false   |
      | BOOLEAN |
      | false   |
      | false   |
