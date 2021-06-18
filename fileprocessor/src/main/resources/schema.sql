DROP TABLE csvfilemodel IF EXISTS;

CREATE TABLE csvfilemodel  (
    file_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    anzsic06 VARCHAR(20),
    Area VARCHAR(20),
    file_year VARCHAR(20),
    geo_count VARCHAR(20),
    ec_count VARCHAR(20)
);