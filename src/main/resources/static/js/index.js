document.querySelector("#btn-submit").addEventListener("click",(e) => {
    getHospital();
});

let getHospital = async () =>{
    let sidoCdNm = document.querySelector("#sidoCdNm").value;
    let sgguCdNm = document.querySelector("#sgguCdNm").value;
    let response = await fetch(`http://localhost:8000/api/hospital?sidoCdNm=${sidoCdNm}&sgguCdNm=${sgguCdNm}`);
    let responseParsing = await response.json();
    setHospital(responseParsing);
}

let setHospital = (responseParsing) => {
    let tbodyHospitalDom = document.querySelector("#tbody-hospital");
    tbodyHospitalDom.innerHTML = "";
    responseParsing.forEach((e)=>{
        let trEL = document.createElement("tr");
        let tdEL1 = document.createElement("td");
        let tdEL2 = document.createElement("td");
        let tdEL3 = document.createElement("td");
        tdEL1.innerHTML = e.yadmNm;
        tdEL2.innerHTML = e.pcrPsblYn;
        tdEL3.innerHTML = e.addr;
        trEL.append(tdEL1);
        trEL.append(tdEL2);
        trEL.append(tdEL3);
        tbodyHospitalDom.append(trEL);
    });
}

let setSggucdnm = (responseParsing) => {
    let sgguCdNmDom = document.querySelector("#sgguCdNm");
    sgguCdNmDom.innerHTML = "";
    responseParsing.forEach((e)=>{
        let optionEL = document.createElement("option");
        optionEL.text = e;
        sgguCdNmDom.append(optionEL);
    });
}

let getSggucdnm = async (sidoCdNm)=>{
    let response = await fetch(`http://localhost:8000/api/sggucdnm?sidoCdNm=${sidoCdNm}`);
    let responseParsing = await response.json();
    setSggucdnm(responseParsing);
    //console.log(responseParsing);
}

let sidoCdNmDom = document.querySelector("#sidoCdNm");

sidoCdNmDom.addEventListener("click", (e)=>{
    //console.log(e.target.value);
    let sidoCdNm = e.target.value;
    getSggucdnm(sidoCdNm);
});