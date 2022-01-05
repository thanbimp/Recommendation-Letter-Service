function submit(){
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/user?email="+document.getElementById("userEmail").textContent);
    xhttp.send();
    xhttp.onload =function(){
        parseDataAndSubmit(JSON.parse(xhttp.responseText),document.getElementById("profEmail").value,document.getElementById("appBody").value);
    }
}

function parseDataAndSubmit(user,profEmail,body){
    var fname=user.fname;
    var lname=user.lname;
    var fromMail=user.email;
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/application");
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send("profEmail="+profEmail+"&appBody="+body+"&fname="+fname+"&lname="+lname+"&fromMail="+fromMail);
}