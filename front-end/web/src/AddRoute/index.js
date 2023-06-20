import React, { useState, useRef, useCallback, useEffect } from "react";
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
import DeleteIcon from "@mui/icons-material/Delete";
import SouthIcon from "@mui/icons-material/South";
import NorthIcon from "@mui/icons-material/North";
import { useLocation } from "react-router-dom";

const AddRoute = () => {
  const location = useLocation();
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
  const [files, setFiles] = useState([]);
  const navigate = useNavigate();
  const itemsRef = useRef([]);

  useEffect(() => {
    itemsRef.current = itemsRef.current.slice(0, stops.length);
  }, [stops]);

  const handlePlaceChanged = () => {
    const [place] = itemsRef.current[index].getPlaces() || [];
    if (place) {
      console.log(place);
      setPlaceInArray(place, index);
    }
  };

  const setDescriptionInArray = (value, arrayIndex) => {
    setStops((prev) => {
      const updatedStops = Array.from(prev);
      updatedStops[arrayIndex].descripcionParada = value;
      return updatedStops;
    });
  };

  const setNameInArray = (value) => {
    setStops((prev) => {
      const updatedStops = Array.from(prev);
      updatedStops[index].nombreParada = value;
      return updatedStops;
    });
  };

  const setPlaceInArray = (place, index) => {
    setStops((prev) => {
      const updatedStops = Array.from(prev);
      updatedStops[index].latitud = place.geometry.location.lat();
      updatedStops[index].longitud = place.geometry.location.lng();
      updatedStops[index].nombreParada = place.name;
      updatedStops[index].orden = index + 1;
      return updatedStops;
    });

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
  };

  const handleFileChange = (file, index) => {
    setFiles((prev) => {
      const updatedFiles = [...prev];
      updatedFiles[index] = file;
      console.log(updatedFiles);
      return updatedFiles;
    });
  };

  const addEmptyStop = () => {
    if (
      stops[stops.length - 1].nombreParada &&
      stops[stops.length - 1].descripcionParada
    ) {
      setStops((prev) => [...prev, {}]);
      setFiles((prev) => [...prev, null]); // Agregar una entrada vacía al array de archivos
    } else {
      setAlerts((prev) => ({ ...prev, showError1: true }));
    }
  };

  const removeStop = (index) => {
    if (stops.length > 1) {
      setStops((prev) => {
        const updatedStops = Array.from(prev);
        updatedStops.splice(index, 1);
        console.log(updatedStops);
        return updatedStops;
      });
      setFiles((prev) => {
        const updatedFiles = [...prev];
        updatedFiles.splice(index, 1);
        console.log(updatedFiles);
        return updatedFiles;
      });
    }
  };

  const moveStopUp = (index) => {
    if (index > 0) {
      setStops((prev) => {
        const updatedStops = Array.from(prev);
        const temp = updatedStops[index - 1];
        updatedStops[index - 1] = updatedStops[index];
        updatedStops[index] = temp;
        console.log(updatedStops);
        return updatedStops;
      });
      setFiles((prev) => {
        const updatedFiles = [...prev];
        const temp = updatedFiles[index - 1];
        updatedFiles[index - 1] = updatedFiles[index];
        updatedFiles[index] = temp;
        console.log(updatedFiles);
        return updatedFiles;
      });
    }
  };

  const moveStopDown = (index) => {
    if (index < stops.length - 1) {
      setStops((prev) => {
        const updatedStops = Array.from(prev);
        const temp = updatedStops[index + 1];
        updatedStops[index + 1] = updatedStops[index];
        updatedStops[index] = temp;
        console.log(updatedStops);
        return updatedStops;
      });
      setFiles((prev) => {
        const updatedFiles = [...prev];
        const temp = updatedFiles[index + 1];
        updatedFiles[index + 1] = updatedFiles[index];
        updatedFiles[index] = temp;
        console.log(updatedFiles);
        return updatedFiles;
      });
    }
  };

  function sendCreateRouteRequest() {
    const formData = new FormData();
    if (location.state && location.state.isEdit) {
      const ReqData = {
        titulo: routeData.routeName,
        descripcion: routeData.routeDescription,
        municipio: routeData.routeLocality,
        provincia: routeData.routeProvince,
        idRuta: location.state.routeEdit.idRuta,
        coordenadas: stops,
      };
      fetch(`api/rutas/editarRuta/${location.state.routeEdit.idRuta}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(ReqData),
      }).then((response) => {
        if (response.status === 200) {
          navigate("/mis-rutas", {
            state: { shouldShowAlert: true },
          });
        }
      });
    } else {
      formData.append("titulo", routeData.routeName);
      formData.append("descripcion", routeData.routeDescription);
      formData.append("autor", jwt_decode(jwt).sub);
      formData.append("municipio", routeData.routeLocality);
      formData.append("provincia", routeData.routeProvince);
      formData.append("coordenadas", JSON.stringify(stops));
      files.forEach((file, index) => {
        file
          ? formData.append("audios", file, `audio_${index}`)
          : formData.append("audios", null);
      });
      fetch("api/rutas/insertarRuta/", {
        method: "POST",
        body: formData,
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
  }

  const handleInputChange = (e, fieldName) => {
    setRouteData((prev) => ({ ...prev, [fieldName]: e.target.value }));
  };

  const handleAlertClose = (alertName) => {
    setAlerts((prev) => ({ ...prev, [alertName]: false }));
  };

  useEffect(() => {
    if (location.state && location.state.isEdit && location.state.routeEdit) {
      setRouteData({
        routeName: location.state.routeEdit.titulo,
        routeDescription: location.state.routeEdit.descripcion,
        routeLocality: location.state.routeEdit.municipio,
        routeProvince: location.state.routeEdit.provincia,
      });
      setStops((prev) => {
        const updatedStops = [...prev];
        const coordenadas = location.state.routeEdit.coordenadas;

        for (let index = 0; index < coordenadas.length; index++) {
          const element = coordenadas[index];

          updatedStops[index] = {
            nombreParada: element.nombreParada,
            descripcionParada: element.descripcionParada,
            latitud: element.latitud,
            longitud: element.longitud,
            orden: element.orden,
            audio:
              element.audio &&
              parseInt(element.audio.charAt(element.audio.length - 1)) ===
                parseInt(element.orden)
                ? element.audio.substring(element.audio.lastIndexOf("/") + 1)
                : null,
          };
        }
        return updatedStops;
      });
    }
  }, []);
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
                <Grid item xs={12} key={index}>
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
                          onLoad={(ref) => (itemsRef.current[index] = ref)}
                          onPlacesChanged={handlePlaceChanged}>
                          <TextField
                            helperText="Ejemplo: Mezquita Catedral de Córdoba"
                            id={"nombreParada" + index}
                            label="Nombre"
                            value={stops[index].nombreParada}
                            fullWidth
                            required
                            onChange={(e) => {
                              setNameInArray(e.target.value);
                            }}
                            onClick={() => {
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
                          value={stops[index].descripcionParada}
                          onChange={(e) => {
                            setDescriptionInArray(e.target.value, index);
                          }}
                          onClick={() => {
                            setIndex(index);
                          }}
                        />
                      </Grid>
                      <Grid container>
                        <Grid item xs={6}>
                          <Button
                            variant="outlined"
                            component="label"
                            sx={{ mt: 5 }}>
                            Añadir Audio
                            <input
                              type="file"
                              hidden
                              accept=".mp3, .wav"
                              onChange={(e) => {
                                handleFileChange(e.target.files[0], index);
                              }}
                            />
                          </Button>
                          <br />
                          <Typography variant="caption">
                            {files[index]
                              ? files[index].name
                              : stops[index].audio }
                          </Typography>
                        </Grid>
                        <Grid
                          item
                          xs={6}
                          container
                          justifyContent="flex-end"
                          sx={{ mt: 5 }}>
                          {index > 0 && (
                            <Grid item>
                              <IconButton
                                onClick={() => {
                                  moveStopUp(index);
                                }}>
                                <NorthIcon color="primary" />
                              </IconButton>
                            </Grid>
                          )}
                          <Grid item>
                            <IconButton
                              onClick={() => {
                                moveStopDown(index);
                              }}>
                              <SouthIcon color="primary" />
                            </IconButton>
                          </Grid>

                          <Grid item>
                            <IconButton
                              onClick={() => {
                                removeStop(index);
                              }}>
                              <DeleteIcon color="primary" />
                            </IconButton>
                          </Grid>
                        </Grid>
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
