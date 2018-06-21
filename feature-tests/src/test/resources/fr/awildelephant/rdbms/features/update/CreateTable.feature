Feature: Create table

  Scenario: I create an empty table with a single integer column

    When I execute the query
    """
    CREATE TABLE test (a INTEGER)
    """

    Then table test should be
      | a       |
      | INTEGER |

  Scenario: I create an empty table with a single text column

    When I execute the query
      """
      CREATE TABLE test (a TEXT)
      """

    Then table test should be
      | a    |
      | TEXT |