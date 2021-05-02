Feature: With

  Scenario: I execute a query with the same query name twice in the with clause

    # TODO: (not related to this test but found executing it) if the query has a syntax error, the exception is lost

    When I execute the query
    """
    WITH oops AS (VALUES ('hello')), oops AS (VALUES ('WORLD')) SELECT * FROM oops
    """

    Then I expect an error with the message
    """
    Duplicate query name 'oops' in with clause
    """

  Scenario: I execute a simple with statement

    Given the table employees
      | id      | name     |
      | integer | text     |
      | 1       | Leo      |
      | 2       | Corentin |
      | 3       | Vincent  |
      | 4       | Etienne  |

    When I execute the query
    """
    WITH proxy AS (SELECT id as no, name as first_name FROM employees) SELECT * FROM proxy
    """

    Then I expect the result set
      | no      | first_name |
      | integer | text       |
      | 1       | Leo        |
      | 2       | Corentin   |
      | 3       | Vincent    |
      | 4       | Etienne    |
