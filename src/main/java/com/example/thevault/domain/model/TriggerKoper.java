// Created by S.C. van Gils
// Creation date 12-1-2022

package com.example.thevault.domain.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerKoper extends Trigger{

    private final Logger logger = LoggerFactory.getLogger(TriggerKoper.class);

    public TriggerKoper() {
        super();
        logger.info("New TriggerKoper");
    }

    public TriggerKoper(Gebruiker gebruiker, Cryptomunt cryptomunt, double triggerPrijs, double aantal) {
        super(gebruiker, cryptomunt, triggerPrijs, aantal);
    }

    public TriggerKoper(double triggerPrijs, double aantal) {
        super(triggerPrijs, aantal);
    }

    @Override
    public String toString() {
        return "TriggerKoper:" + super.toString();
    }
}
