
INSERT INTO platform (id, name) VALUES (1, 'PMI');
INSERT INTO platform (id, name) VALUES (2, 'Sony ATV');


INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (1, 'WCh', 97.500, 'AUTHOR');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (1, 'NMI_WEST', 97.500, 'AUTHOR');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (1, 'NMI', 70.000, 'AUTHOR');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (1, 'PMI_WEST', 97.500, 'AUTHOR');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (1, 'PMI', 70.000, 'AUTHOR');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (1, 'NMI related', 70.000, 'RELATED');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (1, 'PMI related', 70.000, 'RELATED');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (2, 'Sony ATV', 90.000, 'AUTHOR');
INSERT INTO catalog (platform_id, name, royalty, copyright) VALUES (2, 'MSG_MCS', 90.000, 'AUTHOR');


INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (1, '37009', '#1 Da Woman', 'Tricky', 'Norman Gimbel, Charles Fox, Adrian Thaws, John Frusciante', 75.0, 75.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (1, '56984', 'Hot Potato', 'NAUGHTY BY NATURE', 'Criss, A/Gist, Kaygee/Brown, Vinnie?', 100.0, 100.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (1, '40313', 'MORE THAN I CAN BEAR', 'Various', 'REILLY, MARK VINCENT/WHITE, DANNY', 100.0, 100.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (7, '1089690-2', 'Don''t go (Don''t go (Radio Edit))', 'Wretch 32 ft Josh Kumra', '', 100.0, 75.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (8, '1107996', 'All My Bells Are Ringing', 'alice smith', 'Kripac', 100.0, 100.0);


INSERT INTO customer (id, name, right_type, royalty) VALUES (1, 'GSMTech Management', 'copyright', 12.5);

INSERT INTO user(id, login, password, customer_id) VALUES (1, 'gsmuser', '123', 1);

INSERT INTO user_admin(id, login, password) VALUES (1, 'ivan', '123');



