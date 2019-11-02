Feature: Extract function

  Scenario: I extract the year from a date

    When I execute the query
    """
    VALUES (EXTRACT(YEAR FROM DATE '2019-11-02'))
    """

    Then I expect the result set
      | column1 |
      | INTEGER |
      | 2019    |
