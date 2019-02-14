Feature: Boolean operations

    @todo
    Scenario: I output the truth table for and

        When I execute the query
        """
        SELECT column1 AND column2 AS result FROM (VALUES
            (true, true),
            (true, false),
            (false, true),
            (false, false)
        );
        """

        Then I expect the result set
        | result  |
        | BOOLEAN |
        | true    |
        | false   |
        | false   |
        | false   |

    @todo
    Scenario: I output the truth table for or

        When I execute the query
        """
        SELECT column1 OR column2 AS result FROM (VALUES
            (true, true),
            (true, false),
            (false, true),
            (false, false)
        );
        """

        Then I expect the result set
        | result  |
        | BOOLEAN |
        | true    |
        | true    |
        | true    |
        | false   |

    @todo
    Scenario: I output the expect result for not

        When I execute the query
        """
        SELECT NOT column1 AS result FROM (VALUES
            (true),
            (false)
        );
        """

        Then I expect the result set
        | result  |
        | BOOLEAN |
        | false   |
        | true    |
