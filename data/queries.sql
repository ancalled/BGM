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


WHERE cat.platform_id = 1
      AND r.type = 1
      AND r.start_date BETWEEN '2013-01-01' AND '2013-04-01'
      AND cat.copyright = 'AUTHOR'
      AND i.composition_id > 0

GROUP BY i.composition_id
LIMIT 0, 15;

