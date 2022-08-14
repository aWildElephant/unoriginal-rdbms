Feature: Insert into ... select

  Scenario: I insert into a table from another table

    Given the table source
      | s1      | s2      |
      | INTEGER | TEXT    |
      | 3       | and     |
      | 4       | friends |

    Given the table destination
      | d1      | d2    |
      | INTEGER | TEXT  |
      | 1       | hello |
      | 2       | world |

    When I execute the query
    """
    INSERT INTO destination SELECT * FROM source
    """

    Then table destination should be
      | d1      | d2      |
      | INTEGER | TEXT    |
      | 1       | hello   |
      | 2       | world   |
      | 3       | and     |
      | 4       | friends |
