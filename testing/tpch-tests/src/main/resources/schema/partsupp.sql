CREATE TABLE partsupp (
    ps_partkey INTEGER NOT NULL,
    ps_suppkey INTEGER NOT NULL,
    ps_availqty INTEGER NOT NULL,
    ps_supplycost DECIMAL NOT NULL,
    ps_comment VARCHAR(199) NOT NULL,
    UNIQUE(ps_partkey, ps_suppkey)
);
