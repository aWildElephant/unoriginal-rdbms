Feature: Qualified column names

  Background: a table

    Given the table employee
      | name    |
      | TEXT    |
      | Etienne |

  Scenario: I qualify a column with its table

    When I execute the query
    """
    SELECT employee.name FROM employee
    """

    Then I expect the result set
      | name    |
      | TEXT    |
      | Etienne |

  Scenario: I qualify a column with an unknown table

    When I execute the query
    """
    SELECT employer.name FROM employee
    """

    Then I expect an error with the message
    """
    Column not found: employer.name
    """

  Scenario: I qualify a column in a min aggregate

    When I execute the query
    """
    SELECT min(employee.name) FROM employee
    """

    Then I expect the result set
      | min(employee.name) |
      | TEXT               |
      | Etienne            |
