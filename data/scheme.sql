DROP DATABASE IF EXISTS bgm_db;
CREATE DATABASE bgm_db;
USE bgm_db;


CREATE TABLE platform (
  id   INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200)
);


CREATE TABLE catalog (
  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  platform_id INT,
  name        VARCHAR(200),
  royalty     DECIMAL(6, 3),
  copyright   VARCHAR(50),
  tracks      INT,
  artists     INT
);

CREATE TABLE composition (
  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  catalog_id  INT NOT NULL,
  code        VARCHAR(20),
  name        VARCHAR(400),
  artist      VARCHAR(400),
  composer    VARCHAR(400),
  shareMobile DECIMAL(6, 3),
  sharePublic DECIMAL(6, 3)
);

CREATE INDEX code_index ON composition (code) USING BTREE;


CREATE TABLE customer (
  id         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(200),
  right_type VARCHAR(50),
  royalty    DECIMAL(6, 3),
  contract   VARCHAR(100)
);


CREATE TABLE customer_report (
  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_id INT,
  start_date  DATE,
  upload_date DATE,
  type        INT,
  period      INT
);


CREATE TABLE customer_report_item (
  id             INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  report_id      INT,
  composition_id INT,
  name           VARCHAR(200),
  artist         VARCHAR(200),
  content_type   VARCHAR(100),
  qty            INT,
  price          DECIMAL(6, 3)
);

CREATE TABLE user (
  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_id INT,
  login       VARCHAR(30),
  full_name   VARCHAR(110),
  email       VARCHAR(30),
  password    VARCHAR(30)
);

CREATE TABLE user_admin (
  id       INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  login    VARCHAR(30),
  password VARCHAR(30)
);


CREATE TABLE IF NOT EXISTS comp_tmp
  LIKE composition;
ALTER TABLE comp_tmp ADD done TINYINT NULL;
ALTER TABLE comp_tmp ADD update_id INT NULL;



CREATE TABLE catalog_update (
  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  whenUpdated DATETIME,
  catalog_id  INT,
  status      VARCHAR(30),
  tracks      INT,
  crossing    INT,
  applied     BOOL,
  filepath    VARCHAR(300),
  filename    VARCHAR(100)
);



CREATE TABLE report_item_track (
  id             INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  item_id      INT,
  track_id       INT,
  score          FLOAT,
  matched        BOOL
);



