# mobile report calculus, variables: platform_id and start_date range

SELECT
  i.id,
  c.code,
  i.content_type,
  replace(c.name, CHAR(9), ' ') name,
  replace(c.artist, CHAR(9), ' ') artist,
  replace(c.composer, CHAR(9), ' ') composer,
  cat.copyright copyright,
  p.name platform,
  cat.name catalog,

  c.shareMobile,
  cat.royalty cat_royalty,
  cm.royalty,

  price,
  sum(qty) totalQty,
  (price * sum(qty)) vol,

  round((sum(qty) * price * (shareMobile / 100) * (cm.royalty / 100) * (cat.royalty / 100)), 3) revenue


FROM customer_report_item i

  LEFT JOIN composition c
    ON (i.composition_id = c.id)

  LEFT JOIN catalog cat
    ON (cat.id = c.catalog_id)

  LEFT JOIN platform p
    ON (cat.platform_id = p.id)

  LEFT JOIN customer_report r
    ON (i.report_id = r.id)

  LEFT JOIN customer cm
    ON (r.customer_id = cm.id)

WHERE cat.platform_id = 1
      AND r.type = 0
      AND r.start_date BETWEEN '2013-01-01' AND '2013-04-01'
      AND i.composition_id > 0

GROUP BY i.composition_id
LIMIT 0, 15;


# ------------------------------------------------------------------------------


# public report calculus, variables: platform_id and start_date range

SELECT
  i.id,
  c.code,
  replace(c.name, CHAR(9), ' ') name,
  replace(c.artist, CHAR(9), ' ') artist,
  replace(c.composer, CHAR(9), ' ') composer,
  cat.copyright copyright,
  p.name platform,
  cat.name catalog,

  c.sharePublic,
  cat.royalty cat_royalty,
  sum(qty) totalQty

FROM customer_report_item i

  LEFT JOIN composition c
    ON (i.composition_id = c.id)

  LEFT JOIN catalog cat
    ON (cat.id = c.catalog_id)

  LEFT JOIN platform p
    ON (cat.platform_id = p.id)

  LEFT JOIN customer_report r
    ON (i.report_id = r.id)


WHERE
  cat.platform_id = 1
      AND r.type = 1
      AND cat.copyright = 'AUTHOR'
      AND i.composition_id > 0

GROUP BY i.composition_id
LIMIT 0, 15;





# ---------------------------------------------------------------------------------
#             CATALOG UPDATES

# Create temporary table to load all spreadsheet data into it
CREATE TABLE IF NOT EXISTS comp_tmp
  LIKE composition;
ALTER TABLE comp_tmp ADD done TINYINT NULL;
ALTER TABLE comp_tmp ADD update_id INT NULL;

# Load data
LOAD DATA LOCAL INFILE './nmi_related.csv'
INTO TABLE comp_tmp
CHARACTER SET 'utf8'
FIELDS TERMINATED BY ';'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(@dummy, code, name, composer, artist, @dummy, @dummy, @shareMobile, @sharePublic)
SET catalog_id=7,
  shareMobile=IF(@shareMobile != '', @shareMobile, 0),
  sharePublic=IF(@sharePublic != '', @sharePublic, 0);


#Show warnings
SHOW WARNINGS LIMIT 0, 10;

# Show number of code-intercepting rows
SELECT
  count(DISTINCT t.id)
FROM composition c INNER JOIN comp_tmp t
    ON c.code = t.code
WHERE t.update_id = 1;

# show table diff:
SELECT
  t.code,
  IF(t.artist != c.artist, concat('\'', c.artist, '\' -> \'', t.artist, '\''), '') artistDiff,
  IF(t.name != c.name, concat('\'', c.name, '\' -> \'', t.name, '\''), '') nameDiff,
  IF(t.composer != c.composer, concat('\'', c.composer, '\' -> \'', t.composer, '\''), '') composerDiff,
  IF(t.shareMobile != c.shareMobile, concat(c.shareMobile, ' -> ', t.shareMobile), '') shareMobileDiff,
  IF(t.sharePublic != c.sharePublic, concat(c.sharePublic, ' -> ', t.sharePublic), '') sharePublicDiff,
  IF(t.catalog_id != c.catalog_id, concat(c.catalog_id, ' -> ', t.catalog_id), '') sharePublicDiff
FROM comp_tmp t INNER JOIN composition c
    ON c.code = t.code
       AND c.catalog_id = t.catalog_id
WHERE t.update_id = 1;



# Update existing tracks
UPDATE composition c
  INNER JOIN comp_tmp t
    ON c.code = t.code
     AND c.catalog_id = t.catalog_id
SET c.name = IF(t.name != '', t.name, c.name),
  c.composer = IF(t.composer != '', t.composer, c.composer),
  c.artist = IF(t.artist != '', t.artist, c.artist),
  c.shareMobile = IF(t.shareMobile != '', t.shareMobile, c.shareMobile),
  c.sharePublic = IF(t.sharePublic != '', t.sharePublic, c.sharePublic),
  t.done = 1
WHERE t.update_id = 1;



# Insert new tracks
INSERT INTO composition (code, name, composer, artist, shareMobile, sharePublic, catalog_id)
  SELECT
    code,
    name,
    composer,
    artist,
    shareMobile,
    sharePublic,
    catalog_id
  FROM comp_tmp
  WHERE done IS null AND update_id = 1;



# Update catalog stats

UPDATE catalog c
SET tracks = (SELECT
                count(DISTINCT t.id)
              FROM composition t
              WHERE t.catalog_id = c.id);

UPDATE catalog c
SET artists = (SELECT
                 count(DISTINCT t.artist)
               FROM composition t
               WHERE t.catalog_id = c.id);