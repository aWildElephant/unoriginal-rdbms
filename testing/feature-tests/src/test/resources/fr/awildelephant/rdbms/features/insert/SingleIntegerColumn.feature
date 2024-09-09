Feature: Insert into a single integer column

  Background: A table with a single integer column

    Given the table test
      | a       |
      | INTEGER |

  Scenario: I insert several valid values

    And I execute the query
      """
      INSERT INTO test VALUES (null), (0), (1), (-1), (1992), (2147483647), (-2147483648)
      """

    Then table test should be
      | a           |
      | INTEGER     |
      | null        |
      | 0           |
      | 1           |
      | -1          |
      | 1992        |
      | 2147483647  |
      | -2147483648 |


  Scenario: I insert a value lower than INTEGER's min value

    When I execute the query
      """
      INSERT INTO test VALUES (-2147483649)
      """

    Then I expect an error with the message
      """
      Value out of bounds: -2147483649
      """

  Scenario: I insert a value greater than INTEGER's max value

    When I execute the query
      """
      INSERT INTO test VALUES (2147483648)
      """

    Then I expect an error with the message
      """
      Value out of bounds: 2147483648
      """
