import React, { useState } from "react";
import { loadScript } from "load-script";
import {
  TextField,
  Button,
  IconButton,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Card,
} from "@material-ui/core";
import PersistentDrawerLeft from "../Sidebar";
import { CardContent, Typography } from "@mui/material";
import ArrowForwardIosSharpIcon from "@mui/icons-material/ArrowForwardIosSharp";
import MuiAccordionDetails from "@mui/material/AccordionDetails";
import { styled } from "@mui/material/styles";
import MuiAccordion from "@mui/material/Accordion";
import MuiAccordionSummary from "@mui/material/AccordionSummary";
import {StopComponent} from "./StopComponent";


import AddCircleIcon from '@mui/icons-material/AddCircle';

const AddRoute = () => {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [countParada, setCountParada] = useState(1);
  

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


  return (
    <>
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
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    label="Nombre"
                    fullWidth
                    required
                  />
                </Grid>

                <Grid item xs={12}>
                  <TextField
                    helperText="Descripción de la ruta"
                    id="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    label="Descripción"
                    multiline
                    fullWidth
                    required
                  />
                </Grid>

                {Array.from({ length: countParada }).map((_, index) => (
                <StopComponent key = {index} countParada={index+1} />
                ))}
                <Grid item xs={12} align="center">
                  <Button onClick= {() => setCountParada(countParada + 1)}>
                  <AddCircleIcon fontSize="large"/>
                  </Button>

                </Grid>

                <Grid item xs={12} align="center">
                  <Button
                    variant="contained"
                    color="primary">
                    {" "}
                    Crear Ruta{" "}
                  </Button>
                </Grid>
              </Grid>
            </form>
          </CardContent>
        </Card>
      </PersistentDrawerLeft>
    </>
  );
};

export default AddRoute;
