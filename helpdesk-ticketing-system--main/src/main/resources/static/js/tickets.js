async function createTicket() {

    const ticket = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        priority: document.getElementById("priority").value,
        userId: Number(document.getElementById("userId").value)
    };

    try {
        const res = await apiCall("/tickets", "POST", ticket);

        alert("Ticket Created Successfully!");
        console.log(res);

        loadTickets();

    } catch (error) {
        console.error("Create Ticket Error:", error);
        alert("Failed to create ticket");
    }
}


async function loadTickets() {

    try {
        const tickets = await apiCall("/tickets", "GET");

        let rows = "";

        tickets.forEach(t => {
            rows += `
                <tr>
                    <td>${t.ticketId}</td>
                    <td>${t.title}</td>
                    <td>${t.description}</td>
                    <td>${t.priority}</td>
                    <td>${t.status}</td>
                </tr>
            `;
        });

        document.getElementById("ticketTable").innerHTML = rows;

    } catch (error) {
        console.error("Load Tickets Error:", error);
    }
}