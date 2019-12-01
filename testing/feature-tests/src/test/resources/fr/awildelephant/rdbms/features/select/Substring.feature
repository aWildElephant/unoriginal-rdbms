Feature: Substring

  Scenario: I cut five characters out of a string starting from the second

    When I execute the query
    """
    SELECT SUBSTRING(column1 FROM 2 FOR 7) AS cut FROM (VALUES
      ('hi'),
      ('hello good sir'),
      (''),
      (null)
    )
    """

    Then I expect the result set
      | cut     |
      | TEXT    |
      | i       |
      | ello go |
      | ""      |
      | null    |
