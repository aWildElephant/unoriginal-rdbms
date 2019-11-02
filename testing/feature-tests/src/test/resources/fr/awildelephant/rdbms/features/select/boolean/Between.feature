Feature: Between predicate

  Scenario: I test a between predicate with integer values

    When I execute the query
    """
    SELECT column1 between 0 and 99 AS result FROM (VALUES
      (42),
      (0),
      (99),
      (-1),
      (100),
      (null)
    );
    """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | true    |
      | true    |
      | true    |
      | false   |
      | false   |
      | unknown |
