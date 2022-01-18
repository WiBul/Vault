// Created by S.C. van Gils
// Creation date 26-12-2021

package com.example.thevault.domain.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class CryptoWaardenHistorischDto {

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(CryptoWaardenHistorischDto.class);

    private String[] datum;
    private double[] waarde;

    public CryptoWaardenHistorischDto() {
        super();
        logger.info("New CryptoWaardenHistorischDto");
    }

    public CryptoWaardenHistorischDto(String[] datum, double[] waarde){
        this.datum = datum;
        this.waarde = waarde;
    }

    public String[] getDatum() {
        return datum;
    }

    public void setDatum(String[] datum) {
        this.datum = datum;
    }

    public double[] getWaarde() {
        return waarde;
    }

    public void setWaarde(double[] waarde) {
        this.waarde = waarde;
    }
}
