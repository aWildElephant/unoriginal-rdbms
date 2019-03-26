CREATE TABLE region (
    r_regionkey INTEGER NOT NULL,
    r_name CHAR(25) NOT NULL,
    r_comment VARCHAR(152) NOT NULL,
    UNIQUE(r_regionkey)
);
