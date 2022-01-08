function submit(){
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/currUser");
    xhttp.send();
    xhttp.onload =function(){
        parseDataAndSubmit(JSON.parse(xhttp.responseText),document.getElementById("profEmail").value,document.getElementById("appBody").value,document.getElementById("toEmail").value);
    }
}

function parseDataAndSubmit(user,profEmail,body,toEmail){
    var fname=user.fname;
    var lname=user.lname;
    var fromMail=user.email;
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/application");
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send("profEmail="+profEmail+"&appBody="+body+"&fname="+fname+"&lname="+lname+"&fromMail="+fromMail+"&toMail="+toEmail);
    xhttp.onload=function (){
        if (xhttp.responseText==="true"){
            window.location.href = "/dashboard?succ=true";
        }
        else{
            window.location.href = "/new_application?succ=false";
        }
    }
}