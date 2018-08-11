Feature: Parenthesized column reference

  Background: A table with some data

    Given the table test
      | value   |
      | INTEGER |
      | null    |
      | 13      |

  Scenario: I select a column with parenthesis around the reference

    When I execute the query
    """
    SELECT (value) FROM test
    """

    Then I expect the result set
      | value   |
      | INTEGER |
      | null    |
      | 13      |

  Scenario: I alias a parenthesized column

    When I execute the query
    """
    SELECT (value) AS alias FROM test
    """

    Then I expect the result set
      | alias   |
      | INTEGER |
      | null    |
      | 13      |