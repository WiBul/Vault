// Created by S.C. van Gils
// Creation date 7-1-2022

/**
 * Deze klasse groepeert de input voor het creëren van data voor het vullen van transactietabel in de database.
 * De database is al gevuld met klanten die een rekening hebben met een saldo van 1000 euro en een lege portefeuille.
 * De booleans bankAlsKoper en bankAlsVerkoper zijn handig om transacties met alleen de bank als tegenpartij te genereren zodat
 * de startsaldi en -portefeuilles minder een probleem zijn om genoeg data te genereren.
 */

package com.example.thevault.support.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deze klasse bundelt de restricties die op de gegenereerde transacties zitten.
 * De hoeveelheid transacties die gegenereerd moet worden, wordt hier los bepaald,
 * De overige restricties, die bepalen hoe de transactie er uit kan zien, zijn in objecten gebundeld.
 */

public class RandomDataInput {

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(RandomDataInput.class);

    private int hoeveelTransacties;
    private TransactieDataRange transactieDataRange;
    private BankAlsTransactiePartij bankAlsTransactiePartij;
    private RandomTransactieRange randomTransactieRange;

    public RandomDataInput(int hoeveelTransacties, TransactieDataRange transactieDataRange,
                           BankAlsTransactiePartij bankAlsTransactiePartij, RandomTransactieRange randomTransactieRange) {
        this.hoeveelTransacties = hoeveelTransacties;
        this.transactieDataRange = transactieDataRange;
        this.bankAlsTransactiePartij = bankAlsTransactiePartij;
        this.randomTransactieRange = randomTransactieRange;
    }

    public RandomDataInput() {
        super();
        logger.info("New RandomDataInput");
    }

    public TransactieDataRange getTransactieDataRange() {
        return transactieDataRange;
    }

    public RandomTransactieRange getRandomTransactieRange() {
        return randomTransactieRange;
    }


    public int getHoeveelTransacties() {
        return hoeveelTransacties;
    }

    public BankAlsTransactiePartij getBankAlsTransactiePartij() {
        return bankAlsTransactiePartij;
    }


}
