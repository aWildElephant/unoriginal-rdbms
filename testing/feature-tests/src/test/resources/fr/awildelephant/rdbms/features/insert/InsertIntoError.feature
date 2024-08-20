Feature: Insert Into errors

  Scenario: I try to insert in a table that doesn't exist

    When I execute the query
      """
      INSERT INTO UnknownTable VALUES (1);
      """

    Then I expect an error with the message
      """
      Table not found: unknowntable
      """

  Scenario: I try to insert a row that doesn't have enough columns for the table

    Given the table people
      | id      | name |
      | INTEGER | TEXT |

    When I execute the query
      """
      INSERT INTO people VALUES (1)
      """

    Then I expect an error with the message
      """
      Column count mismatch: expected 2 but got 1
      """

  # TODO: have an exception type for engine error rather than throwing an IllegalArgumentException
  Scenario: I try to insert a value of the wrong type in a column

    Given the table test
      | a       |
      | INTEGER |

    When I execute the query
      """
      INSERT INTO test VALUES ('wrong value')
      """

    Then I expect an error with the message
      """
      Column 'test.a': type TEXT is incompatible with type INTEGER
      """