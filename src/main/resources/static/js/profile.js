
function innit(){
    getUser();
}

var User;
function getUser(){
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/user");
    xhttp.send();
    xhttp.onload =function(){
        User=JSON.parse(xhttp.responseText);
        console.log(User);
    }
}