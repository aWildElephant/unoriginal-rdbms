Feature: TPC-H Q10

  Background: TPC-H dataset

    Given I load customer scale factor 1
    And I load lineitem scale factor 1
    And I load nation scale factor 1
    And I load orders scale factor 1

  Scenario: I execute TPC-H Q10

    When I execute the query
    """
    SELECT
      c_custkey,
      c_name,
      SUM(l_extendedprice * (1 - l_discount)) AS revenue,
      c_acctbal,
      n_name,
      c_address,
      c_phone,
      c_comment
    FROM
      customer,
      orders,
      lineitem,
      nation
    WHERE
      c_custkey = o_custkey
      AND l_orderkey = o_orderkey
      AND o_orderdate >= DATE '1993-10-01'
      AND o_orderdate < DATE '1993-10-01' + INTERVAL '3' MONTH
      AND l_returnflag = 'R'
      AND c_nationkey = n_nationkey
    GROUP BY
      c_custkey,
      c_name,
      c_acctbal,
      c_phone,
      n_name,
      c_address,
      c_comment
    ORDER BY
      revenue DESC
    LIMIT 20
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q10.test
    Then I expect the result
      | c_custkey | c_name             | revenue     | c_acctbal | n_name         | c_address                                | c_phone         | c_comment                                                                                                        |
      | INTEGER   | TEXT               | DECIMAL     | DECIMAL   | TEXT           | TEXT                                     | TEXT            | TEXT                                                                                                             |
      | 57040     | Customer#000057040 | 734235.2455 | 632.87    | JAPAN          | Eioyzjf4pp                               | 22-895-641-3466 | sits. slyly regular requests sleep alongside of the regular inst                                                 |
      | 143347    | Customer#000143347 | 721002.6948 | 2557.47   | EGYPT          | 1aReFYv,Kw4                              | 14-742-935-3718 | ggle carefully enticing requests. final deposits use bold, bold pinto beans. ironic, idle re                     |
      | 60838     | Customer#000060838 | 679127.3077 | 2454.77   | BRAZIL         | 64EaJ5vMAHWJlBOxJklpNc2RJiWE             | 12-913-494-9813 | " need to boost against the slyly regular account"                                                               |
      | 101998    | Customer#000101998 | 637029.5667 | 3790.89   | UNITED KINGDOM | 01c9CILnNtfOQYmZj                        | 33-593-865-6378 | ress foxes wake slyly after the bold excuses. ironic platelets are furiously carefully bold theodolites          |
      | 125341    | Customer#000125341 | 633508.0860 | 4983.51   | GERMANY        | S29ODD6bceU8QSuuEJznkNaK                 | 17-582-695-5962 | arefully even depths. blithely even excuses sleep furiously. foxes use except the dependencies. ca               |
      | 25501     | Customer#000025501 | 620269.7849 | 7725.04   | ETHIOPIA       | "  W556MXuoiaYCCZamJI,Rn0B4ACUGdkQ8DZ"   | 15-874-808-6793 | he pending instructions wake carefully at the pinto beans. regular, final instructions along the slyly fina      |
      | 115831    | Customer#000115831 | 596423.8672 | 5098.10   | FRANCE         | rFeBbEEyk dl ne7zV5fDrmiq1oK09wV7pxqCgIc | 16-715-386-3788 | l somas sleep. furiously final deposits wake blithely regular pinto b                                            |
      | 84223     | Customer#000084223 | 594998.0239 | 528.65    | UNITED KINGDOM | nAVZCs6BaWap rrM27N 2qBnzc5WBauxbA       | 33-442-824-8191 | " slyly final deposits haggle regular, pending dependencies. pending escapades wake "                            |
      | 54289     | Customer#000054289 | 585603.3918 | 5583.02   | IRAN           | vXCxoCsU0Bad5JQI ,oobkZ                  | 20-834-292-4707 | ely special foxes are quickly finally ironic p                                                                   |
      | 39922     | Customer#000039922 | 584878.1134 | 7321.11   | GERMANY        | Zgy4s50l2GKN4pLDPBU8m342gIw6R            | 17-147-757-8036 | y final requests. furiously final foxes cajole blithely special platelets. f                                     |
      | 6226      | Customer#000006226 | 576783.7606 | 2230.09   | UNITED KINGDOM | 8gPu8,NPGkfyQQ0hcIYUGPIBWc,ybP5g,        | 33-657-701-3391 | "ending platelets along the express deposits cajole carefully final "                                            |
      | 922       | Customer#000000922 | 576767.5333 | 3869.25   | GERMANY        | Az9RFaut7NkPnc5zSD2PwHgVwr4jRzq          | 17-945-916-9648 | luffily fluffy deposits. packages c                                                                              |
      | 147946    | Customer#000147946 | 576455.1320 | 2030.13   | ALGERIA        | iANyZHjqhyy7Ajah0pTrYyhJ                 | 10-886-956-3143 | ithely ironic deposits haggle blithely ironic requests. quickly regu                                             |
      | 115640    | Customer#000115640 | 569341.1933 | 6436.10   | ARGENTINA      | Vtgfia9qI 7EpHgecU1X                     | 11-411-543-4901 | ost slyly along the patterns; pinto be                                                                           |
      | 73606     | Customer#000073606 | 568656.8578 | 1785.67   | JAPAN          | xuR0Tro5yChDfOCrjkd2ol                   | 22-437-653-6966 | he furiously regular ideas. slowly                                                                               |
      | 110246    | Customer#000110246 | 566842.9815 | 7763.35   | VIETNAM        | 7KzflgX MDOq7sOkI                        | 31-943-426-9837 | egular deposits serve blithely above the fl                                                                      |
      | 142549    | Customer#000142549 | 563537.2368 | 5085.99   | INDONESIA      | ChqEoK43OysjdHbtKCp6dKqjNyvvi9           | 19-955-562-2398 | sleep pending courts. ironic deposits against the carefully unusual platelets cajole carefully express accounts. |
      | 146149    | Customer#000146149 | 557254.9865 | 1791.55   | ROMANIA        | s87fvzFQpU                               | 29-744-164-6487 | " of the slyly silent accounts. quickly final accounts across the "                                              |
      | 52528     | Customer#000052528 | 556397.3509 | 551.79    | ARGENTINA      | NFztyTOR10UOJ                            | 11-208-192-3205 | " deposits hinder. blithely pending asymptotes breach slyly regular re"                                          |
      | 23431     | Customer#000023431 | 554269.5360 | 3381.86   | ROMANIA        | HgiV0phqhaIa9aydNoIlb                    | 29-915-458-2654 | nusual, even instructions: furiously stealthy n                                                                  |


