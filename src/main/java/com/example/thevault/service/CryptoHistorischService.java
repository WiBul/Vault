// Created by S.C. van Gils
// Creation date 26-12-2021

package com.example.thevault.service;

import com.example.thevault.domain.mapping.repository.RootRepository;
import com.example.thevault.domain.model.CryptoWaarde;
import com.example.thevault.domain.model.Cryptomunt;
import com.example.thevault.domain.transfer.CryptoWaardenHistorischDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoHistorischService implements ApplicationListener<ContextRefreshedEvent> {


    private final Logger logger = LoggerFactory.getLogger(CryptoHistorischService.class);

    private final RootRepository rootRepository;

    public CryptoHistorischService(RootRepository rootRepository) {
        super();
        this.rootRepository = rootRepository;
        logger.info("New CryptoHistorischService");
    }


    public CryptoWaardenHistorischDto maakCryptoWaardeArray(Cryptomunt cryptomunt){
        // bepalen wat voor soort array moet geschreven worden
        List<CryptoWaarde> cryptoWaardeList = rootRepository.haalAlleCryptoWaardesVanCryptomunt(cryptomunt);
        String[] datum = new String[cryptoWaardeList.size()];
        double[] waarde = new double[cryptoWaardeList.size()];
        for (int i = 0; i < cryptoWaardeList.size(); i++) {
            datum[i] = cryptoWaardeList.get(i).getDatum().toString();
            waarde[i] = cryptoWaardeList.get(i).getWaarde();
        }
        return new CryptoWaardenHistorischDto(datum, waarde);
    }

    public Cryptomunt[] maakCryptoMuntArray(){
        return rootRepository.geefAlleCryptomunten().toArray(Cryptomunt[]::new);
    }

    public Cryptomunt getCryptoMuntOpNaam(String naam){
       return CryptoWaardeService.cryptoLijst().stream()
               .filter(cryptomunt -> cryptomunt.getName().equals(naam)).findFirst().orElse(null);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    /*    List<CryptoWaarde> cryptoWaardeList = rootRepository.haalAlleCryptoWaardesVanCryptomunt(getCryptoMuntOpNaam("bitcoin"));
        cryptoWaardeList.forEach(System.out::println);*/
    }
}
