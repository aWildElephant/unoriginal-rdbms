CREATE TABLE part (
    p_partkey INTEGER NOT NULL,
    p_name TEXT,
    p_mfgr TEXT,
    p_brand TEXT,
    p_size INTEGER,
    p_container TEXT,
    p_retailprice DECIMAL,
    p_comment TEXT,
    UNIQUE (p_partkey)
);