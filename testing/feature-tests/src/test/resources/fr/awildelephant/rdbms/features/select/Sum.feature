Feature: Sum

  Background: a table with some data

    Given the table test
      | a       | b       | c      | d       |
      | INTEGER | INTEGER | BIGINT | DECIMAL |
      | 1       | 1       | 1      | 1       |
      | 2       | 4       | 1      | 1       |
      | 3       | 8       | 1      | 1       |

  Scenario: I sum the column

    When I execute the query
      """
      SELECT SUM(a) FROM test
      """

    Then I expect the result set
      | sum(a)  |
      | INTEGER |
      | 6       |

  Scenario: I sum a map on several columns

    When I execute the query
      """
      SELECT SUM(a*b) FROM test
      """

    Then I expect the result set
      | sum(a * b) |
      | INTEGER    |
      | 33         |

  Scenario: I sum a bigint column

    When I execute the query
      """
      SELECT SUM(c) FROM test
      """

    Then I expect the result set
      | sum(c) |
      | BIGINT |
      | 3      |

  Scenario: I sum a decimal column

    When I execute the query
      """
      SELECT sum (d) FROM test
      """

    Then I expect the result set
      | sum(d)  |
      | DECIMAL |
      | 3       |