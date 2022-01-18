// Created by S.C. van Gils
// Creation date 26-12-2021

package com.example.thevault.controller.rest_api_controller;

import com.example.thevault.domain.model.Cryptomunt;
import com.example.thevault.domain.transfer.CryptoWaardenHistorischDto;
import com.example.thevault.service.CryptoHistorischService;
import com.example.thevault.service.LoginService;
import com.example.thevault.service.RegistrationService;
import com.example.thevault.service.TransactieService;
import com.example.thevault.support.authorization.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class CryptoHistorischController extends BasisApiController{

    private final Logger logger = LoggerFactory.getLogger(CryptoHistorischController.class);

    private final CryptoHistorischService cryptoHistorischService;

    public CryptoHistorischController(RegistrationService registrationService,
                                      AuthorizationService authorizationService, LoginService loginService, TransactieService transactieService,
                                      CryptoHistorischService cryptoHistorischService) {
        super(registrationService, authorizationService, loginService, transactieService);
        this.cryptoHistorischService = cryptoHistorischService;
        logger.info("New CryptoHistorischController");
    }
    @PostMapping("/cryptoGrafiek")
    public ResponseEntity<CryptoWaardenHistorischDto> cryptoWaardeArrayHandler(@RequestBody String cryptoNaam){

        CryptoWaardenHistorischDto cryptoArrays = cryptoHistorischService.maakCryptoWaardeArray(cryptoHistorischService.getCryptoMuntOpNaam(cryptoNaam));
        return ResponseEntity.ok().body(cryptoArrays);
    }
    @GetMapping("/cryptoLijst")
    public ResponseEntity<Cryptomunt[]> getCryptomunten(){
        return ResponseEntity.ok().body(cryptoHistorischService.maakCryptoMuntArray());
    }
}
