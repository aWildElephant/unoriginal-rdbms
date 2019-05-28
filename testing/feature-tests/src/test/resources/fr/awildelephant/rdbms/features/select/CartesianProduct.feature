Feature: Cartesian product

  @todo
  Scenario: I query the cartesian product of two tables

    Given the table firstnames
      | firstname |
      | TEXT      |
      | John      |
      | Ethan     |

    And the table lastnames
      | lastname |
      | TEXT     |
      | Hunt     |
      | Wick     |
      | Preston  |

    When I execute the query
    """
    SELECT firstname, lastname from firstnames, lastnames
    """

    Then I expect the result set
      | firstname | lastname |
      | TEXT      | TEXT     |
      | John      | Hunt     |
      | John      | Wick     |
      | John      | Preston  |
      | Ethan     | Hunt     |
      | Ethan     | Wick     |
      | Ethan     | Preston  |
