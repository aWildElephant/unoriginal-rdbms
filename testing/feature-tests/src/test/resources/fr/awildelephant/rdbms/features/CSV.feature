Feature: CSV

  Scenario: I read the contents of a CSV file

    When I execute the query
    """
    READ CSV 'src/test/resources/read_csv_1.csv.gz' (id INTEGER, birth_date DATE, first_name TEXT)
    """

    Then I expect the result set
      | id      | birth_date | first_name |
      | INTEGER | DATE       | TEXT       |
      | 1       | 1990-05-24 | Nicolas    |
      | 2       | 1992-05-20 | Etienne    |
