Feature: Insert into

    Scenario: I insert a single value into a table

        Given I execute the query
        """
        CREATE TABLE z (y INTEGER);
        """

        And I execute the query
        """
        INSERT INTO z VALUES (1);
        """

        When I execute the query
        """
        SELECT y FROM z
        """

        Then I expect the result set
          | y       |
          | INTEGER |
          | 1       |