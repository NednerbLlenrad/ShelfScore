import { useState } from "react";
import Landing from "./pages/Landing";

function App() {
  const [user, setUser] = useState(null);

  return <Landing user={user} setUser={setUser} />;
}

export default App;