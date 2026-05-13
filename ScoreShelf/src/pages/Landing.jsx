import { useState } from "react";

import Background from "../components/Background";
import NavBar from "../components/NavBar";
import LoginModal from "../components/LoginModal";
import RegisterModal from "../components/RegisterModal";

import "./Landing.css";
import LandingPage from "../assets/LandingPage.svg";

function Landing() {
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);

  return (
    <Background>
      <main className="landing-page">
        <NavBar
          isLoggedIn={false}
          onLoginClick={() => setShowLogin(true)}
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

          <div className="landing-actions">
            <button
              className="sign-up-button"
              type="button"
              onClick={() => setShowRegister(true)}
            >
              Sign Up
            </button>
          </div>
        </section>

        {showLogin && (
          <LoginModal onClose={() => setShowLogin(false)} />
        )}

        {showRegister && (
          <RegisterModal onClose={() => setShowRegister(false)} />
        )}
      </main>
    </Background>
  );
}

export default Landing;