@todo
Feature: I select a constant value from a table

  Scenario: I select a constant from an empty table

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

  Scenario: I select a constant from a table with several rows

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
