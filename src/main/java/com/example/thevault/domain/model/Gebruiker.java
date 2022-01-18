// Created by carme
// Creation date 30/11/2021

package com.example.thevault.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public abstract class Gebruiker {
    protected int gebruikerId;
    protected String gebruikersnaam;
    protected String wachtwoord;
    protected Rekening rekening;
    protected List<Asset> portefeuille;
    protected List<Transactie> transacties;
    protected static int DEFAULT_GEBRUIKERID = 0;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Gebruiker.class);

    public Gebruiker() {
        super();
        logger.info("Lege Gebruiker, no args constructor");
    }


    public Gebruiker(String gebruikersnaam, String wachtwoord,  Rekening rekening,List<Asset> portefeuille,
                     List<Transactie> transacties){
        this.gebruikerId = DEFAULT_GEBRUIKERID;
        this.gebruikersnaam = gebruikersnaam;
        this.wachtwoord = wachtwoord;
        this.rekening = null;
        this.portefeuille = null;
        this.transacties = null;
        logger.info("Gebruiker " + this + "aangemaakt");
    }

    @Override
    public String toString() {
        return "Gebruiker{" +
                "gebruikerId=" + gebruikerId +
                ", gebruikersnaam='" + gebruikersnaam + '\'' +
                '}';
    }

    public int getGebruikerId() {
        return gebruikerId;
    }

    public void setGebruikerId(int gebruikerId) {
        this.gebruikerId = gebruikerId;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public Rekening getRekening() {
        return rekening;
    }

    public void setRekening(Rekening rekening) {
        this.rekening = rekening;
    }

    public List<Asset> getPortefeuille() {
        return portefeuille;
    }

    public void setPortefeuille(List<Asset> portefeuille) {
        this.portefeuille = portefeuille;
    }

    public List<Transactie> getTransacties() {
        return transacties;
    }

    public void setTransacties(List<Transactie> transacties) {
        this.transacties = transacties;
    }

    @Override // TODO juiste equals?
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gebruiker)) return false;
        Gebruiker gebruiker = (Gebruiker) o;
        return gebruikersnaam.equals(gebruiker.gebruikersnaam) && wachtwoord.equals(gebruiker.wachtwoord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gebruikerId, gebruikersnaam, wachtwoord);
    }
}
