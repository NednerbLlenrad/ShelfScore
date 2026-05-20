import { useState } from "react";

import Background from "../components/Background";
import NavBar from "../components/NavBar";
import { apiFetch } from "../services/api";

import "./Account.css";

function Account({ user, setUser, onLogout, openLogin }) {
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    const [accountForm, setAccountForm] = useState({
        username: user?.username || "",
        email: user?.email || "",
        currentPassword: "",
        newPassword: ""
    });

    function handleAccountChange(event) {
        setAccountForm({
            ...accountForm,
            [event.target.name]: event.target.value
        });
    }

    function handleAccountSubmit(event) {
        event.preventDefault();

        setError("");
        setMessage("");

        apiFetch(`/app-user/${user.appUserId}`, {
            method: "PUT",
            body: JSON.stringify({
                appUserId: user.appUserId,
                username: accountForm.username,
                email: accountForm.email,
                currentPassword: accountForm.currentPassword,
                newPassword: accountForm.newPassword
            })
        })
            .then(() => {
                const updatedUser = {
                    ...user,
                    username: accountForm.username,
                    email: accountForm.email
                };

                localStorage.setItem("user", JSON.stringify(updatedUser));
                setUser(updatedUser);

                setMessage("Account updated successfully.");

                setAccountForm({
                    username: accountForm.username,
                    email: accountForm.email,
                    currentPassword: "",
                    newPassword: ""
                });
            })
            .catch((error) => {
                setError(error.message || "Could not update account.");
            });
    }

    return (
        <Background>
            <main className="account-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="account-content">
                    <h1>Account</h1>

                    {error && <p className="form-error">{error}</p>}
                    {message && <p className="form-success">{message}</p>}

                    <div className="account-layout">
                        <form
                            className="account-card"
                            onSubmit={handleAccountSubmit}
                        >
                            <h2>Account Settings</h2>

                            <label>
                                Username
                                <input
                                    name="username"
                                    value={accountForm.username}
                                    onChange={handleAccountChange}
                                />
                            </label>

                            <label>
                                Email
                                <input
                                    name="email"
                                    value={accountForm.email}
                                    onChange={handleAccountChange}
                                />
                            </label>

                            <label>
                                Current Password
                                <input
                                    type="password"
                                    name="currentPassword"
                                    placeholder="Required to change password"
                                    value={accountForm.currentPassword}
                                    onChange={handleAccountChange}
                                />
                            </label>

                            <label>
                                New Password
                                <input
                                    type="password"
                                    name="newPassword"
                                    placeholder="Leave blank to keep current password"
                                    value={accountForm.newPassword}
                                    onChange={handleAccountChange}
                                />
                            </label>

                            <button type="submit">Save Account</button>
                        </form>
                    </div>
                </section>
            </main>
        </Background>
    );
}

export default Account;