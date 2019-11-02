Feature: Less than comparison

  Scenario: I test that an integer is less than another

    When I execute the query
      """
      SELECT column1 < column2 AS result FROM (VALUES
        (1, 2),
        (1, 1),
        (1, 0),
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

  Scenario: I test that a decimal is less than another

    When I execute the query
      """
      SELECT column1 < column2 AS result FROM (VALUES
        (1.1, 1.2),
        (1.1, 1.1),
        (1.1, 1.0),
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

  Scenario: I test that a date is less than another

    When I execute the query
      """
      SELECT column1 < column2 AS result FROM (VALUES
        (date '2019-06-29', date '2019-06-30'),
        (date '2019-06-29', date '2019-06-29'),
        (date '2019-06-29', date '2019-06-28'),
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

  Scenario: I compare a decimal and an integer value

    When I execute the query
    """
    VALUES (0.5 < 1)
    """

    Then I expect the result set
      | column1 |
      | BOOLEAN |
      | true    |