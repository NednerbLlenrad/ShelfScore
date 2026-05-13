import "./LoginModal.css";
import { useState } from "react";
function LoginModal({ onClose, setUser }) {

    const [credentials, setCredentials] = useState({
        username: "",
        password: "",
    });

    function handleChange(event) {
        setCredentials({
            ...credentials,
            [event.target.name]: event.target.value
        });
    }

    function handleSubmit(event) {
        event.preventDefault();

        fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(credentials)
        })
            .then((response) => {
                if (!response.ok) {
                    return response.json().then((errors) => {
                        console.log(errors);
                        throw new Error("Login failed.");
                    });
                }

                return response.json();
            })
            .then((data) => {
                console.log("Logged in user:", data);

                setUser(data);

                onClose();
            })
            .catch((error) => {
                console.error(error);
            });
    }

    return (
        <div className="modal-backdrop">
            <section className="login-card modal-card">
                <button className="modal-close" type="button" onClick={onClose}>
                    ×
                </button>

                <h1>Login</h1>
                <p>Let's get back to scoring!</p>

                <form className="login-form" onSubmit={handleSubmit}>
                    <label>
                        Username
                        <input type="text" name="username" value={credentials.username} onChange={handleChange} />
                    </label>

                    <label>
                        Password
                        <input type="password" name="password" value={credentials.password} onChange={handleChange} />
                    </label>

                    <button type="submit">Log In</button>
                </form>
            </section>
        </div>
    );
}

export default LoginModal;