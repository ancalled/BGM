# CREATE TABLE allmusic (
#   id                    INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
#   uid                   VARCHAR(100),
#   song_name             VARCHAR(400),
#   mobile_rate           DECIMAL(6, 6),
#   public_rate           DECIMAL(6, 6),
#   composer              VARCHAR(400),
#   artist                VARCHAR(400),
#   publisher             VARCHAR(400),
#   comment               VARCHAR(600),
#   controlled_mech_share DECIMAL(6, 3),
#   collect_mech_share    DECIMAL(6, 3)
# );




CREATE TABLE catalogID (
  id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  NAME    VARCHAR(200),
  royalty DECIMAL(6, 3)
);

CREATE TABLE customer (
  id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  NAME    VARCHAR(200),
  right_type VARCHAR(50),
  royalty DECIMAL(6, 3)
);

CREATE TABLE customer_report (
  id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_id INT,
  order_date DATE,
  download_date DATE
);

insert into customer (name,right_type,royalty) values ('GSM Technologies','copyrights',12.5);

CREATE TABLE customer_report_item (
  id  INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  report_id INT,
  composition_id INT,
  name VARCHAR (200),
  artist VARCHAR (200),
  content_type VARCHAR (100),
  qty INT,
  price DECIMAL(6,3)
);





CREATE index code_index ON composition(code) USING btree;

CREATE TABLE composition (
  id         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  catalog_id INT NOT NULL,
  code       VARCHAR(20),
  NAME       VARCHAR (400),
  artist     VARCHAR (400),
  composer   VARCHAR(400),
  shareMobile DECIMAL(6, 3),
  sharePublic DECIMAL(6, 3)
);



