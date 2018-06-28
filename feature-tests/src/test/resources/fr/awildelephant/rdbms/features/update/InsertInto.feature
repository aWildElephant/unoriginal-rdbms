Feature: Insert into

  Scenario: I insert a single digit into an integer column

    Given the table test
      | a       |
      | INTEGER |

    And I execute the query
    """
    INSERT INTO test VALUES (1)
    """

    Then table test should be
      | a       |
      | INTEGER |
      | 1       |

  Scenario: I insert an integer and a string into a table

    Given the table people
      | id      | name |
      | INTEGER | TEXT |

    And I execute the query
    """
    INSERT INTO people VALUES (1, 'bernard');
    """

    Then table people should be
      | id      | name    |
      | INTEGER | TEXT    |
      | 1       | bernard |

  Scenario: I insert an integer greater than 10 into a table

    Given the table test
      | a       |
      | INTEGER |

    When I execute the query
    """
    INSERT INTO test VALUES (1992)
    """

    Then table test should be
      | a       |
      | INTEGER |
      | 1992    |

  Scenario: I insert a decimal value into a table

    Given the table test
      | a       |
      | DECIMAL |

    When I execute the query
      """
      INSERT INTO test VALUES (1.234)
      """

    Then table test should be
      | a       |
      | DECIMAL |
      | 1.234   |