-- Constraints are optional, but must be enforced if specified (1.2.4) => I don't *need* to have FK/PK, and unique/not null are enforced so all good.
CREATE TABLE part (
    p_partkey INTEGER NOT NULL,
    p_name VARCHAR(55) NOT NULL,
    p_mfgr CHAR(25) NOT NULL,
    p_brand CHAR(10) NOT NULL,
    p_type VARCHAR(25) NOT NULL,
    p_size INTEGER NOT NULL,
    p_container CHAR(10) NOT NULL,
    p_retailprice DECIMAL NOT NULL,
    p_comment VARCHAR(23) NOT NULL,
    UNIQUE (p_partkey)
);
CREATE TABLE supplier (
    s_suppkey INTEGER NOT NULL,
    s_name CHAR(25) NOT NULL,
    s_address VARCHAR(40) NOT NULL,
    s_nationkey INTEGER NOT NULL,
    s_phone CHAR(15) NOT NULL,
    s_acctbal DECIMAL NOT NULL,
    s_comment VARCHAR(101) NOT NULL,
    UNIQUE(s_suppkey)
);
CREATE TABLE partsupp (
    ps_partkey INTEGER NOT NULL,
    ps_suppkey INTEGER NOT NULL,
    ps_availqty INTEGER NOT NULL,
    ps_supplycost DECIMAL NOT NULL,
    ps_comment VARCHAR(199) NOT NULL,
    UNIQUE(ps_partkey, ps_suppkey)
);
CREATE TABLE customer (
    c_custkey INTEGER NOT NULL,
    c_name VARCHAR(25) NOT NULL,
    c_address VARCHAR(40) NOT NULL,
    c_nationkey INTEGER NOT NULL
    c_phone CHAR(15) NOT NULL,
    c_acctbal DECIMAL NOT NULL,
    c_mktsegment CHAR(10) NOT NULL,
    c_comment VARCHAR(117) NOT NULL,
    UNIQUE(c_custkey)
);
CREATE TABLE orders (
    O_ORDERKEY INTEGER NOT NULL,
    O_CUSTKEY INTEGER NOT NULL,
    O_ORDERSTATUS CHAR(1) NOT NULL,
    O_TOTALPRICE DECIMAL NOT NULL,
    O_ORDERDATE DATE NOT NULL,
    O_ORDERPRIORITY CHAR(15) NOT NULL,
    O_CLERK CHAR(15) NOT NULL,
    O_SHIPPRIORITY INTEGER NOT NULL,
    O_COMMENT VARCHAR(7) NOT NULL,
    UNIQUE(o_orderkey)
);
CREATE TABLE lineitem (
    l_orderkey INTEGER NOT NULL,
    l_partkey INTEGER NOT NULL,
    l_suppkey INTEGER NOT NULL,
    l_linenumber INTEGER NOT NULL,
    l_quantity DECIMAL NOT NULL,
    l_extendedprice DECIMAL NOT NULL,
    l_discount DECIMAL NOT NULL,
    l_tax DECIMAL NOT NULL,
    l_returnflag CHAR(1) NOT NULL,
    l_linestatus CHAR(1) NOT NULL,
    l_shipdate DATE NOT NULL,
    l_commitdate DATE NOT NULL,
    l_receiptdate DATE NOT NULL,
    l_shipinstruct CHAR(25) NOT NULL,
    l_shipmode CHAR(10) NOT NULL,
    l_comment VARCHAR(44) NOT NULL,
    UNIQUE(l_orderkey, l_linenumber)
);
CREATE TABLE nation (
    n_nationkey INTEGER NOT NULL,
    n_name CHAR(25) NOT NULL,
    n_regionkey INTEGER NOT NULL,
    n_comment VARCHAR(152) NOT NULL,
    UNIQUE(n_nationkey)
);
CREATE TABLE region (
    r_regionkey INTEGER NOT NULL,
    r_name CHAR(25) NOT NULL,
    r_comment VARCHAR(152) NOT NULL,
    UNIQUE(r_regionkey)
);