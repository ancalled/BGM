INSERT INTO platform (id, name) VALUES (1, 'PMI');
INSERT INTO platform (id, name) VALUES (2, 'Sony ATV');


INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (1, 1, 'WCh', 97.500, 'AUTHOR');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (2, 1, 'NMI_WEST', 97.500, 'AUTHOR');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (3, 1, 'NMI', 70.000, 'AUTHOR');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (4, 1, 'PMI_WEST', 97.500, 'AUTHOR');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (5, 1, 'PMI', 70.000, 'AUTHOR');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (6, 1, 'NMI related', 70.000, 'RELATED');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (7, 1, 'PMI related', 70.000, 'RELATED');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (8, 2, 'Sony ATV', 90.000, 'AUTHOR');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (9, 2, 'MSG_MCS', 90.000, 'AUTHOR');
INSERT INTO catalog (id, platform_id, name, royalty, copyright) VALUES (10, 2, 'Kobalt', 90.000, 'AUTHOR');


INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (1, '37009', '#1 Da Woman', 'Tricky', 'Norman Gimbel, Charles Fox, Adrian Thaws, John Frusciante', 75.0, 75.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (1, '56984', 'Hot Potato', 'NAUGHTY BY NATURE', 'Criss, A/Gist, Kaygee/Brown, Vinnie?', 100.0, 100.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (1, '40313', 'MORE THAN I CAN BEAR', 'Various', 'REILLY, MARK VINCENT/WHITE, DANNY', 100.0, 100.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (7, '1089690-2', 'Don''t go (Don''t go (Radio Edit))', 'Wretch 32 ft Josh Kumra', '', 100.0, 75.0);
INSERT INTO composition (catalog_id, code, name, artist, composer, shareMobile, sharePublic) VALUES (8, '1107996', 'All My Bells Are Ringing', 'alice smith', 'Kripac', 100.0, 100.0);

INSERT INTO customer (id, name, customer_type, right_type, royalty) VALUES (1, 'GSMTech Management', 1, 3, 12.5);
INSERT INTO customer (id, name, customer_type, right_type, royalty) VALUES (2, 'Aliya KazAK', 2, 2, 17.5);


INSERT INTO user (login, password, customer_id, full_name, email) VALUES ('max', '123', 1, 'Петр Петрович', 'petr@mail.ru');
INSERT INTO user (login, password, customer_id, full_name, email) VALUES ('vova', '123', 1, 'Вовчик', 'vov@mail.ru');
INSERT INTO user (login, password, customer_id, full_name, email) VALUES ('kirill', '123', 2, 'Царь Кир', 'kir@mail.ru');
INSERT INTO user (login, password, customer_id, full_name, email) VALUES ('perdun', '123', 2, 'Пердун пердуныч', 'perd@mail.ru');
INSERT INTO user (login, password, customer_id, full_name, email) VALUES ('huii', '123', 3, 'Хуй Какой-то', 'huii@mail.ru');


INSERT INTO customer_catalog (customer_id, catalog_id) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10);

INSERT INTO user_admin (login, password) VALUES ('max', '123');


