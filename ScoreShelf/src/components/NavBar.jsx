import { Link, NavLink } from "react-router-dom";
import Logo from "../assets/Logo.svg";
import "./NavBar.css";

function NavBar({ isLoggedIn }) {
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

                    <NavLink className="custom-link" to="/my-games">
                        My Games
                    </NavLink>

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

                        <button>Logout</button>
                    </>
                ) : (
                    <>
                        <NavLink className="login-link" to="/login">
                            Login
                        </NavLink>
                    </>
                )}
            </div>
        </nav>
    );
}

export default NavBar;