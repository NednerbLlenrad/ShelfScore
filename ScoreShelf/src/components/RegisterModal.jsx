import "./RegisterModal.css";
import { useState } from "react";

function RegisterModal({ onClose }) {
  const [user, setUser] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: ""
  });

  function handleChange(event) {
    setUser({
      ...user,
      [event.target.name]: event.target.value
    });
  }

  function handleSubmit(event) {
    event.preventDefault();

    if (user.password !== user.confirmPassword) {
      console.log("Passwords do not match.");
      return;
    }

    const registerRequest = {
      username: user.username,
      email: user.email,
      password: user.password
    };
    fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(registerRequest)
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Registration failed.");
        }

        return response.json();
      })
      .then((data) => {
        console.log("Registered user:", data);
        onClose();
      })
      .catch((error) => {
        console.error(error);
      });
  }

  return (
    <div className="modal-backdrop">
      <section className="register-card">
        <button
          className="modal-close"
          type="button"
          onClick={onClose}
        >
          ×
        </button>

        <h1>Sign Up</h1>
        <p>Let's start scoring!</p>

        <form className="register-form" onSubmit={handleSubmit}>
          <label>
            Username
            <input type="text" name="username" value={user.username} onChange={handleChange} />
          </label>

          <label>
            Email
            <input type="email" name="email" value={user.email} onChange={handleChange} />
          </label>

          <label>
            Password
            <input type="password" name="password" value={user.password} onChange={handleChange} />
          </label>

          <label>
            Confirm Password
            <input type="password" name="confirmPassword" value={user.confirmPassword} onChange={handleChange} />
          </label>

          <button type="submit">
            Sign Up
          </button>
        </form>
      </section>
    </div>
  );
}

export default RegisterModal;