Feature: Insert into a single date column

  Background: A table with a single date column

    Given the table test
      | a    |
      | DATE |

@debug
  Scenario: I insert a value

    When I execute the query
    """
    INSERT INTO test VALUES (date '1992-05-20')
    """

    Then table test should be
      | a          |
      | DATE       |
      | 1992-05-20 |

  Scenario: I insert null

    When I execute the query
      """
      INSERT INTO test VALUES (null)
      """

    Then table test should be
      | a    |
      | DATE |
      | null |
