window.register = async function () {

    const user = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        passwordHash: document.getElementById("passwordHash").value,
        role: document.getElementById("role").value
    };

    const res = await apiCall("/users", "POST", user);

    alert("Registered Successfully!");
    console.log(res);

    window.location.href = "login.html";
};


window.login = async function () {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const res = await apiCall("/users/login", "POST", { email, password });

    localStorage.setItem("token", res.token || res);

    alert("Login Successful!");
    window.location.href = "dashboard.html";
};