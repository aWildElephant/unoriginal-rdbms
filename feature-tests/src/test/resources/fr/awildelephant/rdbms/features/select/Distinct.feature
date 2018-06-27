Feature: Distinct

  Background: a table with some data

    Given the table Employees
      | Id      | FirstName | LastName |
      | INTEGER | TEXT      | TEXT     |
      | 1       | Joe       | Dalton   |
      | 2       | Jack      | Dalton   |
      | 3       | William   | Dalton   |
      | 4       | Averell   | Dalton   |
      | 5       | Etienne   | Girard   |

  Scenario: I group by a column and only output this column to get distinct values

    When I execute the query
    """
    SELECT DISTINCT LastName
    FROM Employees
    """

    Then I expect the result set
      | LastName |
      | TEXT     |
      | Dalton   |
      | Girard   |



