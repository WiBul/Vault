INSERT INTO adres (straatnaam, huisnummer, toevoeging, postcode, plaatsnaam) VALUES ('Hoofdstraat', 4, null, '1234AB', 'Hellevoetsluis');
INSERT INTO adres (straatnaam, huisnummer, toevoeging, postcode, plaatsnaam) VALUES ('Zijstraat', 6, 'a', '9876CD', 'Groessen');

INSERT INTO rekening VALUES (1, 'NL01INGB0056210575', 41908);
INSERT INTO rekening VALUES (2, 'NL05RABO0957285205', 57313);

INSERT INTO klant VALUES (1, 'Carmen', 'GoedWachtwoord', 'Carmen', 123456789, '1985-12-30', 1, 1);
INSERT INTO klant VALUES (2, 'Jolien', 'BeterWachtwoord', 'Jolien', 987654321, '1985-10-14', 2, 2);

INSERT INTO cryptomunt VALUES (1, 'BITCOIN', 'BCN');

INSERT INTO asset VALUES (1, 1, 4.2);

INSERT INTO transactie VALUES (1, 1.3, '2021-12-15 12:43:21', 1, 1, 43.5, 2);
INSERT INTO transactie VALUES (2, 1.1, '2021-12-15 11:43:21', 1, 1, 40.5, 2);
INSERT INTO transactie VALUES (3, 0.3, '2018-12-15 10:43:21', 2, 1, 10.2, 1);


INSERT INTO refreshToken VALUES ('th120857fw1380n5yvb1i4y6dg', 1);

INSERT INTO dagkoersCrypto VALUES (54.1, '2021-12-15', 1, '20211216BCN');