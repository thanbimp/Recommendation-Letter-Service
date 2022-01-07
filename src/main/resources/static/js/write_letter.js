var Application;

function getApplication(){
    let params = new URLSearchParams(location.search);
    let appID = params.get("appID");
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/applicationByID?appID="+appID);
    xhttp.send();
    xhttp.onload =function(){
        Application=JSON.parse(xhttp.responseText);
    }
}