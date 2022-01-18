"use strict";

function loginScreen(){
    document.getElementById("registratie-personalia").style.display = "none";
    document.getElementById("inlog").style.display = "block";
    document.getElementById("dashboard").style.display = "none";
}

function registreerScreen(){
    document.getElementById("registratie-personalia").style.display = "block";
    document.getElementById("inlog").style.display = "none";
    document.getElementById("dashboard").style.display = "none";
}

function dashboardScreen(){
    document.getElementById("registratie-personalia").style.display = "none";
    document.getElementById("inlog").style.display = "none";
    document.getElementById("dashboard").style.display = "block";
}


/*TODO Functie Login schrijven*/
/* 3- Vanuit het endpoint moet een check worden uitgevoerd op naam en wachtwoord */
/* 3a-Hier staat informatie over de authenticate header: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/WWW-Authenticate*/
/* 3a-Moet ik hiervoor het schema HMAC256 gebruiken (is de manier waarop we Tokens maken)? Of 'base64' (zie loginService --> valideerLogin)? Zie ook 'Authorization' header van vindstraatenplaatsnaam. */
/* 4a-Als 'valid' dan moet gebruiker worden doorgestuurd naar dashboard */
/* 4b-En er moet een token worden meegegeven die wordt opgeslagen, via sessionStorage.setItem("sessietoken", "token")? */

function login(){
    const formDataInlog = new FormData(document.getElementById("inlogform"));
    const inlogGegevens = Object.fromEntries(formDataInlog);
    console.log("Hier zijn de inloggegevens " + JSON.stringify(inlogGegevens));

    /*Hoe werkt deze fetch? Hoe weet ik of de API werkt? */
    /*Check KlantController PostMapping Login om te zien wat ik ermee kan*/
    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        /*Ik kan de informatie via json versturen, maar is dat veilig genoeg? DataForm zou de info al moeten encoden...*/
        /* En gaat het zo werken, of werkt serverside alleen/beter via x-www-form-urlencoded? Moet ik iets doen aan de serverside om de JSON op te vangen? */
        body: JSON.stringify(inlogGegevens),
    })
        /*In de WelkomDTO zitten: saldo (double), portefeuille (List<Asset>), IBAN (String)*/
        /*Worden die er nu op de juiste manier uitgehaald? We willen die gebruiken in het Dashboard*/
        .then((response) => {
            if(response.status === 200){
                dashboardScreen();
            }
            console.log('Success:', response);
            return response.json();
        })
        .then((json) => {
            vulCryptoGegevens(json);
            vulCryptoAantal(json);
            vulCryptoKoers(json);
            vulRekeningGegevens(json);
        })
        .catch((error) => {
            console.error('*** Iets misgegaan:', error);
        });
}


/*TODO (Singleton?) Mediator schrijven voor gebruiken login vs registratie? Zie voorbeeld*/
/* https://refactoring.guru/design-patterns/mediator */
/*TODO (Singleton?) Façade pattern voor de bank: de klant ziet alleen de 'voorkant', maar achter de schermen gebeurt veel. Voorbeeld:*/
/* https://www.youtube.com/watch?v=B1Y8fcYrz5o&t=50s */
/* Om met nummers te werken: https://www.w3schools.com/js/js_number_methods.asp*/
/* RegExp: https://www.w3schools.com/js/js_regexp.asp en https://www.w3schools.com/jsref/jsref_obj_regexp.asp */
/* Closures in JavaScript: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Closures */
/* Simpele uitleg closures: https://dmitripavlutin.com/simple-explanation-of-javascript-closures/*/
/* Asymetrisch met promise en await: https://www.w3schools.com/js/js_async.asp*/
/* Voorbeeld hoe we de juiste schermpjes zichtbaar/onzichtbaar maken met buttons: https://www.w3schools.com/js/tryit.asp?filename=tryjs_visibility*/