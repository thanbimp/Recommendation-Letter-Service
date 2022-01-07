var Application;

function getApplication(){
    let params = new URLSearchParams(location.search);
    let appID = params.get("appID");
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/applicationByID?appID="+appID);
    xhttp.send();
    xhttp.onload =function(){
        Application=JSON.parse(xhttp.responseText);
        console.log(Application);
    }
}

function sendLetter(){
    var body = document.getElementById("letterBody").value;
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/letter");
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send("fName="+Application.studFName+"&lName="+Application.studLName+"&body="+body+"&appID="+Application.appId);
}