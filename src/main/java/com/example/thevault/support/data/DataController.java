// Created by S.C. van Gils
// Creation date 20-12-2021

/**
 * Deze klasse is bedoeld om de database te kunnen vullen met random gegenereerde data
 */

package com.example.thevault.support.data;

import com.example.thevault.domain.model.*;
import com.example.thevault.service.*;
import com.example.thevault.support.authorization.AuthorizationService;
import com.example.thevault.support.exceptions.BalanceTooLowException;
import com.example.thevault.support.exceptions.NotEnoughCryptoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.thevault.support.data.DataGenerator.genereerRandomGetal;

/**
 * Deze klasse is bedoeld om data te genereren voor de database en end-to-end tests uit te voeren
 */

@Component
 public class DataController implements ApplicationListener<ContextRefreshedEvent> {

    public static final int BITCOIN_ID = 1;
    public static final int ETHEREUM_ID = 1027;
    public static final String BESTANDSNAAM_RANDOM_DATASET = "Sprint2/datacsv.csv"; // gebruikt om huidige database te vullen
    public static final int UITGANGSJAAR = 2021;
    private final Logger logger = LoggerFactory.getLogger(DataController.class);
    private final AssetService assetService;
    private final CryptoWaardeService cryptoWaardeService;
    private final AuthorizationService authorizationService;
    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final TransactieService transactieService;
    private final KlantService klantService;
    private final TriggerService triggerService;


    public DataController(RegistrationService registrationService,
                          AuthorizationService authorizationService, LoginService loginService, TransactieService transactieService,
                          AssetService assetService, CryptoWaardeService cryptoWaardeService, KlantService klantService, TriggerService triggerService) {
        this.registrationService = registrationService;
        this.authorizationService = authorizationService;
        this.transactieService = transactieService;
        this.loginService = loginService;
        this.assetService = assetService;
        this.cryptoWaardeService = cryptoWaardeService;
        this.klantService = klantService;
        this.triggerService = triggerService;
        logger.info("New DataController");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // gebruik hier onderstaande functies om data te genereren
    //   cryptoWaardeService.haalCryptoWaardes();



/*        TransactieDataRange transactieDataRange = new TransactieDataRange(60, 11);
        RandomTransactieRange randomTransactieRange = new RandomTransactieRange(2, 5);
        BankAlsTransactiePartij bankAlsTransactiePartij = new BankAlsTransactiePartij(false, false);

        RandomDataInput randomDataInput = new RandomDataInput(200, transactieDataRange, bankAlsTransactiePartij, randomTransactieRange);
        List<Transactie> transactieList = genereerRandomTransacties(randomDataInput);
        transactieList.forEach(System.out::println);*/

    }


    /**
     * Deze methode slaat klanten op die gegenereerd worden op basis van een csv-bestand en methodes uit Datagenerator
     *
     * @param hoeveelKlanten Bepaalt hoeveel klanten er worden opgeslagen,
     *                      zodat je niet vast zit aan de grootte van het bestand
     * @param bestandsnaam de naam van het csv-bestand
     * @throws IOException
     */

    public void vulKlantAdresEnRekeningTabel(int hoeveelKlanten, String bestandsnaam) throws IOException {
        List<Klant> list = DataGenerator.maakLijstKlantenVanCSV(bestandsnaam, hoeveelKlanten);
        for (Klant klant : list) {
            registrationService.registreerKlant(klant);
        }
    }

    /**
     * Deze methode genereert random transacties en slaat ze op
     * Het catch-block zorgt ervoor dat een transactie die niet slaagt niet meteen het hele proces stopt
     *
     * @param randomDataInput
     */
    public void slaRandomTransactiesOp(RandomDataInput randomDataInput){
        List<Transactie> transacties = genereerRandomTransacties(randomDataInput);
        transacties.sort(new TransactieComparator());
        for(Transactie transactie: transacties){
            try {
                Trigger triggerKoper = new TriggerKoper(transactie.getKoper(), transactie.getCryptomunt(), transactie.getPrijs(), transactie.getAantal());
                Trigger triggerVerkoper = new TriggerVerkoper(transactie.getVerkoper(), transactie.getCryptomunt(), transactie.getPrijs(), transactie.getAantal());
                transactieService.sluitTransactie(
                        transactie.getMomentTransactie(), triggerKoper, triggerVerkoper);
            }
            catch (NotEnoughCryptoException | BalanceTooLowException notEnoughCryptoException){
                System.out.println(transactie);
            }
        }
    }

    /**
     * Deze methode genereert een List random transacties door eerst een lijst
     * van mogelijke cryptomunten die gebruikt worden in de transacties
     * binnen te halen.
     *
     * @param randomDataInput Een object dat de gewenste restricties aan de dataset meegeeft
     * @return Een List van Transactie-objecten
     */
     public List<Transactie> genereerRandomTransacties(RandomDataInput randomDataInput){
        List<Cryptomunt> cryptomuntList = assetService.geefAlleCryptomunten();
        return creeerTransactieLijst(randomDataInput, cryptomuntList);
    }

    /**
     * Deze methode genereert een List van random transacties
     *
     * @param randomDataInput Een object dat de gewenste restricties aan de dataset meegeeft
     * @param cryptomuntList De lijst van mogelijke cryptomunten die gebruikt worden in de transacties
     * @return Een List van Transactie-objecten
     */
     private List<Transactie> creeerTransactieLijst(RandomDataInput randomDataInput, List<Cryptomunt> cryptomuntList) {
        List<Transactie> transactieList = new ArrayList<>();
        for (int i = 0; i < randomDataInput.getHoeveelTransacties(); i++) {
            Transactie transactie = getRandomTransactie(randomDataInput, cryptomuntList);
            transactieList.add(transactie);
        }
        return transactieList;
    }

    /**
     * Deze methode genereert een random Transactie-object op basis van meegegeven restricties
     *
     * @param randomDataInput Een object dat de gewenste restricties aan de dataset meegeeft
     * @param cryptomuntList De lijst van mogelijke cryptomunten die gebruikt worden in de transacties
     * @return Een random gegenereerd Transactie-object
     */
    private Transactie getRandomTransactie(RandomDataInput randomDataInput, List<Cryptomunt> cryptomuntList) {
        Cryptomunt cryptomunt = cryptomuntList.get((genereerRandomGetal(0, cryptomuntList.size() - 1, 1)));
        LocalDate cryptoDatum = LocalDate.of(UITGANGSJAAR, randomDataInput.getTransactieDataRange().getMaand(),
                genereerRandomGetal(1, 30, 1));
        double prijs = cryptoWaardeService.vindCryptoWaardeOpDatum(cryptomunt, cryptoDatum).getWaarde();
        int koperId = getRandomGebruikerId(randomDataInput.getTransactieDataRange().getGebruikerIdMinimum(),
                randomDataInput.getTransactieDataRange().getGebruikerIdMaximum(), randomDataInput.getBankAlsTransactiePartij().isBankAlsKoper());
        int verkoperId = setVerkoperId(randomDataInput, koperId);
        prijs  = prijs * getAfwijkingPrijs(koperId, verkoperId, randomDataInput.getRandomTransactieRange().getMaxAfwijkingPrijs());
        double randomAantal = getRandomAantal(cryptomunt, randomDataInput.getRandomTransactieRange().getMaxAantal());
        Trigger triggerKoper = getTriggerKoper(cryptomunt, prijs, koperId, randomAantal);
        Trigger triggerVerkoper = getTriggerVerkoper(cryptomunt, prijs, verkoperId, randomAantal);
        LocalDateTime randomDatumTijd = LocalDateTime.of(cryptoDatum, genereerRandomTijdstip());
        return new Transactie(randomDatumTijd, triggerKoper, triggerVerkoper);
    }

    /**
     * Maakt een TriggerVerkoper aan op basis van de random gegenereerde data
     * dat gebruikt wordt als input voor de Transactie-constructor
     *
     * @param cryptomunt De gebruikte cryptomunt
     * @param prijs De gegenereerde prijs
     * @param verkoperId  De gegenereerde gebruikerId van de verkoper
     * @param randomAantal Het gegenereerde aantal van de cryptomunt dat verhandeld wordt
     * @return Een TriggerVerkoper-object
     */
    private Trigger getTriggerVerkoper(Cryptomunt cryptomunt, double prijs, int verkoperId, double randomAantal) {
        Gebruiker verkoper = getTransactiepartij(verkoperId);
        return new TriggerVerkoper(verkoper, cryptomunt, prijs, randomAantal);
    }
    /**
     * Maakt een TriggerKoper aan op basis van de random gegenereerde data
     * dat gebruikt wordt als input voor de Transactie-constructor
     *
     * @param cryptomunt De gebruikte cryptomunt
     * @param prijs De gegenereerde prijs
     * @param koperId  De gegenereerde gebruikerId van de koper
     * @param randomAantal Het gegenereerde aantal van de cryptomunt dat verhandeld wordt
     * @return Een TriggerKoper-object
     */
    private Trigger getTriggerKoper(Cryptomunt cryptomunt, double prijs, int koperId, double randomAantal) {
        Gebruiker koper = getTransactiepartij(koperId);
        return new TriggerKoper(koper, cryptomunt, prijs, randomAantal);
    }

    /**
     * Deze methode maakt op basis van restricties een random gebruikerId voor de verkoper aan,
     * zodat deze niet gelijk is aan de gebruikerId van de koper
     *
     * @param randomDataInput Een object dat de gewenste restricties aan de dataset meegeeft
     * @param koperId De gebruikerId van de koper
     * @return
     */
    private int setVerkoperId(RandomDataInput randomDataInput, int koperId) {
        int verkoperId = 0;
        do {
            verkoperId = getRandomGebruikerId(randomDataInput.getTransactieDataRange().getGebruikerIdMinimum(),
                    randomDataInput.getTransactieDataRange().getGebruikerIdMaximum(),
                    randomDataInput.getBankAlsTransactiePartij().isBankAlsVerkoper());
        }
        while(koperId == verkoperId);
        return verkoperId;
    }

    private int getRandomGebruikerId(int gebruikerIdMinimum, int gebruikerIdMaximum, boolean bankAlsKoperOfVerkoper) {
        int gebruikerId = genereerRandomGetal(gebruikerIdMinimum, gebruikerIdMaximum, 1);
        if (bankAlsKoperOfVerkoper) {
            gebruikerId = 0;
        }
        return gebruikerId;
    }

    private double getAfwijkingPrijs(int koperId, int verkoperId, int maxAfwijkingsPercentage) {
        double afwijking = 0;
        if (koperId != 0 && verkoperId != 0) {
            afwijking = genereerRandomGetal(-100000 * maxAfwijkingsPercentage,
                    100000 * maxAfwijkingsPercentage, 1) / 10000000.0;

        }
        return afwijking;
    }

    /**
     * Deze methode genereert een random aantal voor de transactie, maar houdt rekening met de koers per eenheid
     *
     * @param cryptomunt De betreffende cryptomunt
     * @param maxAantal Het maximum aantal dat gegenereerd mag worden
     * @return Een random aantal voor de transactie
     */
    private double getRandomAantal(Cryptomunt cryptomunt, double maxAantal) {
        double randomAantal = genereerRandomGetal(0, (int) maxAantal * 1000, 1) / 1000.0;
        if(cryptomunt.getId() == BITCOIN_ID || cryptomunt.getId() == ETHEREUM_ID){ // bedrag wordt snel te hoog met deze munten
            randomAantal = genereerRandomGetal(0, 10, 1) / 1000.0;
        }
        return randomAantal;
    }

    private Gebruiker getTransactiepartij(int gebruikerId) {
        Gebruiker verkoper;
        if (gebruikerId == 0) {
            verkoper = Bank.getInstance();
        } else verkoper = klantService.vindKlantById(gebruikerId);
        return verkoper;
    }

    private LocalTime genereerRandomTijdstip() {
        int randomUur = genereerRandomGetal(0, 23, 1);
        int randomMinuut = genereerRandomGetal(0, 59, 1);
        int randomSeconde = genereerRandomGetal(0,59,1);
        return LocalTime.of(randomUur, randomMinuut, randomSeconde);
    }

    /**
     * Deze methode haalt een List binnen van verschillende cryptomunten met hun meest recente koers
     * Op basis van die koers genereert hij historische koersen en slaat die op.
     *
     * @param hoeveelWaarden er wordt per dag een cryptoWaarde gegenereerd, dit bepaalt hoeveel dagwaarden je genereert
     * @param afwijkingsPercentage hoeveel schommelt de koers per dag maximaal
     */

    public void slaHistorischeCryptoWaardenOp(int hoeveelWaarden, int afwijkingsPercentage){
        List<CryptoWaarde> cryptoWaardeList = cryptoWaardeService.haalMeestRecenteCryptoWaardes();
        for(CryptoWaarde cryptoWaarde: cryptoWaardeList){
            List<CryptoWaarde> cryptoWaardeHistorischeList = genereerHistorischeCryptoWaardesVanEenCryptomunt
                    (cryptoWaarde, hoeveelWaarden, afwijkingsPercentage);
            for(CryptoWaarde cryptoWaardeHistorisch: cryptoWaardeHistorischeList){
                cryptoWaardeService.slaCryptoWaardeOp(cryptoWaardeHistorisch);
            }
        }
    }

    /**
     * Deze methode gebruikt een echte koers van een cryptomunt en gaat steeds een dag terug in de tijd
     * en genereert een eerdere dagkoers op basis van meegegeven restricties
     *
     * @param cryptoWaarde de echte cryptowaarde die als startpunt wordt gebruikt
     * @param hoeveelWaarden er wordt per dag een cryptowaarde gegenereerd, dit bepaalt hoeveel dagwaarden je genereert
     * @param afwijkingsPercentage hoeveel schommelt de koers per dag maximaal
     * @return een lijst van gegenereerde cryptowaardes
     */
    public List<CryptoWaarde> genereerHistorischeCryptoWaardesVanEenCryptomunt(CryptoWaarde cryptoWaarde, int hoeveelWaarden, int afwijkingsPercentage){
        List<CryptoWaarde> cryptoWaardeList = new ArrayList<>();
        double afwijking = 0;
        double waarde = cryptoWaarde.getWaarde();
        for (int i = 0; i < hoeveelWaarden; i++) {
            afwijking = genereerRandomGetal(-100000 + afwijkingsPercentage, 100000 * afwijkingsPercentage, 1) / 10000000.0;
            CryptoWaarde oudereCryptoWaarde = new CryptoWaarde();
            oudereCryptoWaarde.setCryptomunt(cryptoWaarde.getCryptomunt());
            oudereCryptoWaarde.setWaarde(waarde * (1 + afwijking));
            oudereCryptoWaarde.setCryptoWaardeId("default");
            oudereCryptoWaarde.setDatum(cryptoWaarde.getDatum().minusDays(i + 1));
            cryptoWaardeList.add(oudereCryptoWaarde);
        }

        return cryptoWaardeList;
    }

    public static class TransactieComparator implements Comparator<Transactie>{

        @Override
        public int compare(Transactie o1, Transactie o2) {
            return o1.getMomentTransactie().compareTo(o2.getMomentTransactie());
        }
    }

    /**
     * Deze methode creeert een basisportefeuille voor de bank zodat deze kan handelen en slaat deze op.
     * Deze portefeuille bevat 20 verschillende cryptomunten
     */

    public void creeerPortefeuilleVoorBank(){
        Gebruiker bank = Bank.getInstance();
        List<Cryptomunt> cryptomuntList = assetService.geefAlleCryptomunten();
        for(Cryptomunt cryptomunt: cryptomuntList){
            Asset asset = new Asset(cryptomunt, 100000);
            asset.setGebruiker(bank);
            assetService.slaNieuwAssetOp(asset);
        }
        assetService.vulPortefeuilleVanGebruiker(bank);
    }
    public void integratieTestSluitTransactie(){
        Gebruiker klant = loginService.vindKlantByGebruikersnaam("LavernRoman");
        Gebruiker andereKlant = loginService.vindKlantByGebruikersnaam("ColumbusMccoy");
        Gebruiker bank = Bank.getInstance();
        List<Cryptomunt> cryptomuntList = assetService.geefAlleCryptomunten();

         //  cryptoWaardeService.haalCryptoWaardes();
        Cryptomunt bitcoin = cryptomuntList.get(0);
        CryptoWaarde bitcoinWaarde = cryptoWaardeService.vindMeestRecenteCryptoWaarde(bitcoin);
        Trigger triggerKoper = new TriggerKoper(klant, bitcoin, bitcoinWaarde.getWaarde(), 0.001);
        Trigger triggerVerkoper = triggerService.maakBankTrigger(triggerKoper);
        System.out.println(triggerVerkoper);
        Trigger triggerVerkoperTwee = new TriggerVerkoper(klant, bitcoin, bitcoinWaarde.getWaarde(), 0.01);
        Trigger triggerKoperTwee = triggerService.maakBankTrigger(triggerVerkoperTwee);
    //    transactieService.sluitTransactie(LocalDateTime.now(), triggerKoper,triggerVerkoper);

     //   transactieService.sluitTransactie(triggerVerkoperTwee, triggerKoperTwee, LocalDateTime.now());

        // trigger met bank als tegenpartij
       /* transactieService.sluitTransactie(bank, bitcoin, bitcoinWaarde.getWaarde(), bitcoinWaarde.getWaarde() * (1 + 0.02), 0.001, klant, LocalDateTime.now());
        // trigger met andere klant als tegenpartij
        transactieService.sluitTransactie(klant, bitcoin, bitcoinWaarde.getWaarde() * (1 - 0.02) , bitcoinWaarde.getWaarde() * (1 + 0.01), 0.001, andereKlant, LocalDateTime.now());
*/
    }
}
