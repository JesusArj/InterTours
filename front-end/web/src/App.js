import "./App.css";
import { Route, Routes } from "react-router-dom";
import AddRoute from "./AddRoute";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";
import MyRoutes from "./MyRoutes";
import Layout from "./Layout";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/mis-rutas"
        element={
          <PrivateRoute>
            <Layout>
              <MyRoutes />
            </Layout>
          </PrivateRoute>
        }
      />
      <Route
        path="/add-ruta"
        element={
          <PrivateRoute>
            <Layout>
              <AddRoute />
            </Layout>
          </PrivateRoute>
        }
      />
    </Routes>
  );
}

export default App;
