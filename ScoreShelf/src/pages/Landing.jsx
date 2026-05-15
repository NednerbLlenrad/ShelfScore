import { useState } from "react";

import Background from "../components/Background";
import NavBar from "../components/NavBar";
import LoginModal from "../components/LoginModal";
import RegisterModal from "../components/RegisterModal";

import "./Landing.css";
import LandingPage from "../assets/LandingPage.svg";

function Landing({ user, setUser, openLogin, onLogout }) {
    const [showLogin, setShowLogin] = useState(false);
    const [showRegister, setShowRegister] = useState(false);
    console.log("Landing user:", user);
    return (
        <Background>
            <main className="landing-page">
                <NavBar
                    isLoggedIn={user !== null}
                    onLoginClick={openLogin}
                    onLogout={onLogout}
                />

                <section className="landing-card">
                    <div className="landing-image-container">
                        <img
                            className="landing-image"
                            src={LandingPage}
                            alt="Landing Page Background"
                        />
                    </div>

                    <div className="landing-text">
                        <h1 className="letterboxd-text">
                            Track every game night.
                            <br />
                            Save scores worth remembering.
                            <br />
                            Build better scoreboards.
                        </h1>
                    </div>

                    {user === null && (
                        <div className="landing-actions">
                            <button
                                className="sign-up-button"
                                type="button"
                                onClick={() => setShowRegister(true)}
                            >
                                Sign Up
                            </button>
                        </div>
                    )}
                </section>

                {showLogin && (
                    <LoginModal
                        onClose={() => setShowLogin(false)}
                        setUser={setUser}
                    />
                )}

                {showRegister && (
                    <RegisterModal onClose={() => setShowRegister(false)} setUser={setUser} />
                )}
            </main>
        </Background>
    );
}

export default Landing;