import { useEffect } from "react";
import "./App.css";
import Cookies from "universal-cookie";
import { useLocalState } from "./util/useLocalStorage";

function App() {
  const [jwt, setJwt] = useLocalState("", "jwt");

  useEffect(() => {
    if (!jwt) {
      console.log("Hello World!");
      const requestBody = {
        username: "jesusarj",
        password: "asdfasdf",
      };

      fetch("auth/login", {
        headers: {
          "Content-Type": "application/json",
        },
        method: "POST",
        body: JSON.stringify(requestBody),
      }).then(() => {
        const cookies = new Cookies();
        setJwt(cookies.get("jwt"));
      });
    }
  }, []);

  return (
    <div className="App">
      {" "}
      <h1> Hello World! </h1>
      <div> JWT Value is {jwt} </div>
    </div>
  );
}

export default App;
