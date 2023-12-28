Feature: CSV

  @todo
  Scenario: I read the contents of a CSV file

    When I execute the query
    """
    READ CSV '/to/do/test.csv' (id INTEGER, birth_date DATE, first_name TEXT)
    """

    Then I expect the result set
      | id      | birth_date | first_name |
      | INTEGER | DATE       | TEXT       |
      | 1       | 1992-05-20 | Etienne    |
