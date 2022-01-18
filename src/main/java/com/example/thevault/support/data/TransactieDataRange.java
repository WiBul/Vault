// Created by S.C. van Gils
// Creation date 8-1-2022

package com.example.thevault.support.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deze klasse bundelt beperkingen die meegegeven worden aan een gegenereerde lijst van transacties.
 * Er kan worden gekozen welke groep gebruikers met elkaar kan handelen en in welke maand
 * de transacties plaatsvinden.
 */

public class TransactieDataRange {

    private final Logger logger = LoggerFactory.getLogger(TransactieDataRange.class);


    private int gebruikerIdMinimum;
    private int gebruikerIdMaximum;
    private int maand;

    public TransactieDataRange() {
        super();
        logger.info("New TransactieDataRange");
    }

    public TransactieDataRange(int gebruikerIdMinimum, int gebruikerIdMaximum, int maand) {
        this.gebruikerIdMinimum = gebruikerIdMinimum;
        this.gebruikerIdMaximum = gebruikerIdMaximum;
        this.maand = maand;
    }

    public int getMaand() {
        return maand;
    }

    public int getGebruikerIdMinimum() {
        return gebruikerIdMinimum;
    }

    public int getGebruikerIdMaximum() {
        return gebruikerIdMaximum;
    }
}
