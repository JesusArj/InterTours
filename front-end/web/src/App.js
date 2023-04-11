import "./App.css";
import { Route, Routes } from "react-router-dom";
import AddRoute from "./AddRoute";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";
import MyRoutes from "./MyRoutes";

function App() {
  return (
    <Routes>
      <Route
        path="/mis-rutas"
        element={
          <PrivateRoute>
            <MyRoutes />
          </PrivateRoute>
        }
      />
      <Route path="/login" element={<Login />} />
      <Route
        path="/add-ruta"
        element={
          <PrivateRoute>
            <AddRoute />
          </PrivateRoute>
        }
      />
    </Routes>
  );
}

export default App;
