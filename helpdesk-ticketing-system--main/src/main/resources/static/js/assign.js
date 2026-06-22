async function assignTicket() {

    const ticketId = document.getElementById("ticketId").value;
    const agentId = document.getElementById("agentId").value;

    const url = `http://localhost:8089/assignments/${ticketId}/${agentId}`;

    console.log("Calling:", url);

    const res = await fetch(url, {
        method: "POST"
    });

    if (!res.ok) {
        const error = await res.text();
        console.error(error);
        alert("Assignment failed");
        return;
    }

    alert("Ticket Assigned Successfully!");
}