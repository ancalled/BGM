# Platform report

SELECT
  i.id,
  c.code,
  replace(c.name, CHAR(9), ' ')name,
  replace(c.artist, CHAR(9), ' ')artist,
  replace(c.composer, CHAR(9), ' ')composer,
  i.content_type,
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