// Created by S.C. van Gils
// Creation date 20-12-2021

package com.example.thevault.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Een trigger is een waarde die een actie inzet:
 * In dit geval een maximale koopprijs of een minimale verkoopprijs van een specifieke cryptomunt
 * met een bepaalde gevraagde of aangeboden hoeveelheid
 */

public abstract class Trigger {

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Trigger.class);

    protected Gebruiker gebruiker;
    protected  Cryptomunt cryptomunt;
    protected  double triggerPrijs;
    protected  double aantal;

    public Trigger() {
        super();
        logger.info("New Trigger");
    }
    public Trigger(Gebruiker gebruiker, Cryptomunt cryptomunt, double triggerPrijs, double aantal){
        super();
        this.gebruiker = gebruiker;
        this.cryptomunt = cryptomunt;
        this.triggerPrijs = triggerPrijs;
        this.aantal = aantal;

        logger.info("New Trigger, all-args constructor");
    }
    public Trigger(double triggerPrijs, double aantal) {
        this(null, null, triggerPrijs,aantal);
        logger.info("New Trigger, RowMapper constructor");
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public double getTriggerPrijs() {
        return triggerPrijs;
    }

    public void setTriggerPrijs(double triggerPrijs) {
        this.triggerPrijs = triggerPrijs;
    }

    public Cryptomunt getCryptomunt() {
        return cryptomunt;
    }

    public void setCryptomunt(Cryptomunt cryptomunt) {
        this.cryptomunt = cryptomunt;
    }

    public double getAantal() {
        return aantal;
    }

    public void setAantal(double aantal) {
        this.aantal = aantal;
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "gebruiker=" + gebruiker +
                ", cryptomunt=" + cryptomunt +
                ", triggerPrijs=" + triggerPrijs +
                ", aantal=" + aantal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trigger)) return false;
        Trigger trigger = (Trigger) o;
        return Double.compare(trigger.triggerPrijs, triggerPrijs) == 0 && Double.compare(trigger.aantal, aantal) == 0 && Objects.equals(gebruiker, trigger.gebruiker) && Objects.equals(cryptomunt, trigger.cryptomunt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gebruiker, cryptomunt, triggerPrijs, aantal);
    }
}
