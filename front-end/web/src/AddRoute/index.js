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

const AddRoute = () => {
  const [routeName, setRouteName] = useState("");
  const [routeDescription, setRouteDescription] = useState("");
  const [stops, setStops] = useState([{}]);
  const [showError1, setShowError1] = useState(false);
  const [showError2, setShowError2] = useState(false);
  const [index, setIndex] = useState(0);

  const setNameInArray = (value, arrayIndex) => {
    stops[arrayIndex].name = value;
    console.log(stops);
  };

  const clearNameInArray = (arrayIndex) => {
    stops[arrayIndex].name = "";
    console.log(stops);
  };

  const setDescriptionInArray = (value, arrayIndex) => {
    stops[arrayIndex].description = value;
    console.log(stops);
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
        handleLatLong(place, index);
        setNameInArray(place.name, index);
        console.log(stops);
      }
    }
  };

  const setLatLongInArray = (valueLat, valueLong, index) => {
    stops[index].lat = valueLat;
    stops[index].long = valueLong;
  };

  function handleLatLong(place, index) {
    setLatLongInArray(
      place.geometry.location.lat(),
      place.geometry.location.lng(),
      index
    );
  }

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
    if (stops[stops.length - 1].name && stops[stops.length - 1].description) {
      const stopsCopy = stops.slice();
      stopsCopy.push({});
      setStops(stopsCopy);
    } else {
      setShowError1(true);
    }
  };

  return (
    <PersistentDrawerLeft>
      <Card style={{ maxWidth: 450, margin: "0 auto" }}>
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
                    El lugar seleccionado no está dentro del territorio Español.
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
                <Button variant="contained" color="primary">
                  {" "}
                  Crear Ruta{" "}
                </Button>
              </Grid>
            </Grid>
          </form>
        </CardContent>
      </Card>
    </PersistentDrawerLeft>
  );
};

export default AddRoute;
