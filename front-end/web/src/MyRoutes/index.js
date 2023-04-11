import { useLocalState } from "../util/useLocalStorage";
import Typography from "@mui/material/Typography";
import PersistentDrawerLeft from "../Sidebar/index.js";
const MyRoutes = () => {
  const [jwt, setJwt] = useLocalState("", "jwt");

  return (
      <PersistentDrawerLeft>
          <Typography variant="h1" fontSize={50}> Mis Rutas</Typography>
          <div> JWT token: {jwt} </div>
      </PersistentDrawerLeft>
  );
};

export default MyRoutes;
