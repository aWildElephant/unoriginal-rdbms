Feature: Explain

  Scenario: I explain a simple values query

    When I execute the query
    """
    EXPLAIN VALUES (1)
    """

    Then I expect the plan
    """
    {
      "type": "constructor",
      "matrix": [[{
          "type": "constant",
          "value": "IntegerValue[1]"
        }
        ]
      ]
    }
    """