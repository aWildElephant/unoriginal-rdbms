@todo
Feature: I filter using the having clause

  Background: a table and some data

    Given the table client
      | client_id | client_name |
      | INTEGER   | TEXT        |
      | 0         | Michel      |
      | 1         | Bernard     |

    And the table orders
      | client_id | amount  |
      | INTEGER   | DECIMAL |
      | 0         | 19.99   |
      | 1         | 9.99    |
      | 0         | 3.75    |

  Scenario: I filter on clients that have ordered more than 20€ total

    When I execute the query
      """
      SELECT client_name
      FROM client INNER JOIN orders ON client.client_id = orders.client_id
      HAVING SUM(amount) > 20
      """

    Then I expect the result set
      | client_name |
      | TEXT        |
      | Michel      |
