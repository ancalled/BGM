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
  id         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  NAME       VARCHAR(200),
  right_type VARCHAR(50),
  royalty    DECIMAL(6, 3)
);

CREATE TABLE customer_report (
  id            INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_id   INT,
  order_date    DATE,
  download_date DATE
);

CREATE TABLE user (
  id       INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  login    VARCHAR(30),
  password VARCHAR(30),
  role     VARCHAR(15)
);

INSERT INTO customer (name, right_type, royalty) VALUES ('GSM Technologies', 'copyrights', 12.5);
INSERT INTO customer (name, right_type, royalty) VALUES ('Authors Society', 'copyrights', 12.5);

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


CREATE INDEX code_index ON composition (code) USING BTREE;

CREATE TABLE composition (
  id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  catalog_id  INT NOT NULL,
  code        VARCHAR(20),
  NAME        VARCHAR(400),
  artist      VARCHAR(400),
  composer    VARCHAR(400),
  shareMobile DECIMAL(6, 3),
  sharePublic DECIMAL(6, 3)
);


# -------------------------------------------------------------------------


SELECT
  i.id,
  c.code,
  replace(c.name, CHAR(9), ' ')name,
  replace(c.artist, CHAR(9), ' ')artist,
  replace(c.composer, CHAR(9), ' ')composer,
  report_item.content_type,
  price,
  sum(qty),
  (price * sum(qty))vol,
  shareMobile,

  @customerRoyalty := (SELECT
  cm.royalty
                       FROM customer cm
                       WHERE cm.id = (SELECT
                                        cr.customer_id
                                      FROM customer_report cr
                                      WHERE cr.id =
                                            i.report_id))`customer_royalty`,


#   @customerRoyalty := (SELECT cm.royalty
#                        FROM customer_report cr
#                          LEFT JOIN customer cm
#                            ON (cm.id = cr.customer_id)
#                        WHERE cr.id =
#                              i.report_id) `customer royalty`,

  cat.royalty cat_royalty,

  round((sum(qty) * price * (shareMobile / 100) * (@customerRoyalty / 100) * (cat.royalty / 100)), 3) revenue,
  cat.name catalog
#   cat.copyright copyright

FROM customer_report_item i

  LEFT JOIN composition c
    ON (i.composition_id = c.id)

  INNER JOIN catalog cat
    ON (cat.id = c.catalog_id)

WHERE i.composition_id > 0
#       AND cat.platform = 'NMI'
  GROUP BY i.composition_id
LIMIT 0, 15;

alter table catalog add column copyright varchar (20);

INSERT INTO catalog (name, royalty, copyright) VALUES ('WCh', 97.500, 'AUTHOR');
INSERT INTO catalog (name, royalty, copyright) VALUES ('NMI_WEST', 97.500, 'AUTHOR');
INSERT INTO catalog (name, royalty, copyright) VALUES ('NMI', 70.000, 'AUTHOR');
INSERT INTO catalog (name, royalty, copyright) VALUES ('PMI_WEST', 97.500, 'AUTHOR');
INSERT INTO catalog (name, royalty, copyright) VALUES ('PMI', 70.000, 'AUTHOR');
INSERT INTO catalog (name, royalty, copyright) VALUES ('NMI related', 70.000, 'RELATED');
INSERT INTO catalog (name, royalty, copyright) VALUES ('PMI related', 70.000, 'RELATED');
INSERT INTO catalog (name, royalty, copyright) VALUES ('Sony ATV', 90.000, 'AUTHOR');
INSERT INTO catalog (name, royalty, copyright) VALUES ('MSG_MCS', 90.000, 'AUTHOR');

