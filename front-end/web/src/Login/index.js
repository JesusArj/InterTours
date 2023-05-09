import React, { useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import Button from "@mui/joy/Button";
import FormControl from "@mui/joy/FormControl";
import FormLabel from "@mui/joy/FormLabel";
import Input from '@mui/joy/Input';
import { Sheet } from "@mui/joy";
import Typography from "@mui/joy/Typography";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [jwt, setJwt] = useLocalState("", "jwt");
  
  function sendLoginRequest() {
    const requestBody = {
      username: username,
      password: password,
    };

    fetch("api/auth/login", {
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
      body: JSON.stringify(requestBody),
    })
      .then((response) => {
        if (response.status === 200) return response.text();
        else if (response.status === 401 || response.status === 403) {
          alert("Usuario o contraseña incorrectos");
        } else {
          alert(
            "Ha ocurrido un error, si el error persiste contacte con el administrador"
          );
        }
      })
      .then((data) => {
        if (data) {
          setJwt(data);
          window.location.href = "/mis-rutas";
        }
      });
  }

  return (

  <Sheet
          sx={{
            width: 300,
            mx: 'auto', 
            my: 4, 
            py: 3, 
            px: 2, 
            display: 'flex',
            flexDirection: 'column',
            gap: 2,
            borderRadius: 'sm',
            boxShadow: 'md',
          }}
          variant="outlined"
        >
      <Typography level="h1" fontSize="xl" sx={{textAlign : "center" }}> Bienvenid@ a EspaTours! </Typography>
      <FormControl>
        <FormLabel>Username</FormLabel>
        <Input 
        id="username" 
        type="text" 
        placeholder="jesusarj"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        />
      </FormControl>

      <FormControl>
        <FormLabel>Password</FormLabel>
        <Input 
        id="password" 
        type="password" 
        placeholder="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        />
      </FormControl> 

        <Button
          variant="soft"
          id="submit"
          type="button"
          sx={{ mt: 1 /* margin top */ }}
          onClick={() => sendLoginRequest()}>
          Login
        </Button>
      </Sheet>
  );
};

export default Login;
