const TREATMENT_URL = 'http://localhost:8080/api/v1/treatment';

document.getElementById('form-treatment').addEventListener('submit', getTreatmentSuggestion);


async function getTreatmentSuggestion(event) {
    event.preventDefault();

    const URL = `${TREATMENT_URL}?prompt=` + `${document.getElementById('prompt').value}`
    const result = document.getElementById('result');

    try {
        const response = await fetch(URL)
            .then(handleHttpErrors)
        console.log(response.answer)
        document.getElementById('result').innerText = response.answer;
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