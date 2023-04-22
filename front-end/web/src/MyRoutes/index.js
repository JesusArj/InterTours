import { useLocalState } from "../util/useLocalStorage";
import Typography from "@mui/material/Typography";
import PersistentDrawerLeft from "../Sidebar/index.js";
import Alert from "@mui/material/Alert";
import {useLocation} from 'react-router-dom';

const MyRoutes = () => {
  const location = useLocation();
  const [jwt, setJwt] = useLocalState("", "jwt");

  return (
      <PersistentDrawerLeft>
        {location.state && location.state.shouldShowAlert && <Alert severity="success">Ruta creada con éxito</Alert>}
          <Typography variant="h1" fontSize={50}> Mis Rutas</Typography>
          <div> JWT token: {jwt} </div>
      </PersistentDrawerLeft>
  );
};

export default MyRoutes;
