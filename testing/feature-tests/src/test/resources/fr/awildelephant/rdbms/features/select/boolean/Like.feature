Feature: Like predicate

  Scenario: I test a LIKE predicate with a percent wildcard

    When I execute the query
      """
      SELECT column1 LIKE 'a%z'AS result FROM (VALUES
        ('test'),
        ('az'),
        ('abc...xyz'),
        (null)
      );
      """

    Then I expect the result set
      | result  |
      | BOOLEAN |
      | false   |
      | true    |
      | true    |
      | null    |

    # tester: les charactères spéciaux des regex java dans le pattern, not like, null en pattern...
