Feature: Greater than comparison

  Scenario: I test that an integer is greater than another

    When I execute the query
      """
      SELECT column1 > column2 AS result FROM (VALUES
        (2, 1),
        (1, 1),
        (0, 1),
        (1, null),
        (null, 1),
        (null, null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | false   |
      | false   |
      | null    |
      | null    |
      | null    |

  Scenario: I test that a decimal is greater than another

    When I execute the query
      """
      SELECT column1 > column2 AS result FROM (VALUES
        (1.2, 1.1),
        (1.1, 1.1),
        (1.0, 1.1),
        (1.1, null),
        (null, 1.1),
        (null, null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | false   |
      | false   |
      | null    |
      | null    |
      | null    |

  Scenario: I test that a date is greater than another

    When I execute the query
      """
      SELECT column1 > column2 AS result FROM (VALUES
        (date '2019-06-30', date '2019-06-29'),
        (date '2019-06-29', date '2019-06-29'),
        (date '2019-06-28', date '2019-06-29'),
        (date '2019-06-29', null),
        (null, date '2018-07-14'),
        (null, null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | false   |
      | false   |
      | null    |
      | null    |
      | null    |