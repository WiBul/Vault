// Created by carme
// Creation date 30/11/2021

package com.example.thevault.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cryptomunt {

    private int Id;
    private String name;
    private String symbol;

    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(Cryptomunt.class);

    public Cryptomunt() {
        super();
        logger.info("New Cryptomunt, no args constructor");
    }

    /**
     * @Author Carmen
     * Constructor voor het opvragen van informatie uit de database voor Asset
     * @param cryptomuntId de identifier van de cryptomunt
     */
    public Cryptomunt(int cryptomuntId){
        this(cryptomuntId, null, null);
    }

    public Cryptomunt (int cryptomuntId, String name, String afkorting){
        this.Id = cryptomuntId;
        this.name = name;
        this.symbol = afkorting;
        logger.info("Cryptomunt:" + this);
    }

    @Override
    public String toString() {
        return "Cryptomunt{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int cryptomuntId) {
        this.Id = cryptomuntId;
    }

    public String getName() {
        return name;
    }

    public void setName(String naam) {
        this.name = naam;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String afkorting) {
        this.symbol = afkorting;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cryptomunt that = (Cryptomunt) o;
        //TODO Equals methode van Cryptomunt aanpassen? Voor de AssetDAOTest. Dan ook de hashmethode aanpassen!
        return Id == that.Id && name.equals(that.name) && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name, symbol);
    }
}
