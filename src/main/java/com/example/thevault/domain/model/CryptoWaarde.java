// Created by S.C. van Gils
// Creation date 14-12-2021

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

public class CryptoWaarde {

    private String cryptoWaardeId;
    private Cryptomunt cryptomunt;
    private double waarde;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate datum;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(CryptoWaarde.class);
    public CryptoWaarde() {
        super();
        logger.info("New CryptoWaarde");
    }

    public CryptoWaarde(String cryptoWaardeId, Cryptomunt cryptomunt, double waarde, LocalDate datum){
        this.cryptoWaardeId = cryptoWaardeId;
        this.cryptomunt = cryptomunt;
        this.waarde = waarde;
        this.datum = datum;
    }


    public String getCryptoWaardeId() {
        return cryptoWaardeId;
    }

    public void setCryptoWaardeId(String cryptoWaardeId) {
        this.cryptoWaardeId = cryptoWaardeId;
    }

    public Cryptomunt getCryptomunt() {
        return cryptomunt;
    }

    public void setCryptomunt(Cryptomunt cryptomunt) {
        this.cryptomunt = cryptomunt;
    }

    public double getWaarde() {
        return waarde;
    }

    public void setWaarde(double waarde) {
        this.waarde = waarde;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "CryptoWaarde{" +
                "cryptoWaardeId='" + cryptoWaardeId + '\'' +
                ", cryptomunt=" + cryptomunt +
                ", waarde=" + waarde +
                ", datum=" + datum +
                '}';
    }
}
