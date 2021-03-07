@todo # Broken by subquery unnesting refactoring
Feature: TPC-H Q2

  Background: TPC-H dataset

    Given I load nation scale factor 1
    And I load part scale factor 1
    And I load partsupp scale factor 1
    And I load region scale factor 1
    And I load supplier scale factor 1

  Scenario: I execute TPC-H Q2

    When I execute the query
    """
    SELECT
      s_acctbal,
      s_name,
      n_name,
      p_partkey,
      p_mfgr,
      s_address,
      s_phone,
      s_comment
    FROM
      part,
      supplier,
      partsupp,
      nation,
      region
    WHERE
      p_partkey = ps_partkey
      AND s_suppkey = ps_suppkey
      AND p_size = 15
      AND p_type LIKE '%BRASS'
      AND s_nationkey = n_nationkey
      AND n_regionkey = r_regionkey
      AND r_name = 'EUROPE'
      AND ps_supplycost = (
        SELECT
          min(ps_supplycost)
        FROM
          partsupp,
          supplier,
          nation,
          region
        WHERE
          p_partkey = ps_partkey
          AND s_suppkey = ps_suppkey
          AND s_nationkey = n_nationkey
          AND n_regionkey = r_regionkey
          AND r_name = 'EUROPE'
      )
    ORDER BY
      s_acctbal desc,
      n_name,
      s_name,
      p_partkey
    LIMIT 100;
    """

    # Retrieved from https://github.com/apache/impala/blob/master/testdata/workloads/tpch/queries/tpch-q2.test
    Then I expect the result
      | s_acctbal | s_name             | n_name         | p_partkey | p_mfgr         | s_address                                | s_phone         | s_comment                                                                                            |
      | DECIMAL   | TEXT               | TEXT           | INTEGER   | TEXT           | TEXT                                     | TEXT            | TEXT                                                                                                 |
      | 9938.53   | Supplier#000005359 | UNITED KINGDOM | 185358    | Manufacturer#4 | QKuHYh,vZGiwu2FWEJoLDx04                 | 33-429-790-6131 | uriously regular requests hag                                                                        |
      | 9937.84   | Supplier#000005969 | ROMANIA        | 108438    | Manufacturer#1 | ANDENSOSmk,miq23Xfb5RWt6dvUcvt6Qa        | 29-520-692-3537 | efully express instructions. regular requests against the slyly fin                                  |
      | 9936.22   | Supplier#000005250 | UNITED KINGDOM | 249       | Manufacturer#4 | B3rqp0xbSEim4Mpy2RH J                    | 33-320-228-2957 | etect about the furiously final accounts. slyly ironic pinto beans sleep inside the furiously        |
      | 9923.77   | Supplier#000002324 | GERMANY        | 29821     | Manufacturer#4 | y3OD9UywSTOk                             | 17-779-299-1839 | ackages boost blithely. blithely regular deposits c                                                  |
      | 9871.22   | Supplier#000006373 | GERMANY        | 43868     | Manufacturer#5 | J8fcXWsTqM                               | 17-813-485-8637 | etect blithely bold asymptotes. fluffily ironic platelets wake furiously; blit                       |
      | 9870.78   | Supplier#000001286 | GERMANY        | 81285     | Manufacturer#2 | YKA,E2fjiVd7eUrzp2Ef8j1QxGo2DFnosaTEH    | 17-516-924-4574 | " regular accounts. furiously unusual courts above the fi"                                           |
      | 9870.78   | Supplier#000001286 | GERMANY        | 181285    | Manufacturer#4 | YKA,E2fjiVd7eUrzp2Ef8j1QxGo2DFnosaTEH    | 17-516-924-4574 | " regular accounts. furiously unusual courts above the fi"                                           |
      | 9852.52   | Supplier#000008973 | RUSSIA         | 18972     | Manufacturer#2 | t5L67YdBYYH6o,Vz24jpDyQ9                 | 32-188-594-7038 | rns wake final foxes. carefully unusual depende                                                      |
      | 9847.83   | Supplier#000008097 | RUSSIA         | 130557    | Manufacturer#2 | xMe97bpE69NzdwLoX                        | 32-375-640-3593 | " the special excuses. silent sentiments serve carefully final ac"                                   |
      | 9847.57   | Supplier#000006345 | FRANCE         | 86344     | Manufacturer#1 | VSt3rzk3qG698u6ld8HhOByvrTcSTSvQlDQDag   | 16-886-766-7945 | ges. slyly regular requests are. ruthless, express excuses cajole blithely across the unu            |
      | 9847.57   | Supplier#000006345 | FRANCE         | 173827    | Manufacturer#2 | VSt3rzk3qG698u6ld8HhOByvrTcSTSvQlDQDag   | 16-886-766-7945 | ges. slyly regular requests are. ruthless, express excuses cajole blithely across the unu            |
      | 9836.93   | Supplier#000007342 | RUSSIA         | 4841      | Manufacturer#4 | JOlK7C1,7xrEZSSOw                        | 32-399-414-5385 | blithely carefully bold theodolites. fur                                                             |
      | 9817.10   | Supplier#000002352 | RUSSIA         | 124815    | Manufacturer#2 | 4LfoHUZjgjEbAKw TgdKcgOc4D4uCYw          | 32-551-831-1437 | wake carefully alongside of the carefully final ex                                                   |
      | 9817.10   | Supplier#000002352 | RUSSIA         | 152351    | Manufacturer#3 | 4LfoHUZjgjEbAKw TgdKcgOc4D4uCYw          | 32-551-831-1437 | wake carefully alongside of the carefully final ex                                                   |
      | 9739.86   | Supplier#000003384 | FRANCE         | 138357    | Manufacturer#2 | o,Z3v4POifevE k9U1b 6J1ucX,I             | 16-494-913-5925 | s after the furiously bold packages sleep fluffily idly final requests: quickly final                |
      | 9721.95   | Supplier#000008757 | UNITED KINGDOM | 156241    | Manufacturer#3 | Atg6GnM4dT2                              | 33-821-407-2995 | eep furiously sauternes; quickl                                                                      |
      | 9681.33   | Supplier#000008406 | RUSSIA         | 78405     | Manufacturer#1 | ,qUuXcftUl                               | 32-139-873-8571 | haggle slyly regular excuses. quic                                                                   |
      | 9643.55   | Supplier#000005148 | ROMANIA        | 107617    | Manufacturer#1 | kT4ciVFslx9z4s79p Js825                  | 29-252-617-4850 | final excuses. final ideas boost quickly furiously speci                                             |
      | 9624.82   | Supplier#000001816 | FRANCE         | 34306     | Manufacturer#3 | e7vab91vLJPWxxZnewmnDBpDmxYHrb           | 16-392-237-6726 | e packages are around the special ideas. special, pending foxes us                                   |
      | 9624.78   | Supplier#000009658 | ROMANIA        | 189657    | Manufacturer#1 | oE9uBgEfSS4opIcepXyAYM,x                 | 29-748-876-2014 | ronic asymptotes wake bravely final                                                                  |
      | 9612.94   | Supplier#000003228 | ROMANIA        | 120715    | Manufacturer#2 | KDdpNKN3cWu7ZSrbdqp7AfSLxx,qWB           | 29-325-784-8187 | warhorses. quickly even deposits sublate daringly ironic instructions. slyly blithe t                |
      | 9612.94   | Supplier#000003228 | ROMANIA        | 198189    | Manufacturer#4 | KDdpNKN3cWu7ZSrbdqp7AfSLxx,qWB           | 29-325-784-8187 | warhorses. quickly even deposits sublate daringly ironic instructions. slyly blithe t                |
      | 9571.83   | Supplier#000004305 | ROMANIA        | 179270    | Manufacturer#2 | qNHZ7WmCzygwMPRDO9Ps                     | 29-973-481-1831 | kly carefully express asymptotes. furiou                                                             |
      | 9558.10   | Supplier#000003532 | UNITED KINGDOM | 88515     | Manufacturer#4 | EOeuiiOn21OVpTlGguufFDFsbN1p0lhpxHp      | 33-152-301-2164 | " foxes. quickly even excuses use. slyly special foxes nag bl"                                       |
      | 9492.79   | Supplier#000005975 | GERMANY        | 25974     | Manufacturer#5 | S6mIiCTx82z7lV                           | 17-992-579-4839 | arefully pending accounts. blithely regular excuses boost carefully carefully ironic p               |
      | 9461.05   | Supplier#000002536 | UNITED KINGDOM | 20033     | Manufacturer#1 | 8mmGbyzaU 7ZS2wJumTibypncu9pNkDc4FYA     | 33-556-973-5522 | . slyly regular deposits wake slyly. furiously regular warthogs are.                                 |
      | 9453.01   | Supplier#000000802 | ROMANIA        | 175767    | Manufacturer#1 | ,6HYXb4uaHITmtMBj4Ak57Pd                 | 29-342-882-6463 | gular frets. permanently special multipliers believe blithely alongs                                 |
      | 9408.65   | Supplier#000007772 | UNITED KINGDOM | 117771    | Manufacturer#4 | AiC5YAH,gdu0i7                           | 33-152-491-1126 | nag against the final requests. furiously unusual packages cajole blit                               |
      | 9359.61   | Supplier#000004856 | ROMANIA        | 62349     | Manufacturer#5 | HYogcF3Jb yh1                            | 29-334-870-9731 | y ironic theodolites. blithely sile                                                                  |
      | 9357.45   | Supplier#000006188 | UNITED KINGDOM | 138648    | Manufacturer#1 | g801,ssP8wpTk4Hm                         | 33-583-607-1633 | ously always regular packages. fluffily even accounts beneath the furiously final pack               |
      | 9352.04   | Supplier#000003439 | GERMANY        | 170921    | Manufacturer#4 | qYPDgoiBGhCYxjgC                         | 17-128-996-4650 | " according to the carefully bold ideas"                                                             |
      | 9312.97   | Supplier#000007807 | RUSSIA         | 90279     | Manufacturer#5 | oGYMPCk9XHGB2PBfKRnHA                    | 32-673-872-5854 | ecial packages among the pending, even requests use regula                                           |
      | 9312.97   | Supplier#000007807 | RUSSIA         | 100276    | Manufacturer#5 | oGYMPCk9XHGB2PBfKRnHA                    | 32-673-872-5854 | ecial packages among the pending, even requests use regula                                           |
      | 9280.27   | Supplier#000007194 | ROMANIA        | 47193     | Manufacturer#3 | zhRUQkBSrFYxIAXTfInj vyGRQjeK            | 29-318-454-2133 | o beans haggle after the furiously unusual deposits. carefully silent dolphins cajole carefully      |
      | 9274.80   | Supplier#000008854 | RUSSIA         | 76346     | Manufacturer#3 | 1xhLoOUM7I3mZ1mKnerw OSqdbb4QbGa         | 32-524-148-5221 | y. courts do wake slyly. carefully ironic platelets haggle above the slyly regular the               |
      | 9249.35   | Supplier#000003973 | FRANCE         | 26466     | Manufacturer#1 | d18GiDsL6Wm2IsGXM,RZf1jCsgZAOjNYVThTRP4  | 16-722-866-1658 | uests are furiously. regular tithes through the regular, final accounts cajole furiously above the q |
      | 9249.35   | Supplier#000003973 | FRANCE         | 33972     | Manufacturer#1 | d18GiDsL6Wm2IsGXM,RZf1jCsgZAOjNYVThTRP4  | 16-722-866-1658 | uests are furiously. regular tithes through the regular, final accounts cajole furiously above the q |
      | 9208.70   | Supplier#000007769 | ROMANIA        | 40256     | Manufacturer#5 | rsimdze 5o9P Ht7xS                       | 29-964-424-9649 | "lites was quickly above the furiously ironic requests. slyly even foxes against the blithely bold " |
      | 9201.47   | Supplier#000009690 | UNITED KINGDOM | 67183     | Manufacturer#5 | CB BnUTlmi5zdeEl7R7                      | 33-121-267-9529 | e even, even foxes. blithely ironic packages cajole regular packages. slyly final ide                |
      | 9192.10   | Supplier#000000115 | UNITED KINGDOM | 85098     | Manufacturer#3 | nJ 2t0f7Ve,wL1,6WzGBJLNBUCKlsV           | 33-597-248-1220 | es across the carefully express accounts boost caref                                                 |
      | 9189.98   | Supplier#000001226 | GERMANY        | 21225     | Manufacturer#4 | qsLCqSvLyZfuXIpjz                        | 17-725-903-1381 | " deposits. blithely bold excuses about the slyly bold forges wake "                                 |
      | 9128.97   | Supplier#000004311 | RUSSIA         | 146768    | Manufacturer#5 | I8IjnXd7NSJRs594RxsRR0                   | 32-155-440-7120 | "refully. blithely unusual asymptotes haggle "                                                       |
      | 9104.83   | Supplier#000008520 | GERMANY        | 150974    | Manufacturer#4 | RqRVDgD0ER J9 b41vR2,3                   | 17-728-804-1793 | ly about the blithely ironic depths. slyly final theodolites among the fluffily bold ideas print     |
      | 9101.00   | Supplier#000005791 | ROMANIA        | 128254    | Manufacturer#5 | zub2zCV,jhHPPQqi,P2INAjE1zI n66cOEoXFG   | 29-549-251-5384 | ts. notornis detect blithely above the carefully bold requests. blithely even package                |
      | 9094.57   | Supplier#000004582 | RUSSIA         | 39575     | Manufacturer#1 | WB0XkCSG3r,mnQ n,h9VIxjjr9ARHFvKgMDf     | 32-587-577-1351 | "jole. regular accounts sleep blithely frets. final pinto beans play furiously past the "            |
      | 8996.87   | Supplier#000004702 | FRANCE         | 102191    | Manufacturer#5 | 8XVcQK23akp                              | 16-811-269-8946 | ickly final packages along the express plat                                                          |
      | 8996.14   | Supplier#000009814 | ROMANIA        | 139813    | Manufacturer#2 | af0O5pg83lPU4IDVmEylXZVqYZQzSDlYLAmR     | 29-995-571-8781 | " dependencies boost quickly across the furiously pending requests! unusual dolphins play sl"        |
      | 8968.42   | Supplier#000010000 | ROMANIA        | 119999    | Manufacturer#5 | aTGLEusCiL4F PDBdv665XBJhPyCOB0i         | 29-578-432-2146 | ly regular foxes boost slyly. quickly special waters boost carefully ironi                           |
      | 8936.82   | Supplier#000007043 | UNITED KINGDOM | 109512    | Manufacturer#1 | FVajceZInZdbJE6Z9XsRUxrUEpiwHDrOXi,1Rz   | 33-784-177-8208 | efully regular courts. furiousl                                                                      |
      | 8929.42   | Supplier#000008770 | FRANCE         | 173735    | Manufacturer#4 | R7cG26TtXrHAP9 HckhfRi                   | 16-242-746-9248 | "cajole furiously unusual requests. quickly stealthy requests are. "                                 |
      | 8920.59   | Supplier#000003967 | ROMANIA        | 26460     | Manufacturer#1 | eHoAXe62SY9                              | 29-194-731-3944 | aters. express, pending instructions sleep. brave, r                                                 |
      | 8920.59   | Supplier#000003967 | ROMANIA        | 173966    | Manufacturer#2 | eHoAXe62SY9                              | 29-194-731-3944 | aters. express, pending instructions sleep. brave, r                                                 |
      | 8913.96   | Supplier#000004603 | UNITED KINGDOM | 137063    | Manufacturer#2 | OUzlvMUr7n,utLxmPNeYKSf3T24OXskxB5       | 33-789-255-7342 | " haggle slyly above the furiously regular pinto beans. even "                                       |
      | 8877.82   | Supplier#000007967 | FRANCE         | 167966    | Manufacturer#5 | A3pi1BARM4nx6R,qrwFoRPU                  | 16-442-147-9345 | ously foxes. express, ironic requests im                                                             |
      | 8862.24   | Supplier#000003323 | ROMANIA        | 73322     | Manufacturer#3 | W9 lYcsC9FwBqk3ItL                       | 29-736-951-3710 | ly pending ideas sleep about the furiously unu                                                       |
      | 8841.59   | Supplier#000005750 | ROMANIA        | 100729    | Manufacturer#5 | Erx3lAgu0g62iaHF9x50uMH4EgeN9hEG         | 29-344-502-5481 | gainst the pinto beans. fluffily unusual dependencies affix slyly even deposits.                     |
      | 8781.71   | Supplier#000003121 | ROMANIA        | 13120     | Manufacturer#5 | wNqTogx238ZYCamFb,50v,bj 4IbNFW9Bvw1xP   | 29-707-291-5144 | s wake quickly ironic ideas                                                                          |
      | 8754.24   | Supplier#000009407 | UNITED KINGDOM | 179406    | Manufacturer#4 | CHRCbkaWcf5B                             | 33-903-970-9604 | e ironic requests. carefully even foxes above the furious                                            |
      | 8691.06   | Supplier#000004429 | UNITED KINGDOM | 126892    | Manufacturer#2 | k,BQms5UhoAF1B2Asi,fLib                  | 33-964-337-5038 | "efully express deposits kindle after the deposits. final "                                          |
      | 8655.99   | Supplier#000006330 | RUSSIA         | 193810    | Manufacturer#2 | UozlaENr0ytKe2w6CeIEWFWn iO3S8Rae7Ou     | 32-561-198-3705 | symptotes use about the express dolphins. requests use after the express platelets. final, ex        |
      | 8638.36   | Supplier#000002920 | RUSSIA         | 75398     | Manufacturer#1 | Je2a8bszf3L                              | 32-122-621-7549 | ly quickly ironic requests. even requests whithout t                                                 |
      | 8638.36   | Supplier#000002920 | RUSSIA         | 170402    | Manufacturer#3 | Je2a8bszf3L                              | 32-122-621-7549 | ly quickly ironic requests. even requests whithout t                                                 |
      | 8607.69   | Supplier#000006003 | UNITED KINGDOM | 76002     | Manufacturer#2 | EH9wADcEiuenM0NR08zDwMidw,52Y2RyILEiA    | 33-416-807-5206 | ar, pending accounts. pending depende                                                                |
      | 8569.52   | Supplier#000005936 | RUSSIA         | 5935      | Manufacturer#5 | jXaNZ6vwnEWJ2ksLZJpjtgt0bY2a3AU          | 32-644-251-7916 | ". regular foxes nag carefully atop the regular, silent deposits. quickly regular packages "         |
      | 8564.12   | Supplier#000000033 | GERMANY        | 110032    | Manufacturer#1 | gfeKpYw3400L0SDywXA6Ya1Qmq1w6YB9f3R      | 17-138-897-9374 | "n sauternes along the regular asymptotes are regularly along the "                                  |
      | 8553.82   | Supplier#000003979 | ROMANIA        | 143978    | Manufacturer#4 | BfmVhCAnCMY3jzpjUMy4CNWs9 HzpdQR7INJU    | 29-124-646-4897 | ic requests wake against the blithely unusual accounts. fluffily r                                   |
      | 8517.23   | Supplier#000009529 | RUSSIA         | 37025     | Manufacturer#5 | e44R8o7JAIS9iMcr                         | 32-565-297-8775 | "ove the even courts. furiously special platelets "                                                  |
      | 8517.23   | Supplier#000009529 | RUSSIA         | 59528     | Manufacturer#2 | e44R8o7JAIS9iMcr                         | 32-565-297-8775 | "ove the even courts. furiously special platelets "                                                  |
      | 8503.70   | Supplier#000006830 | RUSSIA         | 44325     | Manufacturer#4 | BC4WFCYRUZyaIgchU 4S                     | 32-147-878-5069 | pades cajole. furious packages among the carefully express excuses boost furiously across th         |
      | 8457.09   | Supplier#000009456 | UNITED KINGDOM | 19455     | Manufacturer#1 | 7SBhZs8gP1cJjT0Qf433YBk                  | 33-858-440-4349 | cing requests along the furiously unusual deposits promise among the furiously unus                  |
      | 8441.40   | Supplier#000003817 | FRANCE         | 141302    | Manufacturer#2 | hU3fz3xL78                               | 16-339-356-5115 | ely even ideas. ideas wake slyly furiously unusual instructions. pinto beans sleep ag                |
      | 8432.89   | Supplier#000003990 | RUSSIA         | 191470    | Manufacturer#1 | wehBBp1RQbfxAYDASS75MsywmsKHRVdkrvNe6m   | 32-839-509-9301 | ep furiously. packages should have to haggle slyly across the deposits. furiously regu               |
      | 8431.40   | Supplier#000002675 | ROMANIA        | 5174      | Manufacturer#1 | HJFStOu9R5NGPOegKhgbzBdyvrG2yh8w         | 29-474-643-1443 | ithely express pinto beans. blithely even foxes haggle. furiously regular theodol                    |
      | 8407.04   | Supplier#000005406 | RUSSIA         | 162889    | Manufacturer#4 | j7 gYF5RW8DC5UrjKC                       | 32-626-152-4621 | r the blithely regular packages. slyly ironic theodoli                                               |
      | 8386.08   | Supplier#000008518 | FRANCE         | 36014     | Manufacturer#3 | 2jqzqqAVe9crMVGP,n9nTsQXulNLTUYoJjEDcqWV | 16-618-780-7481 | blithely bold pains are carefully platelets. finally regular pinto beans sleep carefully special     |
      | 8376.52   | Supplier#000005306 | UNITED KINGDOM | 190267    | Manufacturer#5 | 9t8Y8 QqSIsoADPt6NLdk,TP5zyRx41oBUlgoGc9 | 33-632-514-7931 | ly final accounts sleep special, regular requests. furiously regular                                 |
      | 8348.74   | Supplier#000008851 | FRANCE         | 66344     | Manufacturer#4 | nWxi7GwEbjhw1                            | 16-796-240-2472 | " boldly final deposits. regular, even instructions detect slyly. fluffily unusual pinto bea"        |
      | 8338.58   | Supplier#000007269 | FRANCE         | 17268     | Manufacturer#4 | ZwhJSwABUoiB04,3                         | 16-267-277-4365 | iously final accounts. even pinto beans cajole slyly regular                                         |
      | 8328.46   | Supplier#000001744 | ROMANIA        | 69237     | Manufacturer#5 | oLo3fV64q2,FKHa3p,qHnS7Yzv,ps8           | 29-330-728-5873 | ep carefully-- even, careful packages are slyly along t                                              |
      | 8307.93   | Supplier#000003142 | GERMANY        | 18139     | Manufacturer#1 | dqblvV8dCNAorGlJ                         | 17-595-447-6026 | "olites wake furiously regular decoys. final requests nod "                                          |
      | 8231.61   | Supplier#000009558 | RUSSIA         | 192000    | Manufacturer#2 | mcdgen,yT1iJDHDS5fV                      | 32-762-137-5858 | " foxes according to the furi"                                                                       |
      | 8152.61   | Supplier#000002731 | ROMANIA        | 15227     | Manufacturer#4 | " nluXJCuY1tu"                           | 29-805-463-2030 | " special requests. even, regular warhorses affix among the final gr"                                |
      | 8109.09   | Supplier#000009186 | FRANCE         | 99185     | Manufacturer#1 | wgfosrVPexl9pEXWywaqlBMDYYf              | 16-668-570-1402 | tions haggle slyly about the sil                                                                     |
      | 8102.62   | Supplier#000003347 | UNITED KINGDOM | 18344     | Manufacturer#5 | m CtXS2S16i                              | 33-454-274-8532 | egrate with the slyly bold instructions. special foxes haggle silently among the                     |
      | 8046.07   | Supplier#000008780 | FRANCE         | 191222    | Manufacturer#3 | AczzuE0UK9osj ,Lx0Jmh                    | 16-473-215-6395 | onic platelets cajole after the regular instructions. permanently bold excuses                       |
      | 8042.09   | Supplier#000003245 | RUSSIA         | 135705    | Manufacturer#4 | Dh8Ikg39onrbOL4DyTfGw8a9oKUX3d9Y         | 32-836-132-8872 | osits. packages cajole slyly. furiously regular deposits cajole slyly. q                             |
      | 8042.09   | Supplier#000003245 | RUSSIA         | 150729    | Manufacturer#1 | Dh8Ikg39onrbOL4DyTfGw8a9oKUX3d9Y         | 32-836-132-8872 | osits. packages cajole slyly. furiously regular deposits cajole slyly. q                             |
      | 7992.40   | Supplier#000006108 | FRANCE         | 118574    | Manufacturer#1 | 8tBydnTDwUqfBfFV4l3                      | 16-974-998-8937 | " ironic ideas? fluffily even instructions wake. blithel"                                            |
      | 7980.65   | Supplier#000001288 | FRANCE         | 13784     | Manufacturer#4 | zE,7HgVPrCn                              | 16-646-464-8247 | ully bold courts. escapades nag slyly. furiously fluffy theodo                                       |
      | 7950.37   | Supplier#000008101 | GERMANY        | 33094     | Manufacturer#5 | kkYvL6IuvojJgTNG IKkaXQDYgx8ILohj        | 17-627-663-8014 | "arefully unusual requests x-ray above the quickly final deposits. "                                 |
      | 7937.93   | Supplier#000009012 | ROMANIA        | 83995     | Manufacturer#2 | iUiTziH,Ek3i4lwSgunXMgrcTzwdb            | 29-250-925-9690 | to the blithely ironic deposits nag sly                                                              |
      | 7914.45   | Supplier#000001013 | RUSSIA         | 125988    | Manufacturer#2 | riRcntps4KEDtYScjpMIWeYF6mNnR            | 32-194-698-3365 | " busily bold packages are dolphi"                                                                   |
      | 7912.91   | Supplier#000004211 | GERMANY        | 159180    | Manufacturer#5 | 2wQRVovHrm3,v03IKzfTd,1PYsFXQFFOG        | 17-266-947-7315 | ay furiously regular platelets. cou                                                                  |
      | 7912.91   | Supplier#000004211 | GERMANY        | 184210    | Manufacturer#4 | 2wQRVovHrm3,v03IKzfTd,1PYsFXQFFOG        | 17-266-947-7315 | ay furiously regular platelets. cou                                                                  |
      | 7894.56   | Supplier#000007981 | GERMANY        | 85472     | Manufacturer#4 | NSJ96vMROAbeXP                           | 17-963-404-3760 | ic platelets affix after the furiously                                                               |
      | 7887.08   | Supplier#000009792 | GERMANY        | 164759    | Manufacturer#3 | Y28ITVeYriT3kIGdV2K8fSZ V2UqT5H1Otz      | 17-988-938-4296 | ckly around the carefully fluffy theodolites. slyly ironic pack                                      |
      | 7871.50   | Supplier#000007206 | RUSSIA         | 104695    | Manufacturer#1 | 3w fNCnrVmvJjE95sgWZzvW                  | 32-432-452-7731 | ironic requests. furiously final theodolites cajole. final, express packages sleep. quickly reg      |
      | 7852.45   | Supplier#000005864 | RUSSIA         | 8363      | Manufacturer#4 | WCNfBPZeSXh3h,c                          | 32-454-883-3821 | usly unusual pinto beans. brave ideas sleep carefully quickly ironi                                  |
      | 7850.66   | Supplier#000001518 | UNITED KINGDOM | 86501     | Manufacturer#1 | ONda3YJiHKJOC                            | 33-730-383-3892 | ifts haggle fluffily pending pai                                                                     |
      | 7843.52   | Supplier#000006683 | FRANCE         | 11680     | Manufacturer#4 | 2Z0JGkiv01Y00oCFwUGfviIbhzCdy            | 16-464-517-8943 | " express, final pinto beans x-ray slyly asymptotes. unusual, unusual"                               |
