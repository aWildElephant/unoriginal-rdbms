Feature: Insert into a single boolean column

  Background: A table with a single boolean column

    Given the table test
      | a       |
      | BOOLEAN |

  Scenario: I insert values

    When I execute the query
    """
    INSERT INTO test VALUES (TRUE), (FALSE)
    """

    Then table test should be
      | a       |
      | BOOLEAN |
      | true    |
      | false   |

  Scenario: I insert null values

    When I execute the query
    """
    INSERT INTO test VALUES (NULL), (UNKNOWN)
    """

    Then table test should be
      | a       |
      | BOOLEAN |
      | null    |
      | null    |
