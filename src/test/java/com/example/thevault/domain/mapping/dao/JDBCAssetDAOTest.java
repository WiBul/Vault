package com.example.thevault.domain.mapping.dao;

import com.example.thevault.domain.model.*;
import com.example.thevault.support.exceptions.AssetNotExistsException;
import com.example.thevault.support.exceptions.NotEnoughCryptoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doCallRealMethod;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class JDBCAssetDAOTest {

    private AssetDAO testAssetDAO;
    private static Gebruiker testKlant1;
    private static Gebruiker testKlant2;
    private static Cryptomunt testCryptomunt1;
    private static Cryptomunt testCryptomunt2;
    private static Cryptomunt testCryptomunt3;
    private static Asset testAsset1;
    private static Asset testAsset2;
    private static Asset testAsset3;
    private static Asset testAsset4;
    private static Asset testAsset5;
    private static List<Asset> testAsset6;
    private static Asset testAsset7;
    private static Asset testAsset8;

    @Autowired
    public JDBCAssetDAOTest(AssetDAO testAssetDAO) {
        super();
        this.testAssetDAO = testAssetDAO;
    }

    @Test
    public void setupTest(){
        assertThat(testAssetDAO).isNotNull();
    }

    @BeforeEach
    void setup() {
        testKlant1 = new Klant("Jolien", "BeterWachtwoord",
                null, null, null, "Jolien",null,
                987654321, LocalDate.parse("1985-10-14"));
        testKlant2 = new Klant("Carmen", "GoedWachtwoord",
                null, null, null,"Carmen", null,
                123456789, LocalDate.parse("1985-12-30"));
        testKlant1.setGebruikerId(2);
        testKlant2.setGebruikerId(1);
        testCryptomunt1 = new Cryptomunt(1, "Bitcoin", "BCN");
        testCryptomunt2 = new Cryptomunt(1, null, null);
        testCryptomunt3 = new Cryptomunt(2, null, null);
        testAsset1 = new Asset(testCryptomunt1, 3.4, testKlant1);
        testAsset2 = new Asset(testCryptomunt1, 4.2, testKlant2);
        testAsset3 = new Asset(testCryptomunt1, 5.3, testKlant2);
        testAsset4 = new Asset(testCryptomunt2, 4.2, testKlant2);
        testAsset5 = new Asset(testCryptomunt3, 3.5, testKlant2);
        testAsset6 = new ArrayList<>();
        testAsset6.add(testAsset4);
        testAsset6.add(testAsset5);
        testAsset7 = new Asset(testCryptomunt1, 100, testKlant2);
        testAsset8 = new Asset(testCryptomunt1, 3.1, testKlant2);
    }

    @Test
    void geefAanwezigAsset() {
        Optional<Asset> actual = testAssetDAO.geefAssetGebruiker(testKlant2, testCryptomunt1);
        Asset actualUitgepakt = actual.orElse(testAsset7);
        Asset expected = testAsset4;
        Asset unexpected = testAsset7;
        assertThat(actualUitgepakt.getAantal()).as("Test geef 1 asset van gebruiker: aantal").isNotNull().
                isEqualTo(expected.getAantal()).isNotEqualTo(unexpected.getAantal());
    }

    @Test
    void geefNietAanwezigAsset() {
        Optional<Asset> actual = testAssetDAO.geefAssetGebruiker(testKlant1, testCryptomunt2);
        assertThat(actual).as("Test geef niet bestaand asset van gebruiker").isEmpty();
    }

    @Test
    void geefAlleAssets() {
        List<Asset> actual = testAssetDAO.geefAlleAssets(testKlant2);
        List<Asset> expected = testAsset6;
        assertThat(actual.get(0).getGebruiker().getGebruikerId()).
                as("Test ophalen alle assets van gebruiker: gebruiker 1").isNotNull().
                isEqualTo(expected.get(0).getGebruiker().getGebruikerId());
        assertThat(actual.get(1).getGebruiker().getGebruikerId()).
                as("Test ophalen alle assets van gebruiker: gebruiker 2").isNotNull().
                isEqualTo(expected.get(1).getGebruiker().getGebruikerId());
        assertThat(actual.get(0).getCryptomunt().getId()).
                as("Test ophalen alle assets van gebruiker: crypto 1").isNotNull().
                isEqualTo(expected.get(0).getCryptomunt().getId());
        assertThat(actual.get(1).getCryptomunt().getId()).
                as("Test ophalen alle assets van gebruiker: crypto 2").isNotNull().
                isEqualTo(expected.get(1).getCryptomunt().getId());
        assertThat(actual.get(0).getAantal()).as("Test ophalen alle assets van gebruiker: aantal 1").
                isNotNull().isEqualTo(expected.get(0).getAantal());
        assertThat(actual.get(1).getAantal()).as("Test ophalen alle assets van gebruiker: aantal 1").
                isNotNull().isEqualTo(expected.get(1).getAantal());
    }

    @Test
    void geefAlleAssetsLeeg() {
        List<Asset> actual = testAssetDAO.geefAlleAssets(testKlant1);
        assertThat(actual).as("Test ophalen alle assets van gebruiker is leeg").isNullOrEmpty();
    }

    @Test
    void voegNieuwAssetToeAanPortefeuille() {
        Asset actual = testAssetDAO.voegNieuwAssetToeAanPortefeuille(testAsset1);
        Asset expected = testAsset1;
        assertThat(actual).as("Test nieuwe asset toevoegen").isNotNull().isEqualTo(expected).extracting(Asset::getGebruiker).
                extracting(Gebruiker::getGebruikersnaam).isEqualTo("Jolien");
    }

    @Test
    void verwijderAssetUitPortefeuille() {
        Asset actual = testAssetDAO.verwijderAssetUitPortefeuille(testAsset2);
        Asset expected = testAsset2;
        assertThat(actual).as("Verwijder asset uit portefeuille test deel 1").isNotNull().isEqualTo(expected).
                hasNoNullFieldsOrPropertiesExcept("Cryptomunt", "Aantal", "Gebruiker");
        Optional<Asset> actual2 = testAssetDAO.geefAssetGebruiker(testKlant2, testCryptomunt1);
        assertThat(actual2).as("Verwijder asset uit portefeuille test deel 2").isEmpty();
    }

    //TODO Carmen: Uitvinden hoe ik een integratietest kan maken voor deze method, gevraagd aan Huub
    //Staat het goed in de database? Wordt het object goed geupdate teruggegeven?
    //Hoe kan ik controleren of de nieuwe informatie goed in de database staat?
    //LITECOIN VERANDEREN IN ETHEREUM
    @Test
    void updateAssetAankoop() {
        Asset actual = testAssetDAO.updateAsset(testKlant2, testCryptomunt1, 1.1);
        Asset expected = testAsset3;
        assertThat(actual.getAantal()).isEqualTo(expected.getAantal());
    }

    @Test
    void updateAssetVerkoop() {
        Asset actual = testAssetDAO.updateAsset(testKlant2, testCryptomunt1, -1.1);
        Asset expected = testAsset8;
        assertThat(actual.getAantal()).isEqualTo(expected.getAantal());
    }

    @Test
    void updateAssetVerkoopTekortAanCrypto() {
        try{
            testAssetDAO.updateAsset(testKlant2, testCryptomunt1, -10);
            fail();
        } catch (NotEnoughCryptoException notEnoughCryptoException) {
            System.out.println("Not Enough Crypto exception werkt");
        }
    }

    /*@AfterEach
    void breakdown() {
        AssetDAO testAssetDAO = null;
        Gebruiker testKlant1 = null;
        Gebruiker testKlant2 = null;
        Cryptomunt testCryptomunt1 = null;
        Cryptomunt testCryptomunt2 = null;
        Cryptomunt testCryptomunt3 = null;
        Asset testAsset1 = null;
        Asset testAsset2 = null;
        Asset testAsset3 = null;
        Asset testAsset4 = null;
        Asset testAsset5 = null;
        List<Asset> testAsset6 = null;
    }*/
}