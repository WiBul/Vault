function validateJWT(jwtString, gebruikersnaam){
    console.log(gebruikersnaam);
    const endpoint = "/validate/jwt";
    fetch(endpoint, {
        method: 'POST',
    headers: {
        'Authorization': 'Bearer' + jwtString,
    },
    body: gebruikersnaam,
    }).then((response) => {
        if(response.status === 200){
            showDashboard()
        }
        if(response.status === 401){
            validateRefresh()
        }
        else console.log("*** andere status: " + response.statusText)
    }).catch((error) => {
        console.error('*** Iets misgegaan:', error);
    })
}

function getJWT(response){
    console.log("*** moet de jwt uit de header lezen ***")
    return response.headers.get("Authorization").substr(7); // verwijder "Bearer "

}
function validateRefresh(gebruikersnaam){
    console.log("*** jwt niet geldig ***")
    const endpoint = "/validate/refresh"
    fetch(endpoint, {
        method: 'POST',
        body: gebruikersnaam
    }).then((response) => {
        if(response.status === 200){
            const jwt = response.headers.get("Authorization").substr(7); // verwijder "Bearer "
            console.log(jwt);
            return jwt;
        } //TODO status bepalen
        if(response.status === 400){
            console.log(response.statusText);
            console.log("*** refresh token niet meer geldig ***");
            showLogin();
        }
        else console.log("*** andere status: " + response.statusText)
    }).catch((error) => {
        console.error('*** Iets misgegaan:', error);
    })
}
function showDashboard(){
    console.log("*** jwt gevalideerd ***")
}
function showLogin(){
    console.log("*** terug naar het login-scherm ***")
}
/*const canvasVar = document.createElement("canvas");
const graphObject = document.getElementById("testGrafiek");
graphObject.appendChild(canvasVar);
canvasVar.id = "grafiekTest";
canvasVar.style.width = "100%";
canvasVar.style.maxWidth = "600px";

const xValues = [50,60,70,80,90,100,110,120,130,140,150];
const yValues = [7,8,8,9,9,9,10,11,14,14,15];

new Chart("myChart", {
    type: "line",
    data: {
        labels: xValues,
        datasets: [{
            fill: false,
            lineTension: 0,
            backgroundColor: "rgba(0,0,255,1.0)",
            borderColor: "rgba(0,0,255,0.1)",
            data: yValues
        }]
    },
    options: {
        legend: {display: false},
        scales: {
            yAxes: [{ticks: {min: 6, max:16}}],
        }
    }
});*/
