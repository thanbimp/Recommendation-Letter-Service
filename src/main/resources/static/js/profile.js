function init() {
    getUser();
}

var User;

function getUser() {
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/currUser");
    xhttp.send();
    xhttp.onload = function () {
        User = JSON.parse(xhttp.responseText);
        fillBoxes(User);
    }
}

function fillBoxes(user) {
    document.getElementById("fName").value = user.fname;
    document.getElementById("lName").value = user.lname;
    document.getElementById("phoneNo").value = user.phoneNo;
}

function save() {
    var xhr = new XMLHttpRequest();
    xhr.open("PATCH", "/user");
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send("fname=" + document.getElementById("fName").value + "&lname=" + document.getElementById("lName").value + "&phoneNo=" + document.getElementById("phoneNo").value + "&password=" + document.getElementById("newPass").value);
    xhr.onload = function () {
        window.location.href = "/profile?succ=true";
    }
}