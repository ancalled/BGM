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
#   controlled_mech_share DECIMAL(6, 6),
#   collect_mech_share    DECIMAL(6, 6)
# );

CREATE TABLE catalogID (
  id      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name    VARCHAR(200),
  royalty DECIMAL(6, 6)
);


CREATE TABLE name (
  id         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  catalog_id INT NOT NULL,
  code       VARCHAR(20),
  name       VARCHAR (400),
  artist     VARCHAR (400),
  composer   VARCHAR(400),
  shareMobile DECIMAL(6, 6),
  sharePublic DECIMAL(6, 6)
);



