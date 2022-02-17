var Application;

function init(){
    getApplication();
    getLetter();
}
function getApplication(){
    let params = new URLSearchParams(location.search);
    let appID = params.get("appID");
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/professor/application?appID="+appID);
    xhttp.send();
    xhttp.onload =function(){
        Application=JSON.parse(xhttp.responseText);
        console.log(Application);
    }
}

function saveLetter(){
    var body = document.getElementById("letterBody").value;
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/professor/letter");
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send("fName="+Application.studFName+"&lName="+Application.studLName+"&body="+body+"&appID="+Application.appId+"&receiverEmail="+Application.letterReceiverEmail);
    xhttp.onload=function (){
        window.location.href = "/professor/dashboard?letterSave=true";
    }
}

 function sendLetter(){
     let xhttp = new XMLHttpRequest();
     var body = document.getElementById("letterBody").value;
     xhttp.open("POST", "/professor/letter");
     xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
     xhttp.send("fName="+Application.studFName+"&lName="+Application.studLName+"&body="+body+"&appID="+Application.appId+"&receiverEmail="+Application.letterReceiverEmail);
     xhttp.onload=function (){
         document.body.style.cursor = "wait";
         let params = new URLSearchParams(location.search);
         let appID = params.get("appID");
         xhttp.open("PATCH", "/professor/letter");
         xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
         xhttp.send("appID="+appID);
         xhttp.onload=function (){
             if (xhttp.status===200) {
                 window.location.href = "/professor/dashboard?letterSent=true";
             }
         }
     }
}

function getLetter(){
    let params = new URLSearchParams(location.search);
    let appID = params.get("appID");
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/professor/letter?appID="+appID);
    xhttp.send();
    xhttp.onload =function(){
        document.getElementById("letterBody").value=xhttp.responseText;
    }
}