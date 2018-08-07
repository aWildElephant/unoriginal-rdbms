Feature: Create table

  Scenario: I create an empty table with a single integer column

    When I execute the query
    """
    CREATE TABLE test (a INTEGER)
    """

    Then table test should be
      | a       |
      | INTEGER |

  Scenario: I create an empty table with text columns

    When I execute the query
    """
    CREATE TABLE test (a TEXT, b CHAR(8), c VARCHAR(8))
    """

    Then table test should be
      | a    | b    | c    |
      | TEXT | TEXT | TEXT |

  Scenario: I create an empty table with a date column

    When I execute the query
    """
    CREATE TABLE test (a INTEGER UNIQUE NOT NULL, b DATE)
    """

    Then table test should be
      | a       | b    |
      | INTEGER | DATE |
