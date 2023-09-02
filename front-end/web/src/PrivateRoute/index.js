/**
 * COMPONENTE QUE COMPRUEBA SI EL USUARIO TIENE UN JWT VÁLIDO
 * PARA NAVEGAR POR LA APLICACIÓN. 
 * EN CASO TRUE DEVUELVE LA PANTALLA.
 * EN CASO FALSE REDIRIGE AL LOGIN.
 */
import React from "react";
import { Navigate } from "react-router-dom";
import { useLocalState } from "../util/useLocalStorage";
import { useState } from "react";

const PrivateRoute = ({ children }) => {
  const [isValid, setIsValid] = useState(null);
  const [jwt, setJwt] = useLocalState("", "jwt");
  const [isLoading, setIsLoading] = useState(true);

  if (jwt) {
    fetch(`api/auth/validate?token=${jwt}`, {
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
      },
      method: "GET",
    })
      .then((response) => {
        if (response.status === 200) {
          const contentType = response.headers.get("content-type");
          if (contentType && contentType.indexOf("application/json") !== -1) {
            return response.json();
          } else {
            return response.text();
          }
        }
      })
      .then((isValid) => {
        setIsValid(isValid);
        setIsLoading(false);
      });
    return isLoading ? (
      <div>Loading...</div>
    ) : isValid === true ? (
      children
    ) : (
      <Navigate to="/login" />
    );
  }else{
    return <Navigate to="/login" />;
  }
};

export default PrivateRoute;
