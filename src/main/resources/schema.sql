CREATE TABLE adres (
                         adresId INT NOT NULL AUTO_INCREMENT,
                         straatnaam VARCHAR(50) NOT NULL,
                         huisnummer INT(5) NOT NULL,
                         toevoeging VARCHAR(10) NULL,
                         postcode VARCHAR(6) NOT NULL,
                         plaatsnaam VARCHAR(30) NOT NULL,
                         PRIMARY KEY (adresId)
);



CREATE TABLE klant (
                        gebruikerId INT NOT NULL AUTO_INCREMENT,
                        gebruikersNaam VARCHAR(100),
                        wachtwoord VARCHAR(100),
                        naam VARCHAR(100),
                        bsn INT(9),
                        geboortedatum DATE,
                        adresId INT,
                        PRIMARY KEY (gebruikerId),
                        CONSTRAINT woontOpAdres
                        FOREIGN KEY (adresId)
                        REFERENCES adres (adresId)
                        ON DELETE RESTRICT
                        ON UPDATE CASCADE
);

CREATE INDEX verzinZelf1_idx ON klant (adresId ASC);
CREATE UNIQUE INDEX gebruikersNaam_UNIQUE ON klant (gebruikersNaam ASC);

CREATE TABLE rekening (
                          iban VARCHAR(18) NOT NULL,
                          saldo DECIMAL(50,10) NOT NULL,
                          gebruikerId INT NOT NULL,
                          PRIMARY KEY (iban),
                          CONSTRAINT rekeningVan
                              FOREIGN KEY (gebruikerId)
                                  REFERENCES klant (gebruikerId)
                                  ON DELETE RESTRICT
                                  ON UPDATE CASCADE

);

CREATE UNIQUE INDEX iban_UNIQUE ON rekening (iban ASC);
CREATE INDEX foreign_Key_rekening_gebruiker_idx ON rekening (gebruikerId ASC);

CREATE TABLE cryptomunt (
                        cryptomuntId INT NOT NULL AUTO_INCREMENT,
                        naam VARCHAR(30) NOT NULL,
                        afkorting VARCHAR(15) NULL,
                        PRIMARY KEY (cryptomuntId)
);

CREATE UNIQUE INDEX naam_UNIQUE ON cryptomunt (naam ASC);

CREATE TABLE asset (
                        gebruikerId INT NOT NULL,
                        cryptomuntId INT NOT NULL,
                        aantal DECIMAL(50,10) NOT NULL,
                        PRIMARY KEY (cryptomuntId, gebruikerId),
                        CONSTRAINT heeftInPortefeuille
                        FOREIGN KEY (gebruikerId)
                        REFERENCES klant (gebruikerId)
                        ON DELETE RESTRICT
                        ON UPDATE CASCADE,
                        CONSTRAINT bestaatUitCrypto
                        FOREIGN KEY (cryptomuntId)
                        REFERENCES cryptomunt (cryptomuntId)
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT
);

CREATE INDEX verzinZelf6_idx ON asset (gebruikerId ASC);

CREATE TABLE transactie (
                        transactieId INT NOT NULL AUTO_INCREMENT,
                        aantal DECIMAL(50,10) NOT NULL,
                        momentTransactie DATETIME NOT NULL,
                        koperGebruikerId INT NOT NULL,
                        cryptomuntId INT NOT NULL,
                        bedrag DECIMAL(50,10) NOT NULL,
                        verkoperGebruikerId INT NOT NULL,
                        bankFee DECIMAL(50,10),
                        PRIMARY KEY (transactieId),
                        CONSTRAINT kooptMunt
                        FOREIGN KEY (koperGebruikerId)
                        REFERENCES klant (gebruikerId)
                        ON DELETE RESTRICT
                        ON UPDATE CASCADE,
                        CONSTRAINT isVerhandeldIn
                        FOREIGN KEY (cryptomuntId)
                        REFERENCES cryptomunt (cryptomuntId)
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT,
                        CONSTRAINT verkooptMunt
                        FOREIGN KEY (verkoperGebruikerId)
                        REFERENCES klant (gebruikerId)
                        ON DELETE RESTRICT
                        ON UPDATE CASCADE
);

CREATE INDEX verzinZelf10_idx ON transactie (koperGebruikerId ASC);
CREATE INDEX verzinZelf5_idx ON transactie (cryptomuntId ASC);
CREATE INDEX verzinZelf7_idx ON transactie (verkoperGebruikerId ASC);

CREATE TABLE refreshToken (
                        token VARCHAR(100) NOT NULL,
                        gebruikerId INT NOT NULL,
                        PRIMARY KEY (token),
                        CONSTRAINT heeftToegangMet
                        FOREIGN KEY (gebruikerId)
                        REFERENCES klant (gebruikerId)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
);

CREATE INDEX verzinZelf4_idx ON refreshToken (gebruikerId ASC);

CREATE TABLE dagkoersCrypto (
                        waardeCrypto DECIMAL(50,10) NOT NULL,
                        datum DATE NOT NULL,
                        cryptomuntId INT NOT NULL,
                        cryptowaardeId VARCHAR(15) NOT NULL,
                        PRIMARY KEY (cryptowaardeId),
                        CONSTRAINT dagkoersenVan
                        FOREIGN KEY (cryptomuntId)
                        REFERENCES cryptomunt (cryptomuntId)
                        ON DELETE RESTRICT
                        ON UPDATE RESTRICT
);

CREATE INDEX verzinZelf8_idx ON dagkoersCrypto (cryptomuntId ASC);