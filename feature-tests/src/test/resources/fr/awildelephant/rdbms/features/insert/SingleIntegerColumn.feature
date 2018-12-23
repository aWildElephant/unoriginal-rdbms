Feature: Insert into a single integer column

  Background: A table with a single integer column

    Given the table test
      | a       |
      | INTEGER |

  Scenario: I insert a single digit

    And I execute the query
    """
    INSERT INTO test VALUES (1)
    """

    Then table test should be
      | a       |
      | INTEGER |
      | 1       |

  Scenario: I insert an integer greater than 10

    When I execute the query
    """
    INSERT INTO test VALUES (1992)
    """

    Then table test should be
      | a       |
      | INTEGER |
      | 1992    |

  Scenario: I insert null

    When I execute the query
    """
    INSERT INTO test VALUES (null)
    """

    Then table test should be
      | a       |
      | INTEGER |
      | null    |

  Scenario: I insert a negative value

    When I execute the query
    """
    INSERT INTO test VALUES (-1)
    """

    Then table test should be
      | a       |
      | INTEGER |
      | -1      |
