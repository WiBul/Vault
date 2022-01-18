// Created by S.C. van Gils
// Creation date 16-12-2021

package com.example.thevault.support.data;


import com.example.thevault.domain.mapping.repository.RootRepository;
import com.example.thevault.domain.model.Adres;
import com.example.thevault.domain.model.Klant;
import com.example.thevault.domain.model.Transactie;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataGenerator {

    private final Logger logger = LoggerFactory.getLogger(DataGenerator.class);




    public static void main(String[] args) throws IOException {
    List<Klant> list = maakLijstKlantenVanCSV("Sprint2/datacsv.csv", 20);
        for(Klant klant: list){
            System.out.println(klant);
        }

    }
    public static int[] LANGE_MAANDEN = {1,3,5,7,8,10,12};
    public static int FEBRUARI = 2;
    public static int AANTAL_DAGEN_IN_LANGE_MAAND = 31;
    public static int AANTAL_DAGEN_IN_KORTE_MAAND = 30;
    public static int AANTAL_DAGEN_IN_FEBRUARI_NORMAAL = 28;
    public static int AANTAL_DAGEN_IN_FEBRUARI_SCHRIKKELJAAR = 29;
    public static int SCHRIKKELJAAR_PER_ZOVEEL_JAAR = 4;
    public static int VROEGSTE_GEBOORTEJAAR = 1946;
    public static int LAATSTE_GEBOORTEJAAR = 2001;
    public static int EERSTE_MAAND_VAN_JAAR = 1;
    public static int LAATSTE_MAAND_VAN_JAAR = 12;
    public static int AANTAL_TOEVOEGINGEN = 5;



    public static List<Klant> maakLijstKlantenVanCSV(String filename, int hoeveelKlanten) throws IOException {
        Resource resource = new ClassPathResource(filename);

        File file = resource.getFile();
        List<Klant> klantList = new ArrayList<>();
        Scanner klantenLezer;
        int counter = 0;
        try {
            klantenLezer = new Scanner(file);
            createKlantList(hoeveelKlanten, klantList, klantenLezer, counter);
        }
        catch (FileNotFoundException exception){
            System.out.println("geen bestand");
        }
        return klantList;
    }

    private static void createKlantList(int hoeveelKlanten, List<Klant> klantList, Scanner klantenLezer, int counter) {
        while (klantenLezer.hasNextLine() & counter < hoeveelKlanten) {
            String[] regelArray = klantenLezer.nextLine().split(",");
            String adresStraatEnHuisnummer = regelArray[4];
            String[] adresArray = adresStraatEnHuisnummer.split(" ");
            addKlantToList(klantList, regelArray[0], regelArray[1], regelArray[2], Long.parseLong(regelArray[3])
                    , getStraatnaam(adresArray), Integer.parseInt(adresArray[adresArray.length - 1]),
                    regelArray[5].replace(" ", ""), regelArray[6],
                    genereerRandomToevoeging(AANTAL_TOEVOEGINGEN));
            counter++;
        }
    }

    private static void addKlantToList(List<Klant> klantList, String gebruikersnaam, String wachtwoord,
                                       String naam, long bsn, String straatnaam, int huisnummer,
                                       String postcode, String plaatsnaam, String toevoeging) {
        Adres adresKlant = new Adres(straatnaam, huisnummer, toevoeging, postcode, plaatsnaam);
        Klant klant = new Klant(gebruikersnaam, wachtwoord,
                null, null, null, naam, adresKlant, bsn, genereerRandomGeboortedatum());
        klantList.add(klant);
    }

    private static String getStraatnaam(String[] adresArray) {
        String straatnaam = adresArray[0];
        for (int i = 1; i < adresArray.length - 1; i++) {
            straatnaam += " " + adresArray[i];
        }
        return straatnaam;
    }

    private static LocalDate genereerRandomGeboortedatum() {
        int geboortejaar = genereerRandomGetal(VROEGSTE_GEBOORTEJAAR, LAATSTE_GEBOORTEJAAR, 1);
        int geboortemaand = genereerRandomGetal(EERSTE_MAAND_VAN_JAAR, LAATSTE_MAAND_VAN_JAAR, 1);
        int laatsteDagVanDeMaand = AANTAL_DAGEN_IN_KORTE_MAAND;
        laatsteDagVanDeMaand = getLaatsteDagVanDeMaand(geboortejaar, geboortemaand, laatsteDagVanDeMaand);
        int geboortedag = genereerRandomGetal(1, laatsteDagVanDeMaand, 1);
        return LocalDate.of(geboortejaar, geboortemaand, geboortedag);
    }

    private static int getLaatsteDagVanDeMaand(int geboortejaar, int geboortemaand, int laatsteDagVanDeMaand) {
        if(ArrayUtils.contains(LANGE_MAANDEN, geboortemaand)){
            laatsteDagVanDeMaand = AANTAL_DAGEN_IN_LANGE_MAAND;
        }
        else if(geboortemaand == FEBRUARI){
            laatsteDagVanDeMaand = (isSchrikkeljaar(geboortejaar)) ?
                    AANTAL_DAGEN_IN_FEBRUARI_SCHRIKKELJAAR : AANTAL_DAGEN_IN_FEBRUARI_NORMAAL;
        }
        return laatsteDagVanDeMaand;
    }

    public static boolean isSchrikkeljaar(int jaar){
        return jaar%SCHRIKKELJAAR_PER_ZOVEEL_JAAR == 0;
    }

    public static int genereerRandomGetal(int mpMinimum, int mpMaximum, int mpTussenstap){
        return mpTussenstap *
                ((int)(Math.random() * ((mpMaximum - mpMinimum + 1))/mpTussenstap)) + mpMinimum;
    }

    public static String genereerRandomToevoeging(int aantalVerschillende){
        String letter = "";
       int getal = genereerRandomGetal(0, aantalVerschillende, 1);
       if(getal != 0) {
        letter = Character.toString((char)(getal + 64)).toString();
       }
       return letter;
    }


}
