// @author Steven van Gils

let grafiekJson;

async function getCryptoKoersInfo(cryptomunt) {
  await fetch("/cryptoGrafiek", {
        method: 'POST',
        headers: {
            'Content-Type': 'text/plain',
        },
        body: cryptomunt,
    })
        .then((response) => {
            return response.json();
        })
      .then((json) => {
          grafiekJson = json;
          return json
      })

        .catch((error) => {
            console.error('*** Iets misgegaan:', error);
        });
}
function getXArray(cryptoHistorieJson){
    return cryptoHistorieJson.datum;
}
function getYArray(cryptoHistorieJson){
    return cryptoHistorieJson.waarde;
}
let xArray;
let yArray;
let data;
let xArrayMin;
let xArrayMax;
let yArrayMin;
let yArrayMax;
let layout;
let cryptoName;
async function showCryptoChart(cryptomunt) {

    await getCryptoKoersInfo(cryptomunt);
    cryptoName = cryptomunt;
    xArray = getXArray(grafiekJson);
    yArray = getYArray(grafiekJson);
    xArrayMin = xArray.slice().sort()[0];
    xArrayMax = xArray.slice().sort().reverse()[0];
    yArrayMin = yArray.slice().sort(function (a, b) {
        return a - b})[0];
    yArrayMax = yArray.slice().sort(function (a, b) {
        return b - a})[0];
    data = [{
        x: xArray,
        y: yArray,
        mode: "lines"
    }];
    // Define Layout
    layout = setLayout(xArray);
    Plotly.newPlot(document.getElementById("myPlot"), data, layout);
}

 showCryptoChart("bitcoin");

function setLayout(rangeArray){
    return {
        xaxis: {
            linecolor: '#636363',
            range: rangeArray,
            type: 'date',
            title: "datum"},
        yaxis: {
            linecolor: '#636363',
            range: [yArrayMin * 0.95, yArrayMax * 1.05],
            title: "Koers"},
        title: cryptoName
    };
}
const laatsteWeekKnop = document.getElementById("laatsteWeek");
const laatsteMaandKnop = document.getElementById("laatsteMaand");
const laatsteDrieMaandenKnop = document.getElementById("laatsteDrieMaanden");

function maakRangeKnop(element){
    element.addEventListener("click", () => {
    const rangeArray = range(element, xArray);
    const layOutNew = setLayout(rangeArray, document.getElementById("cryptoDropdown").value);
    Plotly.newPlot(document.getElementById("myPlot"), data, layOutNew);
    })
}
maakRangeKnop(laatsteWeekKnop);
maakRangeKnop(laatsteMaandKnop);
maakRangeKnop(laatsteDrieMaandenKnop);

function range(element, dataArray){
    switch (element.value){
        case "Laatste week": return [dataArray[dataArray.length - 8], dataArray[dataArray.length - 1]];

        case "Laatste 30 dagen": return [dataArray[dataArray.length - 31], dataArray[dataArray.length - 1]];

        case "Laatste 90 dagen": return [dataArray[dataArray.length - 91], dataArray[dataArray.length - 1]];
    }
}


