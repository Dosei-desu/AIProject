const TREATMENT_URL = 'http://localhost:8080/api/v1/treatment';
const DIAGNOSIS_URL = 'http://localhost:8080/api/v1/diagnose';

//document.getElementById('form-diagnose').addEventListener('click', getDiagnosis, true);
document.getElementById('form-treatment').addEventListener('submit', getTreatmentSuggestion);

/*
async function getDiagnosis(event) {
    event.preventDefault();

    const URL = `${DIAGNOSIS_URL}?symptom=` + `${document.getElementById('symptom').value}`
    const diagnoses = document.getElementById('diagnoses');

    try {
        const response = await fetch(URL)
            .then(handleHttpErrors)
        console.log(response.toString())
        for (let i = 0; i < response.diagnoses.length; i++) {
            let list = response.diagnoses.get(i)
            list = document.createElement('li')
            document.getElementById('diagnoses').appendChild(list)
            console.log(list)
        }

    } catch (e) {
        diagnoses.style.color = "red";
        diagnoses.innerText = e.message;
    }
}
 */

async function getTreatmentSuggestion(event) {
    event.preventDefault();

    const URL = `${TREATMENT_URL}?prompt=` + `${document.getElementById('prompt').value}`
    const result = document.getElementById('result');

    try {
        const response = await fetch(URL)
            .then(handleHttpErrors)
        console.log(response.toString())
        document.getElementById('result').innerText = response;
    } catch (e) {
        result.style.color = "red";
        result.innerText = e.message;
    }
}

async function handleHttpErrors(res) {
    if (!res.ok) {
        const errorResponse = await res.json();
        const msg = errorResponse.message ? errorResponse.message : "No error details provided"
        throw new Error(msg)
    }
    return res.json()
}