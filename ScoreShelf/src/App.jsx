import { Routes, Route } from "react-router-dom";

import Landing from "./pages/Landing";
import Login from "./components/LoginModal";
import Register from "./components/RegisterModal";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
    </Routes>
  );
}

export default App;