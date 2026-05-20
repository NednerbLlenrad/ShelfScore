import { Link, NavLink, useNavigate } from "react-router-dom";
import Logo from "../assets/Logo.svg";
import "./NavBar.css";

function NavBar({ isLoggedIn, onLoginClick, onLogout, openLogin }) {

    const navigate = useNavigate();

    return (
        <nav className="navbar">
            <div className="navbar-left">
                <Link className="navbar-logo" to="/">
                    <img src={Logo} alt="ScoreBoard" width="200" />
                </Link>

                <div className="navbar-links">
                    <NavLink className="custom-link" to="/">
                        Home
                    </NavLink>

                    <NavLink className="custom-link" to="/games">
                        All Games
                    </NavLink>

                    {isLoggedIn ? (
                        <NavLink className="custom-link" to="/my-games">
                            My Games
                        </NavLink>
                    ) : (
                        <button
                            className="my-games-button"
                            type="button"
                            onClick={onLoginClick}
                        >
                            My Games
                        </button>
                    )}

                    <NavLink className="custom-link" to="/stats">
                        Stats
                    </NavLink>

                </div>
            </div>
            <div className="navbar-right">
                {isLoggedIn ? (
                    <>
                        <NavLink className="custom-link" to="/account">
                            Account
                        </NavLink>

                        <button
                            className="logout-button"
                            type="button"
                            onClick={() => {
                                onLogout();
                                navigate("/");
                            }}
                        >
                            Logout
                        </button>
                    </>
                ) : (
                    <button className="login-link" type="button" onClick={onLoginClick}>
                        Login
                    </button>
                )}
            </div>
        </nav>
    );
}

export default NavBar;