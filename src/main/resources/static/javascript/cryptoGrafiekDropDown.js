// @author Steven van Gils

const dropDownMenu = document.getElementById("cryptoDropdown");

let cryptoArray;

async function getCryptomunten() {
 await fetch("/cryptoLijst")
        .then((response) => response.json())
        .then((json) => {
            cryptoArray = json;
        })
        .catch((error) => {
            console.error('*** cryptoLijst mislukt:', error);
        })
}


async function useCryptomunten() {
 await getCryptomunten();
    for (let i = 0; i < cryptoArray.length; i++){
     const cryptoGrafiekButton =
         createElementWithClassAndId(dropDownMenu, "div", "keuze", cryptoArray[i].name + "GrafiekButton");
     cryptoGrafiekButton.textContent = cryptoArray[i].name;
     cryptoGrafiekButton.addEventListener("click", () => {
         showCryptoChart(cryptoArray[i].name);
     })
    }
}
 useCryptomunten();


/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function myFunction() {
    document.getElementById("cryptoDropdown").classList.toggle("show");
}
document.getElementById("cryptoZoekveld")

function filterFunction() {
 const input = document.getElementById("cryptoZoekveld");
 const filter = input.value.toUpperCase();
 const div = document.getElementById("cryptoDropdown");
 const dropDownDivs = div.getElementsByTagName("div");
    for (let i = 0; i < dropDownDivs.length; i++) {
   const txtValue = dropDownDivs[i].textContent || dropDownDivs[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            dropDownDivs[i].style.display = "";
        } else {
            dropDownDivs[i].style.display = "none";
        }
    }
}

