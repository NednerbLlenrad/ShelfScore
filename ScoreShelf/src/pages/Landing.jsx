import Background from "../components/Background";
import NavBar from "../components/NavBar";

import "./Landing.css";
import LandingPage from "../assets/LandingPage.svg";

function Landing() {
    return (
        <Background>
            <main className="landing-page">
                <NavBar isLoggedIn={false} />

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
                        <button className="sign-up-button" type="button">
                            Sign Up
                        </button>
                    </div>
                </section>
            </main>
        </Background>
    );
}

export default Landing;