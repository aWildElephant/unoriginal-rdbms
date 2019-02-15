Feature: Insert into a single text column

  Background: A table with a single text column

    Given the table test
      | a    |
      | TEXT |

  Scenario: I insert a value

    When I execute the query
      """
      INSERT INTO test VALUES ('bernard')
      """

    Then table test should be
      | a       |
      | TEXT    |
      | bernard |

  Scenario: I insert null

    When I execute the query
      """
      INSERT INTO test VALUES (null)
      """

    Then table test should be
      | a    |
      | TEXT |
      | null |
