// Created by carme
// Creation date 01/12/2021

package com.example.thevault.domain.mapping.repository;

import com.example.thevault.domain.mapping.dao.*;
import com.example.thevault.domain.model.*;
import com.example.thevault.support.exceptions.AssetNotExistsException;
import net.minidev.json.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.thevault.support.data.DataGenerator.genereerRandomGetal;

/**
 * Deze klasse zorgt ervoor dat de incomplete objecten uit de DAO's volledig gemaakt kunnen worden
 * door de DAO's hier met elkaar te combineren
 */

@Repository
public class RootRepository {

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(RootRepository.class);

    private final KlantDAO klantDAO;
    private final RekeningDAO rekeningDAO;
    private final AssetDAO assetDAO;
    private final CryptomuntDAO cryptomuntDAO;
    private final CryptoWaardeDAO cryptoWaardeDAO;
    private final AdresDAO adresDAO;
    private final TransactieDAO transactieDAO;

    @Autowired
    public RootRepository(KlantDAO klantDAO, RekeningDAO rekeningDAO, AssetDAO assetDAO, CryptomuntDAO cryptomuntDAO,
                          CryptoWaardeDAO cryptoWaardeDAO, AdresDAO adresDAO,
                          TransactieDAO transactieDAO) {
        super();
        this.rekeningDAO = rekeningDAO;
        this.klantDAO = klantDAO;
        this.assetDAO = assetDAO;
        this.cryptomuntDAO = cryptomuntDAO;
        this.cryptoWaardeDAO = cryptoWaardeDAO;
        this.adresDAO = adresDAO;
        this.transactieDAO = transactieDAO;
        logger.info("New RootRepository");
    }

    /**
     * Deze methode slaat de gegevens van een klant op in de database
     * op basis van een klant-object, de gebruikerId gaat van 0 naar de juiste
     *
     * @param klant het klant-object op basis van bij registratie ingevoerde gegevens
     * @return het klant-object met de juiste gebruikerId
     */
    public Klant slaKlantOp(Klant klant){
        klant.setAdres(adresDAO.slaAdresOp(klant.getAdres()));
        return klantDAO.slaKlantOp(klant);
    }

    /**
     * Deze methode zorgt ervoor dat een nieuw adres van een klant kan worden opgeslagen
     *
     * @param klant De betreffende klant
     * @param adres Het nieuwe adres
     * @return
     */
    public Adres slaNieuwAdresVanBestaandeKlantOp(Klant klant, Adres adres){
        adresDAO.slaAdresOp(adres);
        klant.setAdres(adres);
        klantDAO.updateKlant(klant);
        return adres;
    }

    /**
     * Deze methode zoekt of er in de database al een klant bestaat met deze gebruikersnaam
     * en maakt eventueel een klant-object aan op nasis van de teruggestuurde gegevens
     * Hier in de repository worden portefeuille en rekening toegevoegd
     *
     * @param gebruikersnaam gebruikersnaam van een (mogelijke) klant die uniek moet zijn
     * @return klant-object op basis van gegevens uit de database of null indien gebruikersnaam niet gevonden is
     */
    public Klant vindKlantByGebruikersnaam(String gebruikersnaam){
        Klant klant = klantDAO.vindKlantByGebruikersnaam(gebruikersnaam);
        maakKlantCompleet(klant);
        return klant;
    }
    private void maakKlantCompleet(Klant klant) {
        if(klant != null){
            klant.setRekening(vindRekeningVanGebruiker(klant));
            klant.setAdres(adresDAO.getAdresByKlant(klant));
            klant.setPortefeuille(vulPortefeuilleKlant(klant));
        }
    }

    /**
     * Deze methode zoekt of er in de database al een klant bestaat met deze gebruikerId
     * en maakt eventueel een klant-object aan op basis van de teruggestuurde gegevens
     * Hier in de repository worden portefeuille en rekening toegevoegd
     *
     * @param gebruikerId gebruikerId van een (mogelijke) klant die uniek moet zijn
     * @return klant-object op basis van gegevens uit de database of null indien gebruikerId niet gevonden is
     */
    public Klant vindKlantById(int gebruikerId){
        Klant klant = klantDAO.vindKlantById(gebruikerId);
        maakKlantCompleet(klant);
        return klant;
    }

    /**
     * Deze methode slaat de gegevens van een rekening op in de database via de methode in de rekeningDAO.
     *
     * @param rekening is de rekening die is aangemaakt bij een nieuwe klant.
     * @return de rekening behorend bij de nieuwe klant met klant-id
     */
    public Rekening slaRekeningOp(Rekening rekening){
        return rekeningDAO.slaRekeningOp(rekening);
    }


    /**
    * Methode die op gebruiker zoekt en de rekening teruggeeft
     *
     * @param gebruiker kan zowel een klant als een bank zijn
     * @return rekening geeft een rekening object terug
    * */
    public Rekening vindRekeningVanGebruiker(Gebruiker gebruiker){
        return rekeningDAO.vindRekeningVanGebruiker(gebruiker);
    }


    /**
     * Deze methode geeft het rekeningsaldo op van de klant in de database via de methode in de rekeningDAO.
     *
     * @param gebruiker is de klant van wie het rekeningsaldo wordt opgevraagd.
     * @return het rekeningsaldo behorend bij de klant met klant-id
     */
    public double vraagSaldoOpVanGebruiker(Gebruiker gebruiker){
        return rekeningDAO.vraagSaldoOpVanGebruiker(gebruiker);
    }

    /**
     * Deze methode wijzigt het rekeningsaldo van de klant in de database via de methode in de rekeningDAO.
     *
     * @param gebruiker is de klant van wie het rekeningsaldo wordt opgevraagd.
     * @param transactiebedrag is het bedrag waarnaar het saldo gewijzigd moet worden.
     * @return het rekeningsaldo behorend bij de klant met klant-id wordt gewijzigd.
     */
    public Rekening wijzigSaldoVanGebruiker(Gebruiker gebruiker, double transactiebedrag){
        return rekeningDAO.wijzigSaldoVanGebruiker(gebruiker, transactiebedrag);
    }

    /**
     * @Author Carmen
     * Dit betreft het vullen van de portefeuille met alle cryptomunten die er in zitten. Voor iedere asset
     * wordt alle informatie over de bijbehorende cryptomunt opgevraagd en meegegeven
     * @param gebruiker de klant die informatie opvraagt over de cryptomunt
     * @return List</Asset> een lijst van alle Assets (cryptomunten + hoeveelheden) in het bezit van de klant
     */
    public List<Asset> vulPortefeuilleKlant(Gebruiker gebruiker){
        List<Asset> portefeuille = assetDAO.geefAlleAssets(gebruiker);
        if(portefeuille.size() != 0){
        for (Asset asset: portefeuille) {
            Cryptomunt cryptomunt = cryptomuntDAO.geefCryptomunt(asset.getCryptomunt().getId());
            asset.setCryptomunt(cryptomunt);
        }
        gebruiker.setPortefeuille(portefeuille);
        }
        return portefeuille;
    }

    /**
     * Dit betreft het vinden van een specifieke cryptomunt die in de portefeuille zit
     * @param gebruiker de gebruiker die informatie opvraagt over de cryptomunt
     * @param cryptomunt cryptomunt waarover informatie wordt opgevraagd
     * @return Asset de asset (cryptomunt + aantal) waarover informatie is opgevraagd
     */
    public Asset geefAssetVanGebruiker(Gebruiker gebruiker, Cryptomunt cryptomunt){
        return assetDAO.geefAssetGebruiker(gebruiker, cryptomunt).orElseThrow(AssetNotExistsException::new);
    }

    /**
     * Dit betreft het toevoegen van een cryptomunt die nog niet in de portefeuille zit
     * Dit gebeurt via een 'transactie', waarbij een klant crypto's koopt
     * @param asset de cryptomunt en het aantal dat de klant aanschaft
     * @return Asset de asset die de klant heeft toegevoegd
     */
    public Asset slaNieuwAssetVanKlantOp(Asset asset){
        return assetDAO.voegNieuwAssetToeAanPortefeuille(asset);
    }

    /**
     * Dit betreft het wijzigen van een cryptomunt die al in de portefeuille zit
     * Dit gebeurt via een 'transactie', waarbij een klant crypto's koopt of verkoopt
     * @param gebruiker de handelende partij
     * @param cryptomunt de munt waarin gehandeld wordt
     * @param aantal de hoeveelheid die verhandeld wordt
     * @return Asset de asset na de update, waarbij het nieuwe aantal wordt meegegeven
     */
    public Asset wijzigAssetVanKlant(Gebruiker gebruiker, Cryptomunt cryptomunt, double aantal){
        return assetDAO.updateAsset(gebruiker, cryptomunt , aantal);
    }

    /**
     * Deze methode haalt uit de database de waarde die de betreffende cryptomunt vandaag heeft
     * zodat huidige waarde van assets kan worden berekend
     *
     * @param cryptomunt de cryptomunt
     * @return CryptoWaarde-object om huidige waarde van asset te kunnen berekenen
     */
    //CryptoWaarde wordt eens per dag opgehaald om 00.00 uur
    public CryptoWaarde haalMeestRecenteCryptoWaarde(Cryptomunt cryptomunt){
        CryptoWaarde cryptoWaarde = cryptoWaardeDAO.getCryptoWaardeByCryptomuntAndDate(cryptomunt, LocalDate.now());
        cryptoWaarde.setCryptomunt(cryptomunt);
        return cryptoWaarde;
    }

    /**
     * Deze methode haalt de koers van een cryptomunt op een bepaalde dag op
     *
     * @param cryptomunt De betreffende cryptomunt
     * @param datum De datum waarop gezocht wordt
     * @return
     */
    public CryptoWaarde haalCryptoWaardeOpDatum(Cryptomunt cryptomunt, LocalDate datum){
        CryptoWaarde cryptoWaarde = cryptoWaardeDAO.getCryptoWaardeByCryptomuntAndDate(cryptomunt, datum);
        cryptoWaarde.setCryptomunt(cryptomunt);
        return cryptoWaarde;
    }
    /**
     * Deze methode slaat een cryptowaarde op in de database
     *
     * @param cryptoWaarde De betreffende cryptowaarde
     * @return
     */
    public CryptoWaarde slaCryptoWaardeOp(CryptoWaarde cryptoWaarde){
        return cryptoWaardeDAO.slaCryptoWaardeOp(cryptoWaarde);
    }

    /**
     * Deze methode haalt de hele historie van koersen van een cryptomunt op
     *
     * @param cryptomunt  De betreffende cryptomunt
     * @return
     */
    public List<CryptoWaarde> haalAlleCryptoWaardesVanCryptomunt(Cryptomunt cryptomunt){
        List<CryptoWaarde> cryptoWaardeList = cryptoWaardeDAO.getCryptoWaardeByCryptomunt(cryptomunt);
        for(CryptoWaarde cryptoWaarde: cryptoWaardeList){
            cryptoWaarde.setCryptomunt(cryptomunt);
        }
        return cryptoWaardeList;
    }

    public Transactie slaTransactieOp(Transactie transactie){
        return transactieDAO.slaTransactieOp(transactie);
    }

    /**
     * methode die alle transacties die bij een klant horen teruggeeft
     * hierbij worden eerst de koper, verkoper en cryptomunt op id uit de database
     * gehaald en toegevoegd aan het transactie object
     *
     * @param gebruiker de klant waarvan alle transacties moeten worden opgezocht
     * @return lijst transacties van de klant
     */
    List<Transactie> geefTransactiesVanGebruiker(Gebruiker gebruiker){
        List<Transactie> transactiesVanKlant = transactieDAO.geefTransactiesVanGebruiker(gebruiker);
        for (Transactie transactie: transactiesVanKlant) {
            transactie.setKoper(klantDAO.vindKlantById(transactie.getKoper().getGebruikerId()));
            transactie.setVerkoper(klantDAO.vindKlantById(transactie.getVerkoper().getGebruikerId()));
            transactie.setCryptomunt(cryptomuntDAO.geefCryptomunt(transactie.getCryptomunt().getId()));
        }
        return transactiesVanKlant;
    }


    /**
     * methode die alle transacties die bij een klant horen die in een bepaalde
     * periode hebben plaatsgevonden teruggeeft
     * @param gebruiker
     * @param startDatum
     * @param eindDatum de klant waarvan alle transacties moeten
     * worden opgezocht, en data vanaf en tot wanneer de transacties plaatsvonden
     * @return lijst transacties van de klant
     */
    List<Transactie> geefTransactiesVanGebruikerInPeriode(Gebruiker gebruiker, Timestamp startDatum, Timestamp eindDatum){
        List<Transactie> transactiesVanKlant =  transactieDAO.geefTransactiesVanGebruikerInPeriode(gebruiker, startDatum, eindDatum);
        for (Transactie transactie: transactiesVanKlant) {
            transactie.setKoper(klantDAO.vindKlantById(transactie.getKoper().getGebruikerId()));
            transactie.setVerkoper(klantDAO.vindKlantById(transactie.getVerkoper().getGebruikerId()));
            transactie.setCryptomunt(cryptomuntDAO.geefCryptomunt(transactie.getCryptomunt().getId()));
        }
        return transactiesVanKlant;
    }

    /**
     * methode die alle transacties binnen een bepaalde periode teruggeeft
     * @params startDatum en eindDatum periode
     * @return lijst transacties van die periode
     */
    List<Transactie> geefAlleTransactiesInPeriode(Timestamp startDatum, Timestamp eindDatum){
        return transactieDAO.geefAlleTransactiesInPeriode(startDatum, eindDatum);
    }

    /**
     * methode die alle transacties die bij een klant horen met een bepaalde cryptomunt
     * @params gebruiker cryptomunt
     * @return lijst transacties van de klant met de meegegeven cryptomunt
     */
    List<Transactie> geefTransactiesVanGebruikerMetCryptomunt(Gebruiker gebruiker, Cryptomunt cryptomunt){
        List<Transactie> transactieList = transactieDAO.geefTransactiesVanGebruikerMetCryptomunt(gebruiker, cryptomunt);
        for (Transactie transactie : transactieList) {
            transactie.setCryptomunt(cryptomunt);
            transactie.setKoper(vindKlantById(transactie.getKoper().getGebruikerId()));
            transactie.setVerkoper(vindKlantById(transactie.getVerkoper().getGebruikerId()));
        }
        return transactieList;
    }

    public double geefAssetVanGebruikerOrElseNull(Gebruiker gebruiker, Cryptomunt cryptomunt){
        return assetDAO.geefAantalCryptoInEigendom(gebruiker, cryptomunt);
    }


    public Cryptomunt geefCryptomunt(int cryptomuntId){
        return cryptomuntDAO.geefCryptomunt(cryptomuntId);
    }
    public List<Cryptomunt> geefAlleCryptomunten(){
        return cryptomuntDAO.geefAlleCryptomunten();
    }


}
