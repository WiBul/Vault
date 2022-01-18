
//WIBUL 20220113
//TODO een designpattern gebruiken voor onderstaande methodes

function vulCryptoGegevens(data){
    const mainContainer = document.getElementById("cryptoNaam");
    const portefeuilleArray = data.portefeuille;
    const div = document.createElement("div");
    for (let i = 0; i < portefeuilleArray.length; i++){
        const div = document.createElement("div");
        const waarde = portefeuilleArray[i].name
            + " " + portefeuilleArray[i].aantal.toFixed(3) + " " + portefeuilleArray[i].prijs.toFixed(2);
        console.log(waarde);
        div.textContent = waarde;
        mainContainer.appendChild(div)
    }
}
//
// function vulCryptoAantal(data){
//     const mainContainer2 = document.getElementById("cryptoAantal");
//     const portefeuilleArray = data.portefeuille;
//     for (let i = 0; i < portefeuilleArray.length; i++){
//         const div = document.createElement("div");
//         const aantal = portefeuilleArray[i].aantal.toFixed(3);
//         console.log(aantal);
//         div.textContent = aantal;
//         mainContainer2.appendChild(div)
//     }
// }
//
// function vulCryptoKoers(data){
//     const mainContainer3 = document.getElementById("cryptoKoers");
//     const portefeuilleArray = data.portefeuille;
//     for (let i = 0; i < portefeuilleArray.length; i++){
//         const div = document.createElement("div");
//         const prijs = portefeuilleArray[i].prijs.toFixed(2);
//         console.log(prijs);
//         div.textContent = prijs;
//         mainContainer3.appendChild(div)
//     }
// }

function transactieButton(){
    const button = document.getElementById("cryptoTransactie");

}

//JU-SEN  20220113

function vulRekeningGegevens(data){
    document.getElementById("iban").textContent = "IBAN " + data.iban;
    document.getElementById("saldo").textContent = "Saldo € " + data.saldo;
}

