Feature: Cast

  Scenario: Date constant, which is translated to a cast from text to date

    When I execute the query
    """
    VALUES (DATE '1992-05-20')
    """

    Then I expect the result set
      | column1    |
      | DATE       |
      | 1992-05-20 |

  Scenario: Explicit cast from text to date

    When I execute the query
    """
    VALUES (CAST('1992-05-20' AS DATE))
    """

    Then I expect the result set
      | column1    |
      | DATE       |
      | 1992-05-20 |

  @todo
  Scenario: I cast an integer constant to a text

    When I execute the query
    """
    VALUES (CAST(12 AS TEXT))
    """

    Then I expect the result set
      | column1 |
      | TEXT    |
      | 12      |
