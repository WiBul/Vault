// Created by carme
// Creation date 30/11/2021

package com.example.thevault.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Klant extends Gebruiker {
    private String naam;
    private Adres adres;
    private long bsn;
    private Rekening rekening;
    private List<Asset> portefeuille;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate geboortedatum;
    //TODO lijst van transacties toevoegen

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Klant.class);

    public Klant(){
        super();
        logger.info("Lege klant, no args constructor");
    }
    //TODO navragen of all-args eigenlijk wel nodig is
    public Klant(String gebruikersnaam, String wachtwoord,
                 List<Asset> portefeuille, Rekening rekening, List<Transactie> transacties,
                 String naam, Adres adres, long bsn, LocalDate geboortedatum) {
        super(gebruikersnaam, wachtwoord,rekening, portefeuille, transacties);
        this.naam = naam;
        this.adres = adres;
        this.bsn = bsn;
        this.geboortedatum = geboortedatum;
        this.rekening = rekening;
        this.portefeuille = portefeuille;
        logger.info("New Klant, all args constructor");
    }
    public Klant(String gebruikersnaam, String wachtwoord,
                 String naam, long bsn, LocalDate geboortedatum){
        this(gebruikersnaam, wachtwoord, null, null, null, naam, null, bsn, geboortedatum);
        logger.info("New Klant, rowMapperConstructor");
    }
    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public long getBsn() {
        return bsn;
    }

    public void setBsn(long bsn) {
        this.bsn = bsn;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }


    @Override
    public String toString() {
        return  "Klant{" + super.toString() +
                ", naam='" + naam + '\'' +
                ", adres=" + adres +
                ", BSN=" + bsn +
                ", rekening=" + rekening +
                ", portefeuille=" + portefeuille +
                ", geboortedatum=" + geboortedatum +
                '}';
    }
//TODO nadenken over noodzakelijke velden
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Klant)) return false;
        if (!super.equals(o)) return false;
        Klant klant = (Klant) o;
        return bsn == klant.bsn && naam.equals(klant.naam) && geboortedatum.equals(klant.geboortedatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), naam, adres, bsn, rekening, portefeuille, geboortedatum);
    }
}
