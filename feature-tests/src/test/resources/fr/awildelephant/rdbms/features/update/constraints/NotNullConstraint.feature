Feature: Not null constraint

  Scenario: I try to insert null in a not null column

    Given the table test
      | notnullable      |
      | INTEGER NOT NULL |

    When I execute the query
      """
      INSERT INTO test VALUES (null)
      """

    Then I expect an error with the message
      """
      Cannot insert NULL in not-null column notnullable
      """
