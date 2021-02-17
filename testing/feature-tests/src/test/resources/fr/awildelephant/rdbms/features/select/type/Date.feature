Feature: In predicate

  Scenario: I output dates

    When I execute the query
    """
    VALUES (date '2021-02-17'), (date('2021-02-18')), (null);
    """

    Then I expect the result set
      | column1    |
      | DATE       |
      | 2021-02-17 |
      | 2021-02-18 |
      | null       |
