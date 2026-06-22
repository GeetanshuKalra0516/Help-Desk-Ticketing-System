const BASE_URL = "http://localhost:8089";

async function apiCall(endpoint, method, body) {

    const res = await fetch(BASE_URL + endpoint, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: body ? JSON.stringify(body) : null
    });

    if (!res.ok) {
        console.log("API ERROR:", res.status);
    }

    return res.json();
}