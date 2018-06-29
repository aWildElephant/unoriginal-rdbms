Feature: Inserting null value

  Scenario: I insert null into a decimal column

    Given the table test
      | value   |
      | DECIMAL |

    When I execute the query
    """
    INSERT INTO test VALUES (null)
    """

    Then table test should be
      | value   |
      | DECIMAL |
      | null    |

  Scenario: I insert null into an integer column

    Given the table test
      | id      |
      | INTEGER |

    When I execute the query
    """
    INSERT INTO test VALUES (null)
    """

    Then table test should be
      | id      |
      | INTEGER |
      | null    |

  Scenario: I insert null into a text column

    Given the table test
      | name |
      | TEXT |

    When I execute the query
      """
      INSERT INTO test VALUES (null)
      """

    Then table test should be
      | name |
      | TEXT |
      | null |