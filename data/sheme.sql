CREATE TABLE allmusic(
id INT NOT NULL AUTO_INCREMENT, 
PRIMARY KEY(id),
  uid VARCHAR(100), 
  song_name  VARCHAR(400),
  mobile_rate decimal (6,6),
  public_rate decimal (6,6),
  composer varchar (400),
  artist varchar (400),
  publisher varchar (400),
  comment varchar (600),
  controlled_mech_share decimal (6,6),
  collect_mech_share decimal (6,6)
  );
 
  
  select * from allmusic; 