// Created by S.C. van Gils
// Creation date 8-1-2022

package com.example.thevault.support.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deze klasse bundelt het mmximum aantal cryptomunten dat in de random transactie verhandeld wordt
 * met de maximum afwijking van de dagkoers
 */

public class RandomTransactieRange {

    private final Logger logger = LoggerFactory.getLogger(RandomTransactieRange.class);

    private double maxAantal;
    private int maxAfwijkingPrijs;

    public RandomTransactieRange() {
        super();
        logger.info("New RandomTransactieRange");
    }

    public RandomTransactieRange(double maxAantal, int maxAfwijkingPrijs) {
        this.maxAantal = maxAantal;
        this.maxAfwijkingPrijs = maxAfwijkingPrijs;
    }

    public double getMaxAantal() {
        return maxAantal;
    }

    public int getMaxAfwijkingPrijs() {
        return maxAfwijkingPrijs;
    }
}
