CREATE TABLE allmusic (
  id                    INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (id),
  uid                   VARCHAR(100),
  song_name             VARCHAR(400),
  mobile_rate           DECIMAL(6, 6),
  public_rate           DECIMAL(6, 6),
  composer              VARCHAR(400),
  artist                VARCHAR(400),
  publisher             VARCHAR(400),
  comment               VARCHAR(600),
  controlled_mech_share DECIMAL(6, 6),
  collect_mech_share    DECIMAL(6, 6)
);


