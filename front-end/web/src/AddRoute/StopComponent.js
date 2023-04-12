import React, { useState, useRef } from "react";
import ArrowForwardIosSharpIcon from "@mui/icons-material/ArrowForwardIosSharp";
import MuiAccordionDetails from "@mui/material/AccordionDetails";
import { styled } from "@mui/material/styles";
import MuiAccordion from "@mui/material/Accordion";
import MuiAccordionSummary from "@mui/material/AccordionSummary";
import { Grid, TextField, Button } from "@mui/material";
import { Typography } from "@mui/material";
import { StandaloneSearchBox, LoadScript } from "@react-google-maps/api";

const StopComponent = (props) => {
  const { countParada } = props;

  let [expanded, setExpanded] = useState(false);

  const handleChange = () => {
    setExpanded(!expanded);
  };

  const Accordion = styled((props) => (
    <MuiAccordion disableGutters elevation={0} square {...props} />
  ))(({ theme }) => ({
    border: `1px solid ${theme.palette.divider}`,
    "&:not(:last-child)": {
      borderBottom: 0,
    },
    "&:before": {
      display: "none",
    },
  }));

  const AccordionSummary = styled((props) => (
    <MuiAccordionSummary
      expandIcon={<ArrowForwardIosSharpIcon sx={{ fontSize: "0.9rem" }} />}
      {...props}
    />
  ))(({ theme }) => ({
    backgroundColor: "rgba(0, 0, 0, .02)",
    flexDirection: "row-reverse",
    "& .MuiAccordionSummary-expandIconWrapper.Mui-expanded": {
      transform: "rotate(90deg)",
    },
    "& .MuiAccordionSummary-content": {
      marginLeft: theme.spacing(1),
    },
  }));

  const AccordionDetails = styled(MuiAccordionDetails)(({ theme }) => ({
    padding: theme.spacing(2),
    borderTop: "1px solid rgba(0, 0, 0, .125)",
  }));

  const inputRef = useRef();

  const handlePlaceChanged = () => {
    const [place] = inputRef.current.getPlaces();
    console.log(place);
    if (place) {
      console.log(place.formatted_address);
      console.log(place.geometry.location.lat());
      console.log(place.geometry.location.lng());
    }
  };

  return (
    <Grid item xs={12}>
      <Accordion expanded={expanded} onChange={handleChange}>
        <AccordionSummary
          aria-controls={"panel" + countParada + "d-content"}
          id={"panel" + countParada + "d-header"}>
          <Typography>Parada {countParada} </Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Grid item xs={12}>
            <LoadScript
              googleMapsApiKey="AIzaSyBD-G6oLUdzpDbD9mPRepdMhXgny0XP1BU"
              libraries={["places"]}>
              <StandaloneSearchBox
                onLoad={(ref) => (inputRef.current = ref)}
                onPlacesChanged={handlePlaceChanged}>
                <TextField
                  helperText="Ejemplo: Mezquita Catedral de Córdoba"
                  id={"nombreParada" + countParada}
                  label="Nombre"
                  fullWidth
                  required
                />
              </StandaloneSearchBox>
            </LoadScript>
          </Grid>
          <Grid item xs={12}>
            <TextField
              helperText="Descripción de la parada"
              id={"descripcionParada" + countParada}
              label="Descripción"
              multiline
              minRows={2}
              fullWidth
              required
            />
          </Grid>
          <Grid item xs={12}>
            <Button variant="outlined" component="label" sx={{ mt: 5 }}>
              Añadir Audio
              <input type="file" hidden accept=".mp3" />
            </Button>
          </Grid>
        </AccordionDetails>
      </Accordion>
    </Grid>
  );
};

export { StopComponent };
