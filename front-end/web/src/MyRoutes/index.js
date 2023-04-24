import { useLocalState } from "../util/useLocalStorage";
import Typography from "@mui/material/Typography";
import PersistentDrawerLeft from "../Sidebar/index.js";
import Alert from "@mui/material/Alert";
import {useLocation} from 'react-router-dom';
import jwt_decode from "jwt-decode";
import { useEffect } from "react";

const MyRoutes = () => {
  const location = useLocation();
  const [jwt, setJwt] = useLocalState("", "jwt");

    useEffect(() => {
    fetch(`api/rutas/${jwt_decode(jwt).sub}`)
      .then((response) => response.json())
      .then((data) => console.log(data));
     }, []);

  return (
      <PersistentDrawerLeft>
        {location.state && location.state.shouldShowAlert && <Alert severity="success">Ruta creada con éxito</Alert>}
          <Typography variant="h1" fontSize={50}> Mis Rutas</Typography>
          <div> JWT token: {jwt} </div>
      </PersistentDrawerLeft>
  );
};

export default MyRoutes;
