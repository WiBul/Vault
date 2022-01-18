/* @author Wim
check of postcode voldoet aan opmaak CCCCLL
//TODO ophalen adres adhv postcode huisnummer
*/

package com.example.thevault.service;

import org.springframework.stereotype.Service;

@Service
public class AdresService {

    public AdresService() {
        super();
    }

    public static final String REGEX_POSTCODE = "[1-9][0-9]{3}[a-zA-Z]{2}";

    public boolean postcodeOpmaak(String postcode){
        return postcode.matches(REGEX_POSTCODE);
    }
}
