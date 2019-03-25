Feature: Insert into a single decimal column

  Background: A table with a single decimal column

    Given the table test
      | a       |
      | DECIMAL |

  Scenario: I insert a decimal value

    When I execute the query
      """
      INSERT INTO test VALUES (1.234)
      """

    Then table test should be
      | a       |
      | DECIMAL |
      | 1.234   |

  Scenario: I insert null

    When I execute the query
      """
      INSERT INTO test VALUES (null)
      """

    Then table test should be
      | a       |
      | DECIMAL |
      | null    |
