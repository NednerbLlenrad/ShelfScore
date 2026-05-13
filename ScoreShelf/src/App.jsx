import { useState } from "react";
import Landing from "./pages/Landing";

function App() {
  const [user, setUser] = useState(() => {
  const savedUser = localStorage.getItem("user");

  return savedUser ? JSON.parse(savedUser) : null;
});

  return <Landing user={user} setUser={setUser} />;
}

export default App;