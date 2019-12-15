@todo
Feature: Left join

  Scenario: Left join between two tables

    Given the table person
      | name     | address_id |
      | TEXT     | INTEGER    |
      | Bernard  | 1          |
      | Jacky    | 2          |
      | Yennefer | 3          |


    Given the table address
      | id      | zip_code |
      | INTEGER | INTEGER  |
      | 1       | 78350    |
      | 2       | 46201    |

    When I execute the query
      """
      SELECT name, zip_code FROM person LEFT JOIN address ON address_id = id
      """

    Then I expect the result set
      | name     | zip_code |
      | TEXT     | INTEGER  |
      | Bernard  | 78350    |
      | Jacky    | 46201    |
      | Yennefer | null     |
