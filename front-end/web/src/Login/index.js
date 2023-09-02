/**
 * PANTALLA PARA LOGIN/REGISTRO
 */
import React, { useState } from "react";
import { useLocalState } from "../util/useLocalStorage";
import Button from "@mui/joy/Button";
import FormControl from "@mui/joy/FormControl";
import FormLabel from "@mui/joy/FormLabel";
import Input from "@mui/joy/Input";
import { Sheet } from "@mui/joy";
import Typography from "@mui/joy/Typography";
import { Link, useNavigate } from "react-router-dom";
import { Alert, Dialog, TextField, Tooltip } from "@mui/material";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Grid from "@mui/material/Grid";
import VisibilityIcon from "@mui/icons-material/Visibility";
import VisibilityOffIcon from "@mui/icons-material/VisibilityOff";
import InputAdornment from "@mui/material/InputAdornment";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";

const Login = () => {
  /**
  * DECLARACIÓN DE VARIABLES Y FUNCIONES
  */
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [registerUsername, setRegisterUsername] = useState("");
  const [registerPassword, setRegisterPassword] = useState("");
  const [repeatRegisterPassword, setRepeatRegisterPassword] = useState("");
  const [jwt, setJwt] = useLocalState("", "jwt");
  const [showRegisterDialog, setShowRegisterDialog] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showRegisterSuccessAlert, setShowRegisterSuccessAlert] = useState(false);

  const handleCloseRegisterDialog = () => setShowRegisterDialog(false);

  const handleOpenRegisterDialog = () => setShowRegisterDialog(true);

  const handleClickShowPassword = () => setShowPassword(!showPassword);

  const handleClickCloseAlert = () => setShowRegisterSuccessAlert(false);

  //Comprobaciones de formulario de registro y llamada a la API en caso OK.
  function sendRegisterRequest() {
    if (
      registerUsername === "" ||
      registerPassword === "" ||
      repeatRegisterPassword === ""
    ) {
      alert("Rellene todos los campos");
      return;
    } else if (registerPassword !== repeatRegisterPassword) {
      alert("Las contraseñas no coinciden");
      return;
    } else if (registerPassword.length < 8) {
      alert("La contraseña debe tener al menos 8 caracteres");
      return;
    }

    const registerRequestBody = {
      username: registerUsername,
      password: registerPassword,
    };

    fetch("api/auth/registerGuiaTuristico", {
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
      body: JSON.stringify(registerRequestBody),
    }).then((response) => {
      if (response.status === 200) {
        setShowRegisterSuccessAlert(true);
        setRegisterPassword("");
        setRepeatRegisterPassword("");
        setRegisterUsername("");
        handleCloseRegisterDialog();
      } else if (response.status === 409) {
        alert("El usuario ya existe");
      } else {
        alert(
          "Ha ocurrido un error, si el error persiste contacte con el administrador"
        );
      }
    });
  }

  //Llamada a la API para hacer LOGIN
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

  /**
  * RENDERIZADO DE PANTALLA
  */
  return (
    <>
    {showRegisterSuccessAlert && ( <Alert
  open={showRegisterSuccessAlert}
  severity="success"
  sx={{
    width: 300,
    mx: "auto",
    my: 4,
    py: 1, 
    flexDirection: "column",
    borderRadius: "sm",
    position: "relative", 
  }}
>
  <IconButton
    aria-label="Cerrar"
    size="small"
    style={{
      position: "absolute", 
      top: "0.5rem", 
      right: "0.5rem",
    }}
    onClick={() => handleClickCloseAlert()}
  >
    <CloseIcon fontSize="small" />
  </IconButton>
  Usuario registrado correctamente. Ya puede iniciar sesión con su usuario y contraseña.
</Alert>)}
     
      <Sheet
        sx={{
          width: 300,
          mx: "auto",
          my: 4,
          py: 3,
          px: 2,
          display: "flex",
          flexDirection: "column",
          gap: 2,
          borderRadius: "sm",
          boxShadow: "md",
        }}
        variant="outlined">
        <Typography level="h1" fontSize="xl" sx={{ textAlign: "center" }}>
          {" "}
          Bienvenid@ a InterTours!{" "}
        </Typography>
        <FormControl>
          <FormLabel>Username</FormLabel>
          <Input
            id="username"
            type="text"
            placeholder="username"
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
        <Typography>
          {" "}
          ¿No estás registrado?{" "}
          <Link onClick={() => handleOpenRegisterDialog()}>
            Pulsa aquí
          </Link>{" "}
        </Typography>
      </Sheet>
      <Dialog
        onClose={handleCloseRegisterDialog}
        open={showRegisterDialog}
        maxWidth="xs"
        fullWidth>
        <DialogTitle>Registro</DialogTitle>
        <DialogContent>
          <Grid container spacing={1}>
            <Grid item xs={12} sm={12}>
              <Tooltip
                title="Su nombre de usuario será público para los usuarios que vean sus rutas"
                placement="right">
                <TextField
                  id="username"
                  type="text"
                  size="small"
                  label="Usuario"
                  fullWidth
                  sx={{ mt: 1 /* margin top */ }}
                  required
                  value={registerUsername}
                  onChange={(e) => setRegisterUsername(e.target.value)}
                />
              </Tooltip>
            </Grid>
            <Grid item xs={12} sm={12}>
              <Tooltip
                title="La contraseña debe tener al menos 8 caracteres"
                placement="right">
                <TextField
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={() => handleClickShowPassword()}
                          size="small">
                          {showPassword ? (
                            <VisibilityIcon />
                          ) : (
                            <VisibilityOffIcon />
                          )}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                  id="password"
                  label="Contraseña"
                  type={showPassword ? "text" : "password"}
                  size="small"
                  fullWidth
                  required
                  value={registerPassword}
                  onChange={(e) => setRegisterPassword(e.target.value)}
                />
              </Tooltip>
            </Grid>
            <Grid item xs={12} sm={12}>
              <TextField
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => handleClickShowPassword()}
                        size="small">
                        {showPassword ? (
                          <VisibilityIcon />
                        ) : (
                          <VisibilityOffIcon />
                        )}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
                id="password"
                label="Contraseña"
                type={showPassword ? "text" : "password"}
                size="small"
                fullWidth
                helperText="Repita la contraseña"
                required
                value={repeatRegisterPassword}
                onChange={(e) => setRepeatRegisterPassword(e.target.value)}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button
            variant="soft"
            type="button"
            onClick={() => sendRegisterRequest()}>
            Registro
          </Button>
          <Button
            onClick={handleCloseRegisterDialog}
            color="primary"
            variant="soft"
            type="button">
            Cancelar
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default Login;
