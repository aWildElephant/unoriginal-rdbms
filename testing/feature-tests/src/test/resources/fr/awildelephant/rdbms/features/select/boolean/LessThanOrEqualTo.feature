Feature: Less than or equal to comparison

  Scenario: I test that a date is less than or equal to another

    When I execute the query
      """
      SELECT column1 <= column2 AS result FROM (VALUES
        (date '2018-07-15', date '2018-07-16'),
        (date '2018-07-15', date '2018-07-15'),
        (date '2018-07-15', date '2018-07-14'),
        (date '2018-07-14', null),
        (null, date '2018-07-14'),
        (null, null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | true    |
      | false   |
      | null    |
      | null    |
      | null    |

  Scenario: I test that a decimal is less than or equal to another

    When I execute the query
      """
      SELECT column1 <= column2 AS result FROM (VALUES
        (0.99999999999, 1),
        (0.99999999999, 0.99999999999),
        (1.00000000001, 1),
        (0.99999999999, null),
        (null, 0.99999999999),
        (null, null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | true    |
      | false   |
      | null    |
      | null    |
      | null    |