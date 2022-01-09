function init(){
    getApplications(document.getElementById("userEmail").textContent);
}


function getApplications(email){
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/myApplicationsProf?email="+email);
    xhttp.send();
    xhttp.onload =function(){
        makeApplicationsArray(xhttp.responseText);
    }
}

var currentSelectedApplicationID;

function makeApplicationsArray(responseText) {
    let as = JSON.parse(responseText);
    makeList(as);
}

function makeList(data){
    var listDiv = document.getElementById('listDiv');
    var ul = document.createElement('ul');
    listDiv.appendChild(ul);
    for (var i = 0; i < data.length; ++i) {
        var li = document.createElement('li');
        li.setAttribute("id",data[i].appId);
        li.setAttribute("onclick","onApplicationClicked(this.id)");
        var textnode = document.createTextNode(data[i].timeStamp);
        li.appendChild(textnode);
        ul.appendChild(li);
    }
}

function onApplicationClicked(id){
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", "/application?appID="+id);
    xhttp.send();
    xhttp.onload =function(){
        currentSelectedApplicationID = id;
        renderApplication(JSON.parse(xhttp.responseText));

    }
}

function renderApplication(application){
    var detailsDiv=document.getElementById("detailsDiv");
    detailsDiv.innerHTML="";
    var namePar=document.createElement("p");
    namePar.innerText="Name:\n"+application.studFName+" "+application.studLName+"("+application.fromMail+")"+"\n";
    var profToPar=document.createElement("p");
    profToPar.innerText="To professor:\n"+application.profEmail;
    var acceptedPar=document.createElement("p");
    if(application.accepted===null){
        acceptedPar.innerText="Accepted:No";
        var acceptBtn=document.createElement("button");
        acceptBtn.setAttribute("class","button");
        acceptBtn.setAttribute("onclick","acceptApplication(true)");
        acceptBtn.innerText="Accept Application";
    }
    else {
        acceptedPar.innerText="Accepted:Yes";
        var acceptBtn=document.createElement("button");
        acceptBtn.setAttribute("class","button");
        acceptBtn.setAttribute("onclick","redirectToLetterPage()");
        acceptBtn.innerText="Write Letter";
    }
    var bodyPar=document.createElement("p");
    bodyPar.innerText="Application Body:\n"+application.body+"\n";
    var denyBtn=document.createElement("button");
    denyBtn.setAttribute("class","button");
    denyBtn.setAttribute("onclick","acceptApplication(false)");
    denyBtn.setAttribute("style","background-color:#ff4646");
    denyBtn.innerText="Decline Application";
    detailsDiv.appendChild(namePar);
    detailsDiv.appendChild(profToPar);
    detailsDiv.appendChild(acceptedPar);
    detailsDiv.appendChild(bodyPar);
    detailsDiv.appendChild(acceptBtn);
    detailsDiv.appendChild(denyBtn);
}

function acceptApplication(result){
    var xhr = new XMLHttpRequest();
    xhr.open("PATCH","/application");
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send("accepted="+result+"&appID="+currentSelectedApplicationID);
    document.getElementById("listDiv").innerHTML="";
    document.getElementById("detailsDiv").innerHTML=""
    setTimeout(function (){init()},50);
}

function redirectToLetterPage(){
    window.location.href="/write_letter?appID="+currentSelectedApplicationID;

}