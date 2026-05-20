import { useState } from "react";
import { Routes, Route } from "react-router-dom";

import Landing from "./pages/Landing";
import GameList from "./pages/GameList";
import LoginModal from "./components/LoginModal";
import MyGames from "./pages/MyGames";
import GameForm from "./components/GameForm";
import ScoreSheetList from "./pages/ScoreSheetList";
import PlaySetup from "./pages/PlaySetup";
import ScoreSession from "./pages/ScoreSession";
import ScoreResults from "./pages/ScoreResults";
import Stats from "./pages/Stats"
import Account from "./pages/Account";

function App() {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem("user");
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const [showLogin, setShowLogin] = useState(false);

  function handleLogout() {
    setUser(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  }

  return (
    <>
      <Routes>
        <Route path="/" element={<Landing user={user} setUser={setUser} openLogin={() => setShowLogin(true)} onLogout={handleLogout} />} />
        <Route path="/games" element={<GameList user={user} setUser={setUser} openLogin={() => setShowLogin(true)} onLogout={handleLogout} />} />
        <Route path="/my-games" element={<MyGames user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/my-games/add" element={<GameForm user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/my-games/edit/:id" element={<GameForm user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/my-games/:gameId/score-sheets" element={<ScoreSheetList user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/my-games/:gameId/play" element={<PlaySetup user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/sessions/:gameSessionId/score" element={<ScoreSession user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/sessions/:gameSessionId/results" element={<ScoreResults user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/stats" element={<Stats user={user} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
        <Route path="/account" element={<Account user={user} setUser={setUser} onLogout={handleLogout} openLogin={() => setShowLogin(true)} />} />
      </Routes>

      {showLogin && (
        <LoginModal
          onClose={() => setShowLogin(false)}
          setUser={setUser}
        />
      )}
    </>
  );
}

export default App;