package com.example.thevault.service;

import com.example.thevault.domain.mapping.repository.RootRepository;
import com.example.thevault.domain.model.Gebruiker;
import com.example.thevault.domain.model.Klant;
import com.example.thevault.domain.model.Rekening;
import com.example.thevault.support.exceptions.UserNotExistsException;
import net.minidev.json.annotate.JsonIgnore;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Ju-Sen m.u.v. methode creeerIban
 * Servicelaag voor rekening waarin een rekening aangemaakt kan worden en informatie over een rekening opgevraagd of aangepast kan worden.
 */

@Service
public class RekeningService {

    private RootRepository rootRepository;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(RekeningService.class);

    private final Double STARTSALDO = 1000.0;


    @Autowired
    public RekeningService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;
        logger.info("Nieuwe RekeningService.");
    }
    /**
     * @Author Wim Bultman
     * creeert IBAN, mbv org.iban4j.Iban library, zie https://github.com/arturmkrtchyan/iban4j
     */
    public static Iban creeerIban(){
        Iban iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("TVLT").buildRandom();
        return iban;
    }

    /**
     * Deze methode zorgt ervoor dat er een rekening wordt aangemaakt voor een nieuwe klant
     * met een juiste iban en vast startsaldo.
     *
     * @param klant is de klant die meegegeven wordt waarvoor de rekening wordt aangemaakt.
     * @return de aangemaakte rekening wordt teruggegeven.
     */
    public Rekening creeerRekening(Klant klant) {
        String iban = creeerIban().toString();
        Rekening rekening = new Rekening(iban, STARTSALDO);
        klant.setRekening(rekening);
        rekening.setGebruiker(klant);
        return rekening;
    }

    /**
     * Deze methode zorgt ervoor dat een aangemaakte rekening wordt opgeslagen.
     *
     * @param rekening is de meegegeven rekening welke opgeslagen moet worden.
     * @return de aangemaakte rekening wordt doorgegeven aan de root repository
     * voor het verdere opslagproces via de DAO in de database.
     */
    public Rekening slaRekeningOp(Rekening rekening){
        return rootRepository.slaRekeningOp(rekening);
    }

    /**
     * Met deze methode kan je met een opgegeven klant de rekening opvragen als de klant
     * terug te vinden is in de database.
     *
     * @param klant is de meegegeven klant voor wie je de rekening op wilt vragen.
     * @throws UserNotExistsException als er geen klant wordt meegegeven of als de
     * gebruikersnaam niet bestaat, wordt er een exceptie gegooid.
     * @return als de gebruikersnaam overeenkomt met de gebruikersnaam in de database dan
     * wordt de opgevraagde rekening teruggegeven.
     */
 /*   public Rekening vindRekening(Klant klant) throws UserNotExistsException {
        if (klant == null){
            throw new UserNotExistsException();
        }
        if (rootRepository.vindKlantByGebruikersnaam(klant.getGebruikersnaam()) == null ){
            throw new UserNotExistsException();
        }
        return rootRepository.vindRekeningVanKlant(klant);
    }*/

    /**
     * Met deze methode kan je met een opgegeven klant de rekening opvragen als de klant
     * terug te vinden is in de database.
     *
     * @param gebruiker is de meegegeven klant voor wie je de rekening op wilt vragen.
     * @throws UserNotExistsException als er geen klant wordt meegegeven of als de
     * gebruikersnaam niet bestaat, wordt er een exceptie gegooid.
     * @return als de gebruikersnaam overeenkomt met de gebruikersnaam in de database dan
     * wordt de opgevraagde rekening teruggegeven.
     */
    public Rekening vindRekening(Gebruiker gebruiker) throws UserNotExistsException {
        if (gebruiker == null){
            throw new UserNotExistsException();
        }
        if (rootRepository.vindKlantByGebruikersnaam(gebruiker.getGebruikersnaam()) == null ){
            throw new UserNotExistsException();
        }
        return rootRepository.vindRekeningVanGebruiker(gebruiker);
    }

    /**
     * Met deze methode kan je het rekeningsaldo van de klant opvragen als de klant
     * terug te vinden is in de database.
     *
     * @param gebruiker is de meegegeven klant voor wie je het rekeningsaldo op wilt vragen.
     * @throws UserNotExistsException als er geen klant wordt meegegeven of als de
     * gebruikersnaam niet bestaat, wordt er een exceptie gegooid.
     * @return als de gebruikersnaam overeenkomt met de gebruikersnaam in de database dan
     * wordt het saldo van de opgevraagde rekening teruggegeven.
     */
    public double vraagSaldoOp(Gebruiker gebruiker) throws UserNotExistsException{
        return vindRekening(gebruiker).getSaldo();
    }

    /**
     * Met deze methode kan je het rekeningsaldo van de klant wijzigen als de klant
     * terug te vinden is in de database.
     *
     * @param gebruiker is de meegegeven klant voor wie je het rekeningsaldo op wilt wijzigen.
     * @param transactiebedrag is het bedrag waarnaar je het wil wijzigen.
     * @throws UserNotExistsException als er geen klant wordt meegegeven of als de
     * gebruikersnaam niet bestaat, wordt er een exceptie gegooid.
     * @return als de gebruikersnaam overeenkomt met de gebruikersnaam in de database dan
     * wordt het saldo van de opgevraagde rekening gewijzigd naar het opgegeven bedrag.
     */

    //parameter bedrag = transactiebedrag NIET saldo van rekening
    //IPV rekening geven we een klant mee
    public Rekening wijzigSaldo(Gebruiker gebruiker, double transactiebedrag) {
        return rootRepository.wijzigSaldoVanGebruiker(gebruiker, transactiebedrag);
    }


// TODO verwijderen
    public RootRepository getRootRepository() {
        return rootRepository;
    }
}
