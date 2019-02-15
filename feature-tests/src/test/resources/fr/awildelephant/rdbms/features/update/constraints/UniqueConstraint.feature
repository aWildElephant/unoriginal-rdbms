Feature: Unique constraint

  Background: a table with an unique column

    Given the table person
      | pseudo      |
      | TEXT UNIQUE |
      | mistermv    |

  Scenario: I insert a value that doesn't exist in an unique column

    When I execute the query
      """
      INSERT INTO person VALUES ('zerator')
      """

    Then table person should be
      | pseudo   |
      | TEXT     |
      | mistermv |
      | zerator  |

  Scenario: I insert a value that already exist in an unique column

    When I execute the query
      """
      INSERT INTO person VALUES ('mistermv')
      """

    Then I expect an error with the message
      """
      Unique constraint violation
      """

  Scenario: I insert several null values in the an unique column

    When I execute the query
      """
      INSERT INTO person VALUES (null)
      """

    And I execute the query
      """
      INSERT INTO person VALUES (null)
      """

    Then table person should be
      | pseudo   |
      | TEXT     |
      | mistermv |
      | null     |
      | null     |
