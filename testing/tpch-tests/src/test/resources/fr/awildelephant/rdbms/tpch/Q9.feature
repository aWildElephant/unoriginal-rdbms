@todo
Feature: TPC-H Q9

  Background: TPC-H dataset

    Given I load lineitem scale factor 1
    And I load nation scale factor 1
    And I load orders scale factor 1
    And I load part scale factor 1
    And I load partsupp scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q9

    When I execute the query
    """
    SELECT
      nation,
      o_year,
      SUM(amount) AS sum_profit
    FROM (
      SELECT
        n_name AS nation,
        EXTRACT(YEAR FROM o_orderdate) AS o_year,
        l_extendedprice * (1 - l_discount) - ps_supplycost * l_quantity AS amount
      FROM
        part,
        supplier,
        lineitem,
        partsupp,
        orders,
        nation
      WHERE
        s_suppkey = l_suppkey
        AND ps_suppkey = l_suppkey
        AND ps_partkey = l_partkey
        AND p_partkey = l_partkey
        AND o_orderkey = l_orderkey
        AND s_nationkey = n_nationkey
        AND p_name LIKE '%green%'
    ) AS profit
    GROUP BY
      nation,
      o_year
    ORDER BY
      nation,
      o_year desc
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q9.test
    Then I expect the result
      | nation         | o_year  | sum_profit    |
      | TEXT           | INTEGER | DECIMAL       |
      | ALGERIA        | 1998    | 27136900.1803 |
      | ALGERIA        | 1997    | 48611833.4962 |
      | ALGERIA        | 1996    | 48285482.6782 |
      | ALGERIA        | 1995    | 44402273.5999 |
      | ALGERIA        | 1994    | 48694008.0668 |
      | ALGERIA        | 1993    | 46044207.7838 |
      | ALGERIA        | 1992    | 45636849.4881 |
      | ARGENTINA      | 1998    | 28341663.7848 |
      | ARGENTINA      | 1997    | 47143964.1176 |
      | ARGENTINA      | 1996    | 45255278.6021 |
      | ARGENTINA      | 1995    | 45631769.2054 |
      | ARGENTINA      | 1994    | 48268856.3547 |
      | ARGENTINA      | 1993    | 48605593.6162 |
      | ARGENTINA      | 1992    | 46654240.7487 |
      | BRAZIL         | 1998    | 26527736.3960 |
      | BRAZIL         | 1997    | 45640660.7677 |
      | BRAZIL         | 1996    | 45090647.1630 |
      | BRAZIL         | 1995    | 44015888.5132 |
      | BRAZIL         | 1994    | 44854218.8932 |
      | BRAZIL         | 1993    | 45766603.7379 |
      | BRAZIL         | 1992    | 45280216.8027 |
      | CANADA         | 1998    | 26828985.3944 |
      | CANADA         | 1997    | 44849954.3186 |
      | CANADA         | 1996    | 46307936.1108 |
      | CANADA         | 1995    | 47311993.0441 |
      | CANADA         | 1994    | 46691491.9596 |
      | CANADA         | 1993    | 46634791.1121 |
      | CANADA         | 1992    | 45873849.6882 |
      | CHINA          | 1998    | 27510180.1657 |
      | CHINA          | 1997    | 46123865.4097 |
      | CHINA          | 1996    | 49532807.0601 |
      | CHINA          | 1995    | 46734651.4838 |
      | CHINA          | 1994    | 46397896.6097 |
      | CHINA          | 1993    | 49634673.9463 |
      | CHINA          | 1992    | 46949457.6426 |
      | EGYPT          | 1998    | 28401491.7968 |
      | EGYPT          | 1997    | 47674857.6783 |
      | EGYPT          | 1996    | 47745727.5450 |
      | EGYPT          | 1995    | 45897160.6783 |
      | EGYPT          | 1994    | 47194895.2280 |
      | EGYPT          | 1993    | 49133627.6471 |
      | EGYPT          | 1992    | 47000574.5027 |
      | ETHIOPIA       | 1998    | 25135046.1377 |
      | ETHIOPIA       | 1997    | 43010596.0838 |
      | ETHIOPIA       | 1996    | 43636287.1922 |
      | ETHIOPIA       | 1995    | 43575757.3343 |
      | ETHIOPIA       | 1994    | 41597208.5283 |
      | ETHIOPIA       | 1993    | 42622804.1616 |
      | ETHIOPIA       | 1992    | 44385735.6813 |
      | FRANCE         | 1998    | 26210392.2804 |
      | FRANCE         | 1997    | 42392969.4731 |
      | FRANCE         | 1996    | 43306317.9749 |
      | FRANCE         | 1995    | 46377408.4328 |
      | FRANCE         | 1994    | 43447352.9922 |
      | FRANCE         | 1993    | 43729961.0639 |
      | FRANCE         | 1992    | 44052308.4290 |
      | GERMANY        | 1998    | 25991257.1071 |
      | GERMANY        | 1997    | 43968355.8079 |
      | GERMANY        | 1996    | 45882074.8049 |
      | GERMANY        | 1995    | 43314338.3077 |
      | GERMANY        | 1994    | 44616995.4369 |
      | GERMANY        | 1993    | 45126645.9113 |
      | GERMANY        | 1992    | 44361141.2107 |
      | INDIA          | 1998    | 29626417.2379 |
      | INDIA          | 1997    | 51386111.3448 |
      | INDIA          | 1996    | 47571018.5122 |
      | INDIA          | 1995    | 49344062.2829 |
      | INDIA          | 1994    | 50106952.4261 |
      | INDIA          | 1993    | 48112766.6987 |
      | INDIA          | 1992    | 47914303.1234 |
      | INDONESIA      | 1998    | 27734909.6763 |
      | INDONESIA      | 1997    | 44593812.9863 |
      | INDONESIA      | 1996    | 44746729.8078 |
      | INDONESIA      | 1995    | 45593622.6993 |
      | INDONESIA      | 1994    | 45988483.8772 |
      | INDONESIA      | 1993    | 46147963.7895 |
      | INDONESIA      | 1992    | 45185777.0688 |
      | IRAN           | 1998    | 26661608.9301 |
      | IRAN           | 1997    | 45019114.1696 |
      | IRAN           | 1996    | 45891397.0992 |
      | IRAN           | 1995    | 44414285.2348 |
      | IRAN           | 1994    | 43696360.4795 |
      | IRAN           | 1993    | 45362775.8094 |
      | IRAN           | 1992    | 43052338.4143 |
      | IRAQ           | 1998    | 31188498.1914 |
      | IRAQ           | 1997    | 48585307.5222 |
      | IRAQ           | 1996    | 50036593.8404 |
      | IRAQ           | 1995    | 48774801.7275 |
      | IRAQ           | 1994    | 48795847.2310 |
      | IRAQ           | 1993    | 47435691.5082 |
      | IRAQ           | 1992    | 47562355.6571 |
      | JAPAN          | 1998    | 24694102.1720 |
      | JAPAN          | 1997    | 42377052.3454 |
      | JAPAN          | 1996    | 40267778.9094 |
      | JAPAN          | 1995    | 40925317.4650 |
      | JAPAN          | 1994    | 41159518.3058 |
      | JAPAN          | 1993    | 39589074.2771 |
      | JAPAN          | 1992    | 39113493.9052 |
      | JORDAN         | 1998    | 23489867.7893 |
      | JORDAN         | 1997    | 41615962.6619 |
      | JORDAN         | 1996    | 41860855.4684 |
      | JORDAN         | 1995    | 39931672.0908 |
      | JORDAN         | 1994    | 40707555.4638 |
      | JORDAN         | 1993    | 39060405.4658 |
      | JORDAN         | 1992    | 41657604.2684 |
      | KENYA          | 1998    | 25566337.4303 |
      | KENYA          | 1997    | 43108847.9024 |
      | KENYA          | 1996    | 43482953.5430 |
      | KENYA          | 1995    | 42517988.9814 |
      | KENYA          | 1994    | 43612479.4523 |
      | KENYA          | 1993    | 42724038.7571 |
      | KENYA          | 1992    | 43217106.2068 |
      | MOROCCO        | 1998    | 24915496.8756 |
      | MOROCCO        | 1997    | 42698382.8550 |
      | MOROCCO        | 1996    | 42986113.5049 |
      | MOROCCO        | 1995    | 42316089.1593 |
      | MOROCCO        | 1994    | 43458604.6029 |
      | MOROCCO        | 1993    | 42672288.0699 |
      | MOROCCO        | 1992    | 42800781.6415 |
      | MOZAMBIQUE     | 1998    | 28279876.0301 |
      | MOZAMBIQUE     | 1997    | 51159216.2298 |
      | MOZAMBIQUE     | 1996    | 48072525.0645 |
      | MOZAMBIQUE     | 1995    | 48905200.6007 |
      | MOZAMBIQUE     | 1994    | 46092076.2805 |
      | MOZAMBIQUE     | 1993    | 48555926.2669 |
      | MOZAMBIQUE     | 1992    | 47809075.1192 |
      | PERU           | 1998    | 26713966.2678 |
      | PERU           | 1997    | 48324008.6011 |
      | PERU           | 1996    | 50310008.8629 |
      | PERU           | 1995    | 49647080.9629 |
      | PERU           | 1994    | 46420910.2773 |
      | PERU           | 1993    | 51536906.2487 |
      | PERU           | 1992    | 47711665.3137 |
      | ROMANIA        | 1998    | 27271993.1010 |
      | ROMANIA        | 1997    | 45063059.1953 |
      | ROMANIA        | 1996    | 47492335.0323 |
      | ROMANIA        | 1995    | 45710636.2909 |
      | ROMANIA        | 1994    | 46088041.1066 |
      | ROMANIA        | 1993    | 47515092.5613 |
      | ROMANIA        | 1992    | 44111439.8044 |
      | RUSSIA         | 1998    | 27935323.7271 |
      | RUSSIA         | 1997    | 48222347.2924 |
      | RUSSIA         | 1996    | 47553559.4932 |
      | RUSSIA         | 1995    | 46755990.0976 |
      | RUSSIA         | 1994    | 48000515.6191 |
      | RUSSIA         | 1993    | 48569624.5082 |
      | RUSSIA         | 1992    | 47672831.5329 |
      | SAUDI ARABIA   | 1998    | 27113516.8424 |
      | SAUDI ARABIA   | 1997    | 46690468.9649 |
      | SAUDI ARABIA   | 1996    | 47775782.6670 |
      | SAUDI ARABIA   | 1995    | 46657107.8287 |
      | SAUDI ARABIA   | 1994    | 48181672.8100 |
      | SAUDI ARABIA   | 1993    | 45692556.4438 |
      | SAUDI ARABIA   | 1992    | 48924913.2717 |
      | UNITED KINGDOM | 1998    | 26366682.8786 |
      | UNITED KINGDOM | 1997    | 44518130.1851 |
      | UNITED KINGDOM | 1996    | 45539729.6166 |
      | UNITED KINGDOM | 1995    | 46845879.3390 |
      | UNITED KINGDOM | 1994    | 43081609.5737 |
      | UNITED KINGDOM | 1993    | 44770146.7555 |
      | UNITED KINGDOM | 1992    | 44123402.5484 |
      | UNITED STATES  | 1998    | 27826593.6825 |
      | UNITED STATES  | 1997    | 46638572.3648 |
      | UNITED STATES  | 1996    | 46688280.5474 |
      | UNITED STATES  | 1995    | 48951591.6156 |
      | UNITED STATES  | 1994    | 45099092.0598 |
      | UNITED STATES  | 1993    | 46181600.5278 |
      | UNITED STATES  | 1992    | 46168214.0901 |
      | VIETNAM        | 1998    | 27281931.0011 |
      | VIETNAM        | 1997    | 48735914.1796 |
      | VIETNAM        | 1996    | 47824595.9040 |
      | VIETNAM        | 1995    | 48235135.8016 |
      | VIETNAM        | 1994    | 47729256.3324 |
      | VIETNAM        | 1993    | 45352676.8672 |
      | VIETNAM        | 1992    | 47846355.6485 |