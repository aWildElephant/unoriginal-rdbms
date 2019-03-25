Feature: Drop table

  Scenario: I drop a table

    Given the table test
      | id      | name |
      | INTEGER | TEXT |

    When I execute the query
      """
      DROP TABLE Test;
      """

    Then there is no table named test

  Scenario: I try to drop a table that does not exist

    When I execute the query
      """
      DROP TABLE UnknownTable
      """

    Then I expect an error with the message
      """
      Table not found: unknowntable
      """
