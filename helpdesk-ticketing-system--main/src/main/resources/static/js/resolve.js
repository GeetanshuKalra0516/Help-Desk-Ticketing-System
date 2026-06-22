async function resolveTicket() {

    const ticketId = document.getElementById("ticketId").value;
    const userId = document.getElementById("agentId").value;
    const notes = document.getElementById("notes").value;

    const url = `http://localhost:8089/resolutions/${ticketId}/${userId}`;

    console.log("Calling:", url);

    const res = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            notes: notes
        })
    });

    if (!res.ok) {
        const error = await res.text();
        console.error("Error:", error);
        alert("Resolution failed");
        return;
    }

    const data = await res.json();

    alert("Ticket Resolved Successfully!");
    console.log(data);
}