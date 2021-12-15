package com.example.thevault.domain.mapping.repository;

import com.example.thevault.domain.mapping.dao.*;
import com.example.thevault.domain.model.*;
import com.example.thevault.support.exceptions.AssetNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.junit.jupiter.api.Assertions.fail;

class RootRepositoryTest {

    public static RootRepository rootRepository;
    private static KlantDAO klantDAO;
    private static AssetDAO assetDAO;
    private static RekeningDAO rekeningDAO;
    private static CryptomuntDAO cryptomuntDAO;
    private static CryptoWaardeDAO cryptoWaardeDAO;
    private static Klant testKlant;
    public static Asset testAsset1;
    public static Asset testAsset2;
    public static Asset testAsset3;
    public static Asset testAsset4;
    public static Asset testAsset5;
    public static Optional<Asset> testAsset6;
    public static List<Asset> portefeuille;
    public static Cryptomunt testCryptomunt1;
    public static Cryptomunt testCryptomunt2;
    public static Cryptomunt testCryptomunt3;
    public static Cryptomunt testCryptomunt4;
    public static Cryptomunt testCryptoMunt5;
    public static Rekening rekeningOrigineelSaldo;
    public static Rekening rekeningGewijzigdSaldo;
    public static double origineelSaldo;
    public static double teWijzigenBedrag;
    public static double gewijzigdSaldo;
    public static CryptoWaarde testCryptoWaarde1;
    public static CryptoWaarde testCryptoWaarde2;
    public static CryptoWaarde testCryptoWaarde3;

    @BeforeEach
    void setUp() {
        klantDAO = Mockito.mock(KlantDAO.class);
        assetDAO = Mockito.mock(AssetDAO.class);
        rekeningDAO = Mockito.mock(RekeningDAO.class);
        cryptomuntDAO = Mockito.mock(CryptomuntDAO.class);
        cryptoWaardeDAO = Mockito.mock(CryptoWaardeDAO.class);
        testKlant = new Klant();
        rootRepository = new RootRepository(klantDAO, rekeningDAO, assetDAO, cryptomuntDAO, cryptoWaardeDAO, transactieDAO);
        testCryptomunt1 = new Cryptomunt(1, "CarmenCrypto", "CCR" );
        testCryptoWaarde1 = new CryptoWaarde("20211214CCR", testCryptomunt1, 100.0, LocalDate.now());
        testCryptomunt2 = new Cryptomunt(2, "DigiCrypto", "DIG");
        testCryptoWaarde2 = new CryptoWaarde("20211214DIG", testCryptomunt1, 75.0, LocalDate.now());
        testCryptomunt3 = new Cryptomunt(3, "Coyne", "COY");
        testCryptoWaarde3 = new CryptoWaarde("20211214COY", testCryptomunt1, 125.0, LocalDate.now());
        testCryptomunt4 = new Cryptomunt(4,"VaultMoney","VMN");
        testCryptoMunt5 = null;
        testAsset1 = new Asset(testCryptomunt1, 5.1, testKlant);
        testAsset2 = new Asset(testCryptomunt2, 2.4, testKlant);
        testAsset3 = new Asset(testCryptomunt3, 3.6, testKlant);
        testAsset4 = new Asset(testCryptomunt1, 0.5, testKlant);
        testAsset5 = new Asset(testCryptomunt4, 1.3, testKlant);
        testAsset6 = Optional.empty();
        portefeuille = new ArrayList<>();
        portefeuille.add(testAsset1);
        portefeuille.add(testAsset2);
        portefeuille.add(testAsset3);
        origineelSaldo = 1000.0;
        rekeningOrigineelSaldo = new Rekening("INGB0001234567NL", origineelSaldo);
        testKlant = new Klant("Huub", "PWHuub", portefeuille, rekeningOrigineelSaldo, "Huub", null,
                0, null);
        teWijzigenBedrag = 100.0;
        gewijzigdSaldo = 1100.0;
        rekeningGewijzigdSaldo = new Rekening("INGB0001234567NL", gewijzigdSaldo);
    }

    @Test
    void slaKlantOp() {
        Mockito.when(klantDAO.slaKlantOp(testKlant)).thenReturn(testKlant);
        Klant expected = testKlant;
        Klant actual = rootRepository.slaKlantOp(testKlant);
        assertThat(actual).as("Test opslaan van klant").isNotNull().isEqualTo(expected).
                hasNoNullFieldsOrPropertiesExcept("adres", "geboortedatum").asString().startsWith("Klant{").
                contains("Huub").doesNotContain("Michel");
    }

    @Test
    void vindKlantByGebruikersnaam() {
        Mockito.when(klantDAO.vindKlantByGebruikersnaam("Huub")).thenReturn(testKlant);
        Klant expected = testKlant;
        Klant actual = rootRepository.vindKlantByGebruikersnaam("Huub");
        assertThat(actual).as("Testen zoeken klant op gebruikersnaam").isNotNull().isEqualTo(expected).
                hasNoNullFieldsOrPropertiesExcept("adres", "geboortedatum").asString().startsWith("Klant{").
                contains("Huub").doesNotContain("Michel");
    }

    @Test
    void slaRekeningOp() {
        Mockito.when(rekeningDAO.slaRekeningOp(rekeningOrigineelSaldo)).thenReturn(rekeningOrigineelSaldo);
        Rekening expected = rekeningOrigineelSaldo;
        Rekening actual = rootRepository.slaRekeningOp(rekeningOrigineelSaldo);
        assertThat(actual).as("Test sla rekening op van testklant").isNotNull().isEqualTo(expected).
                hasNoNullFieldsOrPropertiesExcept("klant").extracting("iban", "saldo").
                contains("INGB0001234567NL",1000.0);
    }

    @Test
    void vindRekeningVanKlant() {
        Mockito.when(rekeningDAO.vindRekeningVanKlant(testKlant)).thenReturn(rekeningOrigineelSaldo);
        Rekening expected = rekeningOrigineelSaldo;
        Rekening actual = rootRepository.vindRekeningVanKlant(testKlant);
        assertThat(actual).as("Test vind rekening van testklant").isNotNull().isEqualTo(expected).
                hasNoNullFieldsOrPropertiesExcept("klant").extracting("iban", "saldo").
                contains("INGB0001234567NL",1000.0);
    }

    @Test
    void vraagSaldoOpVanKlant() {
        Mockito.when(rekeningDAO.vraagSaldoOpVanKlant(testKlant)).thenReturn(rekeningOrigineelSaldo.getSaldo());
        double expected = rekeningOrigineelSaldo.getSaldo();
        double actual = rootRepository.vraagSaldoOpVanKlant(testKlant);
        assertThat(actual).as("Test vraag saldo op van testklant").isNotNull().isEqualTo(expected).
                isNotNegative().isNotEqualTo(gewijzigdSaldo);
    }

    @Test
    void wijzigSaldoVanKlant() {
        Mockito.when(rekeningDAO.wijzigSaldoVanKlant(testKlant, teWijzigenBedrag)).thenReturn(rekeningGewijzigdSaldo);
        Rekening expected = rekeningGewijzigdSaldo;
        Rekening actual = rootRepository.wijzigSaldoVanKlant(testKlant, teWijzigenBedrag);
        assertThat(actual).as("Test wijzigen saldo van testklant").isNotNull().isEqualTo(expected).
                isNotEqualTo(rekeningOrigineelSaldo).hasNoNullFieldsOrPropertiesExcept("klant").
                extracting("iban", "saldo").contains("INGB0001234567NL",1100.0);
    }

    @Test
    void vulPortefeuilleKlant() {
        Mockito.when(assetDAO.geefAlleAssets(testKlant)).thenReturn(portefeuille);
        Mockito.when(cryptomuntDAO.geefCryptomunt(testCryptomunt1.getId())).thenReturn(testCryptomunt1);
        Mockito.when(cryptomuntDAO.geefCryptomunt(testCryptomunt2.getId())).thenReturn(testCryptomunt2);
        Mockito.when(cryptomuntDAO.geefCryptomunt(testCryptomunt3.getId())).thenReturn(testCryptomunt3);
        List<Asset> expected = portefeuille;
        List<Asset> actual = rootRepository.vulPortefeuilleKlant(testKlant);
        assertThat(actual).as("Test vullen portefeuille van testklant").isNotNull().isEqualTo(expected).
                hasSize(3).contains(testAsset1, atIndex(0)).contains(testAsset2, atIndex(1)).
                contains(testAsset3, atIndex(2)).doesNotContain(testAsset4).extracting(Asset::getCryptomunt).
                extracting(Cryptomunt::getName).contains("CarmenCrypto", "DigiCrypto", "Coyne").doesNotContain("BitCoin");
    }

    @Test
    void geefBestaandeAssetVanKlant() {
        Mockito.when(assetDAO.geefAsset(testKlant, testAsset1.getCryptomunt())).
                thenReturn(java.util.Optional.ofNullable(testAsset1));
        Asset expected = testAsset1;
        Asset actual = rootRepository.geefAssetVanKlant(testKlant, testAsset1.getCryptomunt());
        assertThat(actual).as("Test asset van testklant opvragen").isNotNull().isEqualTo(expected).
                isIn(portefeuille).hasNoNullFieldsOrProperties().asString().startsWith("Asset{").contains("5.1").
                doesNotContain("2.4").hasSize(316);
    }

    @Test
    void exceptionOnbestaandeAssetVanKlant() {
        Mockito.when(assetDAO.geefAsset(testKlant, testCryptoMunt5)).
                thenReturn(testAsset6);
        try {
            rootRepository.geefAssetVanKlant(testKlant, testCryptoMunt5);
            fail();
        } catch (AssetNotExistsException assetNotExistsException){
            assertThat(assetNotExistsException.getMessage()).isEqualTo("Asset bestaat niet.");
        }
    }

    @Test
    void slaNieuweAssetVanKlantOp() {
        Mockito.when(assetDAO.geefAsset(testKlant, testAsset5.getCryptomunt())).
                thenReturn(null);
        Mockito.when(assetDAO.voegNieuwAssetToeAanPortefeuille(testAsset5)).thenReturn(testAsset5);
        Asset expected = testAsset5;
        Asset actual = rootRepository.slaNieuwAssetVanKlantOp(testAsset5);
        assertThat(actual).as("Test opslaan asset van testklant").isNotNull().isEqualTo(expected).
                isNotIn(portefeuille).isNotEqualTo(testAsset2).extracting("cryptomunt").
                extracting("name", "symbol").contains("VaultMoney", "VMN");
    }

    @Test
    void wijzigAssetVanKlant() {
        Mockito.when(assetDAO.updateAsset(testAsset3)).thenReturn(testAsset3);
        Asset expected = testAsset3;
        Asset actual = rootRepository.wijzigAssetVanKlant(testAsset3);
        assertThat(actual).as("Test wijzigen asset van testklant").isNotNull().isEqualTo(expected).
                isIn(portefeuille).hasNoNullFieldsOrProperties().asString().startsWith("Asset{").contains("Coyne").
                doesNotContain("BitCoin").hasSize(309);
    }
}