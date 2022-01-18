-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema The_Vault
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema The_Vault
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `The_Vault` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `The_Vault` ;

-- -----------------------------------------------------
-- Table `The_Vault`.`adres`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`adres` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`adres` (
  `adresId` INT NOT NULL AUTO_INCREMENT,
  `straatnaam` VARCHAR(50) NOT NULL,
  `huisnummer` INT(5) NOT NULL,
  `toevoeging` VARCHAR(10) NULL,
  `postcode` VARCHAR(6) NOT NULL,
  `plaatsnaam` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`adresId`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `The_Vault`.`rekening`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`rekening` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`rekening` (
  `rekeningId` INT NOT NULL AUTO_INCREMENT,
  `iban` VARCHAR(18) NOT NULL,
  `saldo` BIGINT(255) NOT NULL,
  PRIMARY KEY (`rekeningId`))
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE UNIQUE INDEX `iban_UNIQUE` ON `The_Vault`.`rekening` (`iban` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `The_Vault`.`klant`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`klant` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`klant` (
  `gebruikerId` INT NOT NULL AUTO_INCREMENT,
  `gebruikersNaam` VARCHAR(100) NOT NULL,
  `wachtwoord` VARCHAR(100) NOT NULL,
  `naam` VARCHAR(100) NOT NULL,
  `bsn` INT(9) NOT NULL,
  `geboortedatum` DATE NOT NULL,
  `adresId` INT NOT NULL,
  `rekeningId` INT NOT NULL,
  PRIMARY KEY (`gebruikerId`),
  CONSTRAINT `woontOpAdres`
    FOREIGN KEY (`adresId`)
    REFERENCES `The_Vault`.`adres` (`adresId`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `rekeninghouder`
    FOREIGN KEY (`rekeningId`)
    REFERENCES `The_Vault`.`rekening` (`rekeningId`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `verzinZelf1_idx` ON `The_Vault`.`klant` (`adresId` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `verzinZelf3_idx` ON `The_Vault`.`klant` (`rekeningId` ASC) VISIBLE;

SHOW WARNINGS;
CREATE UNIQUE INDEX `gebruikersNaam_UNIQUE` ON `The_Vault`.`klant` (`gebruikersNaam` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `The_Vault`.`cryptomunt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`cryptomunt` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`cryptomunt` (
  `cryptomuntId` INT NOT NULL AUTO_INCREMENT,
  `naam` VARCHAR(30) NOT NULL,
  `afkorting` VARCHAR(15) NULL,
  PRIMARY KEY (`cryptomuntId`))
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE UNIQUE INDEX `naam_UNIQUE` ON `The_Vault`.`cryptomunt` (`naam` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `The_Vault`.`asset`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`asset` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`asset` (
  `gebruikerId` INT NOT NULL,
  `cryptomuntId` INT NOT NULL,
  `aantal` DECIMAL(50) NOT NULL,
  PRIMARY KEY (`cryptomuntId`, `gebruikerId`),
  CONSTRAINT `heeftInPortefeuille`
    FOREIGN KEY (`gebruikerId`)
    REFERENCES `The_Vault`.`klant` (`gebruikerId`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `bestaatUitCrypto`
    FOREIGN KEY (`cryptomuntId`)
    REFERENCES `The_Vault`.`cryptomunt` (`cryptomuntId`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `verzinZelf6_idx` ON `The_Vault`.`asset` (`gebruikerId` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `The_Vault`.`transactie`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`transactie` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`transactie` (
  `transactieId` INT NOT NULL AUTO_INCREMENT,
  `aantal` DECIMAL(50) NOT NULL,
  `momentTransactie` DATETIME NOT NULL,
  `koperGebruikerId` INT NOT NULL,
  `cryptomuntId` INT NOT NULL,
  `bedrag` DOUBLE NOT NULL,
  `verkoperGebruikerId` INT NOT NULL,
  PRIMARY KEY (`transactieId`),
  CONSTRAINT `kooptMunt`
    FOREIGN KEY (`koperGebruikerId`)
    REFERENCES `The_Vault`.`klant` (`gebruikerId`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `isVerhandeldIn`
    FOREIGN KEY (`cryptomuntId`)
    REFERENCES `The_Vault`.`cryptomunt` (`cryptomuntId`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `verkooptMunt`
    FOREIGN KEY (`verkoperGebruikerId`)
    REFERENCES `The_Vault`.`klant` (`gebruikerId`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `verzinZelf10_idx` ON `The_Vault`.`transactie` (`koperGebruikerId` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `verzinZelf5_idx` ON `The_Vault`.`transactie` (`cryptomuntId` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `verzinZelf7_idx` ON `The_Vault`.`transactie` (`verkoperGebruikerId` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `The_Vault`.`refreshToken`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`refreshToken` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`refreshToken` (
  `token` VARCHAR(100) NOT NULL,
  `gebruikerId` INT NOT NULL,
  PRIMARY KEY (`token`),
  CONSTRAINT `heeftToegangMet`
    FOREIGN KEY (`gebruikerId`)
    REFERENCES `The_Vault`.`klant` (`gebruikerId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `verzinZelf4_idx` ON `The_Vault`.`refreshToken` (`gebruikerId` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `The_Vault`.`dagkoersCrypto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `The_Vault`.`dagkoersCrypto` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `The_Vault`.`dagkoersCrypto` (
  `waardeCrypto` DOUBLE NOT NULL,
  `datum` DATE NOT NULL,
  `cryptomuntId` INT NOT NULL,
  PRIMARY KEY (`cryptomuntId`, `datum`),
  CONSTRAINT `dagkoersenVan`
    FOREIGN KEY (`cryptomuntId`)
    REFERENCES `The_Vault`.`cryptomunt` (`cryptomuntId`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `verzinZelf8_idx` ON `The_Vault`.`dagkoersCrypto` (`cryptomuntId` ASC) VISIBLE;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
