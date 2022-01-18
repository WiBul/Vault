// Created by S.C. van Gils
// Creation date 24-12-2021

package com.example.thevault.service;

import com.example.thevault.domain.mapping.repository.RootRepository;
import com.example.thevault.domain.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO Deze klasse moet periodiek zoeken?

@Service
public class TriggerService {

    private final RootRepository rootRepository;
    private final CryptoWaardeService cryptoWaardeService;

    private final Logger logger = LoggerFactory.getLogger(TriggerService.class);

    public TriggerService(RootRepository rootRepository, CryptoWaardeService cryptoWaardeService) {
        super();
        this.rootRepository = rootRepository;
        this.cryptoWaardeService = cryptoWaardeService;
        logger.info("New TriggerService");
    }
    
    public Trigger slaTriggerOp(Trigger trigger){
        // eerst kijken of iemand in staat is om mogelijke transactie te betalen
        // dan wel of iemand genoeg van een asset heeft om te verkopen
        // TODO eerst in javascript al nakijken
        
        // sla trigger op
        
        return trigger;
    }
    
    public void zoekNaarMatch(Trigger trigger){
        // haal lijst van prijzen binnen: nieuwe dagkoersen en lijst van aanbieders / kopers
        List<CryptoWaarde> cryptoWaardeList = cryptoWaardeService.haalMeestRecenteCryptoWaardes(); //TODO rootrepository-versie maken?
        
        // vergelijk met triggerPrijs

        // vergelijk met aantal (alleen bij perfecte match?)

        // triggers weghalen van klanten die trigger niet meer kunnen betalen?
    }

    public Transactie sluitTransactieAf(Trigger triggerKoper, Trigger triggerVerkoper){
        // via transactionService proberen transactie af te sluiten
        // wat te doen als dit niet kan? Trigger verwijderen?
        vergelijkTriggerMetSaldo(triggerKoper);
        vergelijkTriggerMetAsset(triggerVerkoper);
        checkTriggers(triggerKoper, triggerVerkoper);


        return null;
    }
    public void checkTriggers(Trigger triggerKoper, Trigger triggerVerkoper){
        checkKoper(triggerKoper);
        checkVerkoper(triggerVerkoper);
        checkAantalGelijk(triggerKoper, triggerVerkoper);
        checkCryptomuntGelijk(triggerKoper, triggerVerkoper);
        checkAantalPositief(triggerKoper, triggerVerkoper);
        checkPrijs(triggerKoper, triggerVerkoper);

    }

    public void checkVerkoper(Trigger triggerVerkoper) {
        if(isKoper(triggerVerkoper)){
            // throw exception
        }
    }
    public void checkKoper(Trigger triggerKoper) {
        if(!(isKoper(triggerKoper))){
            // throw exception
        }
    }

    public void checkAantalGelijk(Trigger triggerKoper, Trigger triggerVerkoper){
        if(triggerKoper.getAantal() != triggerVerkoper.getAantal()){
            // throw exception
        }
    }
    public void checkCryptomuntGelijk(Trigger triggerKoper, Trigger triggerVerkoper){
        if(!triggerKoper.getCryptomunt().equals(triggerVerkoper.getCryptomunt())){
            // throw exception
        }
    }
    public void checkAantalPositief(Trigger triggerKoper, Trigger triggerVerkoper){
        if(triggerKoper.getAantal() <= 0 || triggerVerkoper.getAantal() <= 0){
            // throw exception
        }
    }
    public void checkPrijs(Trigger triggerKoper, Trigger triggerVerkoper){
        if(triggerKoper.getTriggerPrijs() < triggerVerkoper.getTriggerPrijs()){
            // throw exception
        }
    }

    public Trigger vergelijkTriggerMetSaldo(Trigger trigger){

        return trigger;
    }
    public Trigger vergelijkTriggerMetAsset(Trigger trigger){

        return trigger;
    }
    // moet dit periodieke functie worden?
    public void vergelijkAlleTriggersMetBeschikbareFondsen(){
        // Lijst van alle triggers binnenhalen

        // bovenstaande methodes in loop langsgaan
    }

    public Trigger maakBankTrigger(Trigger klantTrigger){
        double prijs = rootRepository.haalMeestRecenteCryptoWaarde(klantTrigger.getCryptomunt()).getWaarde();
        double aantal = klantTrigger.getAantal();
        Cryptomunt cryptomunt = klantTrigger.getCryptomunt();
        if(isKoper(klantTrigger)){
            return new TriggerVerkoper(Bank.getInstance(), cryptomunt, prijs, aantal);
        }
        return new TriggerKoper(Bank.getInstance(), cryptomunt, prijs, aantal);
    }
    public boolean isKoper(Trigger trigger){
        if(trigger instanceof TriggerKoper){
            return true;
        }
        else return false;
    }
}
