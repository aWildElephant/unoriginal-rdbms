Feature: Where clause

  Scenario: I select where a column is true

    When I execute the query
    """
    SELECT column1 FROM (VALUES ('you should see this', true), ('but not this', false)) WHERE column2
    """

    Then I expect the result set
      | column1             |
      | TEXT                |
      | you should see this |
