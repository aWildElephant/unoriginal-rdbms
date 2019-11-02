Feature: Extract function

  Scenario: I extract the year from a date

    When I execute the query
    """
    VALUES (EXTRACT(YEAR FROM DATE '2019-11-02'))
    """

    Then I expect the result set
      | column1 |
      | INTEGER |
      | 2019    |

  Scenario: I extract the year from a column with date type

    Given the table test
      | order_date |
      | DATE       |
      | 2019-11-02 |
      | null       |

    When I execute the query
    """
    SELECT EXTRACT(YEAR FROM order_date) as order_year FROM test
    """

    Then I expect the result set
      | order_year |
      | INTEGER    |
      | 2019       |
      | null       |

  Scenario: I try to extract the year from an incompatible type

    When I execute the query
    """
    VALUES (EXTRACT(YEAR FROM 1))
    """

    Then I expect an error with the message
    """
    Cannot extract YEAR from incompatible type INTEGER
    """
