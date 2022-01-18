package com.example.thevault.service;

import com.example.thevault.domain.mapping.repository.RootRepository;
import com.example.thevault.domain.model.Klant;
import com.example.thevault.domain.model.Rekening;
import com.example.thevault.support.exceptions.UserNotExistsException;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class RekeningServiceTest {

    public static Klant bestaandeKlant;
    public static Klant nietBestaandeKlant;
    public static Klant nieuweKlant;
    public static RootRepository mockRepo;
    public static Rekening rekeningExpected;
    public static RekeningService rekeningServiceTest;
    public static Rekening nieuweRekening;
    public static Klant bestaandeKlantVoorWijzigSaldo;
    public static Rekening rekeningVoorWijzigSaldoExpected;

    @BeforeAll
    static void setUp() {
        bestaandeKlant = new Klant( "Henknr1", "fdsaljkl", "Hello", 1890393, LocalDate.of(1991, 1, 12));
        nietBestaandeKlant = new Klant( "HarryBeste", "210jklf", "", 101212, LocalDate.of(1991, 1, 12));
        nieuweKlant = new Klant( "ThomasBeste", "831hgtr", "", 1528719, LocalDate.of(1990, 5, 10));
        mockRepo = Mockito.mock(RootRepository.class);
        rekeningServiceTest = new RekeningService(mockRepo);
        rekeningExpected = new Rekening("INGB0001234567NL", 1000.0);
        bestaandeKlant.setRekening(rekeningExpected);
        rekeningExpected.setGebruiker(bestaandeKlant);
        nieuweRekening = new Rekening("NL20RABO9876543", 1000.0);
        bestaandeKlantVoorWijzigSaldo = new Klant("MarkSlegte", "456jesv", "", 5248136, LocalDate.of(1987, 2, 16));
        rekeningVoorWijzigSaldoExpected = new Rekening("INGB0001234567NL", 3000.0);
        bestaandeKlantVoorWijzigSaldo.setRekening(rekeningVoorWijzigSaldoExpected);
        rekeningVoorWijzigSaldoExpected.setGebruiker(bestaandeKlantVoorWijzigSaldo);
    }

    /**
     * @Author Wim Bultman
     */
    @Test
    void createIban() {
        Iban iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("TVLT").buildRandom();
        assertThat(iban.toString()).contains("NL");
    }

    /**
     * @Author Ju-Sen Cheung
     */
    @Test
    void creeerRekening() {
        Rekening actual = rekeningServiceTest.creeerRekening(nieuweKlant);
        System.out.println(actual);
        assertThat(actual.getIban()).isNotNull();
        assertThat(actual.getSaldo()).isEqualTo(1000.0);
        //TODO Testen dat de rekening aan de juiste klant is gekoppeld
    }

    /**
     * @Author Ju-Sen Cheung
     */
    @Test
    void slaRekeningOp() {
        Mockito.when(mockRepo.slaRekeningOp(nieuweRekening)).thenReturn(nieuweRekening);

        Rekening actual = rekeningServiceTest.slaRekeningOp(nieuweRekening);
        System.out.println(actual);
        Rekening expected = nieuweRekening;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    /**
     * @Author Ju-Sen Cheung
     */
    @Test
    void vindRekeningVanKlant() {
        Mockito.when(mockRepo.vindKlantByGebruikersnaam(bestaandeKlant.getGebruikersnaam())).thenReturn(bestaandeKlant);
        Mockito.when(mockRepo.vindRekeningVanGebruiker(bestaandeKlant)).thenReturn(rekeningExpected);

        Rekening actual = rekeningServiceTest.vindRekening(bestaandeKlant);
        System.out.println(actual);
        Rekening expected = rekeningExpected;
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    /**
     * @Author Ju-Sen Cheung
     */
    @Test
    void vindRekeningVanNietBestaandeKlant() {
        Mockito.when(mockRepo.vindKlantByGebruikersnaam(bestaandeKlant.getGebruikersnaam())).thenReturn(bestaandeKlant);
        Mockito.when(mockRepo.vindRekeningVanGebruiker(bestaandeKlant)).thenReturn(rekeningExpected);

        try{
            rekeningServiceTest.vraagSaldoOp(nietBestaandeKlant);
            fail("Moet een UserNotExistsException gooien");
        } catch (UserNotExistsException expected){
            System.out.println("Test geslaagd!");
        }
    }

    /**
     * @Author Elise Olthof
     */
    @Test
    void vraagSaldoOp() {
        Mockito.when(mockRepo.vindKlantByGebruikersnaam(bestaandeKlant.getGebruikersnaam())).thenReturn(bestaandeKlant);
        Mockito.when(mockRepo.vindRekeningVanGebruiker(bestaandeKlant)).thenReturn(rekeningExpected);
        Mockito.when(mockRepo.vraagSaldoOpVanGebruiker(bestaandeKlant)).thenReturn(bestaandeKlant.getRekening().getSaldo());

        double saldoActual = rekeningServiceTest.vraagSaldoOp(bestaandeKlant);
        System.out.println(saldoActual);
        double expected = 1000.0;
        assertThat(saldoActual).isEqualTo(expected);
    }

    /**
     * @Author Elise Olthof
     */
    @Test
    void vraagSaldoOpVanNietBestaandeKlant() {
        Mockito.when(mockRepo.vindKlantByGebruikersnaam(bestaandeKlant.getGebruikersnaam())).thenReturn(bestaandeKlant);
        Mockito.when(mockRepo.vindRekeningVanGebruiker(bestaandeKlant)).thenReturn(rekeningExpected);
        Mockito.when(mockRepo.vraagSaldoOpVanGebruiker(bestaandeKlant)).thenReturn(bestaandeKlant.getRekening().getSaldo());

        try{
            rekeningServiceTest.vraagSaldoOp(nietBestaandeKlant);
            fail("Moet een UserNotExistsException gooien");
        } catch (UserNotExistsException expected){
            System.out.println("Test geslaagd!");
        }
    }

    /**
     * @Author Ju-Sen Cheung
     */
    @Test
    void wijzigSaldo() {
        Mockito.when(mockRepo.wijzigSaldoVanGebruiker(bestaandeKlant, 2000.0)).thenReturn(rekeningVoorWijzigSaldoExpected);

        Rekening rekeningActual = rekeningServiceTest.wijzigSaldo(bestaandeKlant, 2000.0);
        double expected = 3000.0;
        double actual = rekeningActual.getSaldo();
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void wijzigSaldoInGebruikerRekeningJava(){
        Mockito.when(mockRepo.wijzigSaldoVanGebruiker(bestaandeKlant, 2000.0)).thenReturn(rekeningVoorWijzigSaldoExpected);

        Rekening rekening = rekeningServiceTest.wijzigSaldo(bestaandeKlant, 2000.0);
        double expected = 3000.0;
        bestaandeKlant.setRekening(rekening);
        double actual = bestaandeKlant.getRekening().getSaldo();
        assertThat(actual).isEqualTo(expected);
    }

}
