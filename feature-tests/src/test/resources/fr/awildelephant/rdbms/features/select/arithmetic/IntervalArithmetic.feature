Feature: Interval data type

  Scenario: I remove one day to the current date

    When I execute the query
    """
    VALUES (DATE '2019-03-21' - INTERVAL '1' DAY)
    """

    Then I expect the result set
      | column1    |
      | DATE       |
      | 2019-03-20 |

  Scenario: I add one day to the current date

    When I execute the query
    """
    VALUES (DATE '2019-03-21' + INTERVAL '1' DAY)
    """

    Then I expect the result set
      | column1    |
      | DATE       |
      | 2019-03-22 |
