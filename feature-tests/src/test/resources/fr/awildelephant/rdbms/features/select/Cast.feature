Feature: Cast

  Scenario: Date constant, which is translated to a cast from string to date

    When I execute the query
    """
    VALUES (date '1992-05-20')
    """

    Then I expect the result set
      | column1    |
      | DATE       |
      | 1992-05-20 |
