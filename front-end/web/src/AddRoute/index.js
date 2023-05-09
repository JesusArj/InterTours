import React, { useState, useRef, useCallback } from "react";
import {
  TextField,
  Button,
  Grid,
  Card,
  CardContent,
  Typography,
  Collapse,
  IconButton,
} from "@mui/material";
import { StandaloneSearchBox } from "@react-google-maps/api";
import Alert from "@mui/material/Alert";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import jwt_decode from "jwt-decode";
import { useLocalState } from "../util/useLocalStorage";
import { useNavigate } from "react-router-dom";
import DeleteIcon from '@mui/icons-material/Delete';

const AddRoute = () => {
  const [routeData, setRouteData] = useState({
    routeName: "",
    routeDescription: "",
    routeLocality: "",
    routeProvince: "",
  });
  const [stops, setStops] = useState([{}]);
  const [alerts, setAlerts] = useState({
    showError1: false,
    showSuccess: false,
    showDefaultError: false,
    showBadRequest: false,
  });
  const [index, setIndex] = useState(0);
  const [jwt, setJwt] = useLocalState("", "jwt");
  const navigate = useNavigate();

  const setDescriptionInArray = useCallback(
    (value, arrayIndex) => {
      stops[arrayIndex].descripcionParada = value;
    },
    [stops]
  );

  const inputRef = useRef();

  const handlePlaceChanged = () => {
    const [place] = inputRef.current.getPlaces();
    if (place) {
        setPlaceInArray(place, index);
    }
  };

  const setPlaceInArray = useCallback(
    (place, index) => {
      stops[index].latitud = place.geometry.location.lat();
      stops[index].longitud = place.geometry.location.lng();
      stops[index].nombreParada = place.name;
      stops[index].orden = index + 1;
      if (index === 0) {
        place.address_components.forEach((element) => {
          if (element.types[0] === "locality") {
            setRouteData((prev) => ({
              ...prev,
              routeLocality: element.long_name,
            }));
          }
          if (element.types[0] === "administrative_area_level_2") {
            setRouteData((prev) => ({
              ...prev,
              routeProvince: element.long_name,
            }));
          }
        });
      }
    },
    [stops]
  );

  const addEmptyStop = () => {
    if (
      stops[stops.length - 1].nombreParada &&
      stops[stops.length - 1].descripcionParada
    ) {
      setStops((prev) => [...prev, {}]);
    } else {
      setAlerts((prev) => ({ ...prev, showError1: true }));
    }
  };

  const removeStop = (index) => {
    console.log(stops); 
    if (stops.length > 1) {
      setStops((prev) => {
        const updatedStops = Array.from(prev);
        updatedStops.splice(index, 1);
        return updatedStops;
      });
    }
  };

  function sendCreateRouteRequest() {
    const data = {
      titulo: routeData.routeName,
      descripcion: routeData.routeDescription,
      autor: jwt_decode(jwt).sub,
      municipio: routeData.routeLocality,
      provincia: routeData.routeProvince,
      coordenadas: stops,
    };

    fetch("api/rutas/insertarRuta/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }).then((response) => {
      if (response.status === 200) {
        navigate("/mis-rutas", {
          state: { shouldShowAlert: true },
        });
      } else if (response.status === 400) {
        setAlerts((prev) => ({ ...prev, showBadRequest: true }));
      } else {
        setAlerts((prev) => ({ ...prev, showDefaultError: true }));
      }
    });
  }

  const handleInputChange = (e, fieldName) => {
    setRouteData((prev) => ({ ...prev, [fieldName]: e.target.value }));
  };

  const handleAlertClose = (alertName) => {
    setAlerts((prev) => ({ ...prev, [alertName]: false }));
  };
  return (
    <>
      <h1 style={{ textAlign: "center", color: "#3276D2" }}>AÑADIR RUTA</h1>
      <Card style={{ maxWidth: 500, margin: "0 auto" }}>
        {alerts.showSuccess && (
          <Collapse in={alerts.showSuccess}>
            <Alert
              severity="success"
              onClose={() => {
                handleAlertClose("showSuccess");
              }}>
              Ruta añadida con éxito
            </Alert>
          </Collapse>
        )}
        <CardContent>
          <form>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  helperText="Nombre que identifique la ruta"
                  id="name"
                  value={routeData.routeName}
                  onChange={(e) => handleInputChange(e, "routeName")}
                  label="Nombre"
                  fullWidth
                  required
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  helperText="Descripción de la ruta"
                  id="description"
                  value={routeData.routeDescription}
                  onChange={(e) => handleInputChange(e, "routeDescription")}
                  label="Descripción"
                  multiline
                  fullWidth
                  required
                />
              </Grid>

              {stops.map((element, index) => (
                <Grid item xs={12} key={element.nombreParada}>
                  <Accordion>
                    <AccordionSummary
                      expandIcon={<ExpandMoreIcon />}
                      aria-controls={"panel" + index + "d-content"}
                      id={"panel" + index + "d-header"}>
                      <Typography>Parada {index + 1} </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                      <Grid item xs={12}>
                        <StandaloneSearchBox
                          onLoad={(ref) => (inputRef.current = ref)}
                          onPlacesChanged={handlePlaceChanged}>
                          <TextField
                            helperText="Ejemplo: Mezquita Catedral de Córdoba"
                            id={"nombreParada" + index}
                            label="Nombre"
                            fullWidth
                            required
                            onChange={() => {
                              setIndex(index);
                            }}
                          />
                        </StandaloneSearchBox>
                      </Grid>
                      <Grid item xs={12}>
                        <TextField
                          helperText="Descripción de la parada"
                          id={"descripcionParada" + index}
                          label="Descripción"
                          multiline
                          minRows={2}
                          fullWidth
                          required
                          onChange={(e) => {
                            setDescriptionInArray(e.target.value, index);
                          }}
                        />
                      </Grid>
                      <Grid item xs={12}>
                        <Button
                          variant="outlined"
                          component="label"
                          sx={{ mt: 5 }}>
                          Añadir Audio
                          <input type="file" hidden accept=".mp3" />
                        </Button>
                        <IconButton
                        sx={{ mt: 5, ml: 30}}
                          onClick={() => {
                            removeStop(index);
                          }}>
                          <DeleteIcon 
                          color="primary"/>
                        </IconButton>
                      </Grid>
                    </AccordionDetails>
                  </Accordion>
                </Grid>
              ))}
              <Grid item xs={12} align="center">
                {alerts.showError1 && (
                  <Collapse in={alerts.showError1}>
                    <Alert
                      severity="error"
                      onClose={() => {
                        handleAlertClose("showError1");
                      }}>
                      Completa la parada anterior antes de añadir una nueva.
                    </Alert>
                  </Collapse>
                )}
              </Grid>
              <Grid item xs={12} align="center">
                <Button
                  onClick={() => {
                    addEmptyStop();
                  }}>
                  <AddCircleIcon fontSize="large" />
                </Button>
              </Grid>
              {alerts.showBadRequest && (
                <Collapse in={alerts.showBadRequest}>
                  <Alert
                    severity="error"
                    onClose={() => {
                      handleAlertClose("showBadRequest");
                    }}>
                    Por favor, revisa los datos de la ruta. Recuerda que no
                    puedes dejar ningún campo vacío.
                  </Alert>
                </Collapse>
              )}

              {alerts.showDefaultError && (
                <Collapse in={alerts.showDefaultError}>
                  <Alert
                    severity="error"
                    onClose={() => {
                      handleAlertClose("showDefaultError");
                    }}>
                    Ha ocurrido un error conectando con el sistema. Si el error
                    persiste, contacte con el administrador.
                  </Alert>
                </Collapse>
              )}

              <Grid item xs={12} align="center">
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => {
                    sendCreateRouteRequest();
                  }}>
                  {" "}
                  Crear Ruta{" "}
                </Button>
              </Grid>
            </Grid>
          </form>
        </CardContent>
      </Card>
    </>
  );
};
export default AddRoute;
