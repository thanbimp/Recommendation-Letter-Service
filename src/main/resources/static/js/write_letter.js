function getApplication(){
    let params = new URLSearchParams(location.search);
    var appID = params.get("appID");
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/applicationByID?appID="+appID);
    xhttp.send();
    xhttp.onload =function(){
        alert(xhttp.responseText);
    }
}