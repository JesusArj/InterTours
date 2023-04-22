import React, { useState, useRef } from "react";
import { TextField, Button, Grid, Card } from "@material-ui/core";
import PersistentDrawerLeft from "../Sidebar";
import { CardContent, Typography } from "@mui/material";
import { StandaloneSearchBox } from "@react-google-maps/api";
import Alert from "@mui/material/Alert";
import AddCircleIcon from "@mui/icons-material/AddCircle";
import Collapse from "@mui/material/Collapse";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import jwt_decode from "jwt-decode";
import { useLocalState } from "../util/useLocalStorage";
import { useNavigate } from "react-router-dom";

const AddRoute = () => {
  const [routeName, setRouteName] = useState("");
  const [routeDescription, setRouteDescription] = useState("");
  const [routeLocality, setRouteLocality] = useState("");
  const [routeProvince, setRouteProvince] = useState("");
  const [stops, setStops] = useState([{}]);
  const [showError1, setShowError1] = useState(false);
  const [showError2, setShowError2] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const [showDefaultError, setShowDefaultError] = useState(false);
  const [showBadRequest, setShowBadRequest] = useState(false);
  const [index, setIndex] = useState(0);
  const [jwt, setJwt] = useLocalState("", "jwt");
  const navigate = useNavigate();

  const setDescriptionInArray = (value, arrayIndex) => {
    stops[arrayIndex].descripcionParada = value;
  };

  const inputRef = useRef();
  const southWest = new window.google.maps.LatLng(35.8579, -9.3924);
  const northEast = new window.google.maps.LatLng(43.7183, 4.9971);
  const bounds = new window.google.maps.LatLngBounds(southWest, northEast);

  const handlePlaceChanged = () => {
    const [place] = inputRef.current.getPlaces();
    if (place) {
      if (!isSpain(place)) {
        setShowError2(true);
      } else {
        setPlaceInArray(place, index);
      }
    }
  };

  const setPlaceInArray = (place, index) => {
    stops[index].latitud = place.geometry.location.lat();
    stops[index].longitud = place.geometry.location.lng();
    stops[index].nombreParada = place.name;
    stops[index].orden = index + 1;
    if (index === 0) {
      place.address_components.forEach((element) => {
        if (element.types[0] === "locality") {
          setRouteLocality(element.long_name);
        }
        if (element.types[0] === "administrative_area_level_2") {
          setRouteProvince(element.long_name);
        }
      });
    }
  };

  function isSpain(place) {
    let isSpain = false;
    place.address_components.forEach((element) => {
      if (element.types[0] === "country" && element.short_name === "ES") {
        isSpain = true;
      }
    });
    return isSpain;
  }

  const addEmptyStop = () => {
    if (
      stops[stops.length - 1].nombreParada &&
      stops[stops.length - 1].descripcionParada
    ) {
      const stopsCopy = stops.slice();
      stopsCopy.push({});
      setStops(stopsCopy);
    } else {
      setShowError1(true);
    }
  };

  function sendCreateRuteRequest() {
    const data = {
      titulo: routeName,
      descripcion: routeDescription,
      autor: jwt_decode(jwt).sub,
      municipio: routeLocality,
      provincia: routeProvince,
      coordenadas: stops,
    };
    console.log(data);
    console.log(jwt_decode(jwt));
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
        })
      } else if (response.status === 400) {
        setShowBadRequest(true);
      } else {
        setShowDefaultError(true);
      }
    });
  }

  return (
    <PersistentDrawerLeft>
      <Card style={{ maxWidth: 450, margin: "0 auto" }}>
        {showSuccess && (
          <>
            <Collapse in={showSuccess}>
              <Alert
                severity="success"
                onClose={() => {
                  setShowSuccess(false);
                }}>
                Ruta añadida con éxito
              </Alert>
            </Collapse>
          </>
        )}
        <CardContent>
          <form>
            <Typography fontSize={25} textAlign="center">
              {" "}
              Añadir Ruta{" "}
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  helperText="Nombre que identifique la ruta"
                  id="name"
                  value={routeName}
                  onChange={(e) => setRouteName(e.target.value)}
                  label="Nombre"
                  fullWidth
                  required
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  helperText="Descripción de la ruta"
                  id="description"
                  value={routeDescription}
                  onChange={(e) => setRouteDescription(e.target.value)}
                  label="Descripción"
                  multiline
                  fullWidth
                  required
                />
              </Grid>

              {stops.map((element, index) => (
                <>
                  <Grid item xs={12}>
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
                            onPlacesChanged={handlePlaceChanged}
                            bounds={bounds}>
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
                        </Grid>
                      </AccordionDetails>
                    </Accordion>
                  </Grid>
                </>
              ))}

              {showError1 && (
                <>
                  <br></br>
                  <Collapse in={showError1}>
                    <Alert
                      severity="error"
                      onClose={() => {
                        setShowError1(false);
                      }}>
                      Completa la parada anterior antes de añadir una nueva.
                    </Alert>
                  </Collapse>
                </>
              )}
              {showError2 && (
                <>
                  <br></br>
                  <Collapse in={showError2}>
                    <Alert
                      severity="error"
                      onClose={() => {
                        setShowError2(false);
                      }}
                      sx={{
                        textAlign: "center",
                      }}>
                      El lugar seleccionado no está dentro del territorio
                      Español.
                    </Alert>
                  </Collapse>
                </>
              )}
              <Grid item xs={12} align="center">
                <Button
                  onClick={() => {
                    addEmptyStop();
                  }}>
                  <AddCircleIcon fontSize="large" />
                </Button>
              </Grid>

              <Grid item xs={12} align="center">
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => {
                    sendCreateRuteRequest();
                  }}>
                  {" "}
                  Crear Ruta{" "}
                </Button>
              </Grid>
            </Grid>
          </form>
          {showBadRequest && (
            <>
              <br></br>
              <Collapse in={showBadRequest}>
                <Alert
                  severity="error"
                  onClose={() => {
                    setShowBadRequest(false);
                  }}>
                  Por favor, revisa los datos de la ruta. Recuerda que solo se
                  aceptan lugares dentro del territorio Español y que no puedes
                  dejar ningún campo vacío.
                </Alert>
              </Collapse>
            </>
          )}

          {showDefaultError && (
            <>
              <br></br>
              <Collapse in={showDefaultError}>
                <Alert
                  severity="error"
                  onClose={() => {
                    setShowDefaultError(false);
                  }}>
                  Ha ocurrido un error conectando con el sistema. Si el error
                  persiste, contacte con el administrador.
                </Alert>
              </Collapse>
            </>
          )}
        </CardContent>
      </Card>
    </PersistentDrawerLeft>
  );
};

export default AddRoute;
