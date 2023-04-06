import { useEffect } from "react";
import "./App.css";
import Cookies from "universal-cookie";
import { Route, Routes } from "react-router-dom";
import { useLocalState } from "./util/useLocalStorage";
import Dashboard from "./Dashboard";
import Homepage from "./Homepage";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";

function App() {
  const [jwt, setJwt] = useLocalState("", "jwt");

  // useEffect(() => {
  //   if (!jwt) {
  //     console.log("Hello World!");
  //     const requestBody = {
  //       username: "jesusarj",
  //       password: "asdfasdf",
  //     };

  //     fetch("auth/login", {
  //       headers: {
  //         "Content-Type": "application/json",
  //       },
  //       method: "POST",
  //       body: JSON.stringify(requestBody),
  //     }).then(() => {
  //       const cookies = new Cookies();
  //       setJwt(cookies.get("jwt"));
  //     });
  //   }
  // }, []);

  return (
    <Routes>
      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }
      />
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<Homepage />} />
    </Routes>
  );
}

export default App;
