CREATE TABLE adres (
                         adresId INT NOT NULL AUTO_INCREMENT,
                         straatnaam VARCHAR(50) NOT NULL,
                         huisnummer INT(5) NOT NULL,
                         toevoeging VARCHAR(10) NULL,
                         postcode VARCHAR(6) NOT NULL,
                         plaatsnaam VARCHAR(30) NOT NULL,
                         PRIMARY KEY (adresId)
);

CREATE TABLE rekening (
                        rekeningId INT NOT NULL AUTO_INCREMENT,
                        iban VARCHAR(18) NOT NULL,
                        saldo DECIMAL(50,10) NOT NULL,
                        PRIMARY KEY (rekeningId)
);

CREATE UNIQUE INDEX iban_UNIQUE ON rekening (iban ASC);

CREATE TABLE klant (
                        gebruikerId INT NOT NULL AUTO_INCREMENT,
                        gebruikersNaam VARCHAR(100) NOT NULL,
                        wachtwoord VARCHAR(100) NOT NULL,
                        naam VARCHAR(100) NOT NULL,
                        bsn INT(9) NOT NULL,
                        geboortedatum DATE NOT NULL,
                        adresId INT NOT NULL,
                        rekeningId INT NOT NULL,
                        PRIMARY KEY (gebruikerId),
                        CONSTRAINT woontOpAdres
                        FOREIGN KEY (adresId)
                        REFERENCES adres (adresId)
                        ON DELETE RESTRICT
                        ON UPDATE CASCADE,
                        CONSTRAINT rekeninghouder
                        FOREIGN KEY (rekeningId)
                        REFERENCES rekening (rekeningId)
                        ON DELETE RESTRICT
                        ON UPDATE CASCADE
);

CREATE INDEX verzinZelf1_idx ON klant (adresId ASC);
CREATE INDEX verzinZelf3_idx ON klant (rekeningId ASC);
CREATE UNIQUE INDEX gebruikersNaam_UNIQUE ON klant (gebruikersNaam ASC);

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