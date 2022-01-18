// Created by E.S. Olthof
// Creation date 12/15/2021

package com.example.thevault.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

public class Transactie{
    private int transactieId;
    private double aantal;
    private LocalDateTime momentTransactie;
    private double prijs;
    private Gebruiker koper;
    private Gebruiker verkoper;
    private Cryptomunt cryptomunt;
    private double bankFee;
    public static final double DEEL_PRIJSVERSCHIL_KOPER = 0.5;
    public static final double DEEL_PRIJSVERSCHIL_VERKOPER = 1 - DEEL_PRIJSVERSCHIL_KOPER;


    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Rekening.class);

    public Transactie() {
        super();
        logger.info("lege Transactie, no args constructor");
    }

    public Transactie(LocalDateTime momentTransactie,
                      Trigger triggerKoper, Trigger triggerVerkoper) {
        this.momentTransactie = momentTransactie;
        this.aantal = triggerKoper.getAantal();
        this.cryptomunt = triggerKoper.getCryptomunt();
        this.prijs = getPrijsViaTrigger(triggerKoper, triggerVerkoper);
        this.koper = triggerKoper.getGebruiker();
        this.verkoper = triggerVerkoper.getGebruiker();
        this.bankFee = Bank.getInstance().getFee();
        logger.info("New "+ this + " aangemaakt");
    }
    public static double getPrijsViaTrigger(Trigger triggerKoper, Trigger triggerVerkoper){
        return triggerKoper.getTriggerPrijs() * DEEL_PRIJSVERSCHIL_KOPER + triggerVerkoper.getTriggerPrijs() * DEEL_PRIJSVERSCHIL_VERKOPER;
    }

    public Transactie(LocalDateTime momentTransactie,
                      Gebruiker verkoper, Cryptomunt cryptomunt, double prijs, double aantal,
                      Gebruiker koper) {
        this.aantal = aantal;
        this.momentTransactie = momentTransactie;
        this.cryptomunt = cryptomunt;
        this.prijs = prijs;
        this.koper = koper;
        this.verkoper = verkoper;
        this.bankFee = Bank.getInstance().getFee();
        logger.info("New "+ this + " aangemaakt");
    }

    public int getTransactieId() {
        return transactieId;
    }

    public void setTransactieId(int transactieId) {
        this.transactieId = transactieId;
    }

    public double getAantal() {
        return aantal;
    }

    public void setAantal(double aantal) {
        this.aantal = aantal;
    }

    public LocalDateTime getMomentTransactie() {
        return momentTransactie;
    }

    public void setMomentTransactie(LocalDateTime momentTransactie) {
        this.momentTransactie = momentTransactie;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public Gebruiker getKoper() {
        return koper;
    }

    public void setKoper(Gebruiker koper) {
        this.koper = koper;
    }

    public Gebruiker getVerkoper() {
        return verkoper;
    }

    public void setVerkoper(Gebruiker verkoper) {
        this.verkoper = verkoper;
    }

    public Cryptomunt getCryptomunt() {
        return cryptomunt;
    }

    public void setCryptomunt(Cryptomunt cryptomunt) {
        this.cryptomunt = cryptomunt;
    }

    public Double getBankFee() {
        return bankFee;
    }

    public void setBankFee(Double bankFee) {
        this.bankFee = bankFee;
    }

    @Override
    public String toString() {
        return "Transactie{" +
                "transactieId=" + transactieId +
                ", aantal=" + aantal +
                ", datumTijd=" + momentTransactie +
                ", prijs=" + prijs +
                ", koper=" + koper +
                ", verkoper=" + verkoper +
                ", cryptomunt=" + cryptomunt +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transactie)) return false;
        Transactie that = (Transactie) o;
        return getTransactieId() == that.getTransactieId() && Double.compare(that.getAantal(), getAantal()) == 0 && Double.compare(that.getPrijs(), getPrijs()) == 0 && Objects.equals(getMomentTransactie(), that.getMomentTransactie()) && Objects.equals(getKoper(), that.getKoper()) && Objects.equals(getVerkoper(), that.getVerkoper()) && Objects.equals(getCryptomunt(), that.getCryptomunt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactieId(), getAantal(), getMomentTransactie()
                , getPrijs(), getKoper(), getVerkoper(), getCryptomunt());
    }
}
