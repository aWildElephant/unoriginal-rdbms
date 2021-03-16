Feature: Min aggregate

  Scenario: I get the min value in an integer column with no null value

    When I execute the query
    """
    SELECT min(column1) as min_value
    FROM (VALUES (1), (2), (0), (-3))
    """

    Then I expect the result set
      | min_value |
      | INTEGER   |
      | -3        |

  Scenario: I get the min value in an integer column with a null value

    When I execute the query
    """
    SELECT min(column1) as min_value
    FROM (VALUES (null), (2), (0), (1))
    """

    Then I expect the result set
      | min_value |
      | INTEGER   |
      | 0         |
