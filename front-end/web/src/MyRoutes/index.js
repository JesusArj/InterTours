import { useLocalState } from "../util/useLocalStorage";
import Typography from "@mui/material/Typography";
import PersistentDrawerLeft from "../Sidebar/index.js";
import Alert from "@mui/material/Alert";
import { useLocation } from "react-router-dom";
import jwt_decode from "jwt-decode";
import { useEffect } from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import { useState } from "react";
import {
  TableContainer,
  TableHead,
  TableCell,
  TableRow,
  Table,
  TableBody,
  Grid,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import { tableCellClasses } from "@mui/material/TableCell";
import TablePagination from "@mui/material/TablePagination";
import { useMemo } from "react";
import VisibilityIcon from "@mui/icons-material/Visibility";
import IconButton from "@mui/material/IconButton";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import * as React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

const MyRoutes = () => {
  const [showDetail, setShowDetail] = useState(false);
  const location = useLocation();
  const [jwt, setJwt] = useLocalState("", "jwt");
  const [data, setData] = useState([{}]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [modalIndex, setModalIndex] = useState(0);

  const handleClickOpenDetailModal = (index) => {
    console.log(data);
    console.log(index);
    console.log(data[index]);
    setModalIndex(index);
    setShowDetail(true);
  };

  const handleCloseDetailModal = () => {
    setShowDetail(false);
  };

  useEffect(() => {
    fetch(`api/rutas/${jwt_decode(jwt).sub}`)
      .then((response) => response.json())
      .then((data) => setData(data));
  }, [jwt]);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
      backgroundColor: "#3276D2",
      color: theme.palette.common.black,
    },
    [`&.${tableCellClasses.body}`]: {
      fontSize: 14,
    },
  }));

  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    "&:nth-of-type(odd)": {
      backgroundColor: "#DFEBFF",
    },
    "&:nth-of-type(even)": {
      backgroundColor: "#F3F3F3",
    },
    "&:last-child td, &:last-child th": {
      border: 0,
    },
  }));

  const visibleRows = useMemo(
    () => data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage),
    [data, page, rowsPerPage]
  );

  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - data.length) : 0;

  return (
    <>
      {location.state && location.state.shouldShowAlert && (
        <Alert severity="success">Ruta creada con éxito</Alert>
      )}
      <h1 style={{ textAlign: "center", color: "#3276D2" }}>MIS RUTAS</h1>
      <Card style={{ maxWidth: 800, margin: "0 auto" }}>
        <CardContent>
          <TableContainer>
            <Table>
              <TableHead>
                <StyledTableRow>
                  <StyledTableCell width="35%">Título</StyledTableCell>
                  <StyledTableCell width="23%">Municipio</StyledTableCell>
                  <StyledTableCell width="23%">Provincia</StyledTableCell>
                  <StyledTableCell width="19%"></StyledTableCell>
                </StyledTableRow>
              </TableHead>
              <TableBody>
                {visibleRows.map((data, index) => (
                  <StyledTableRow key={index}>
                    <TableCell>{data.titulo}</TableCell>
                    <TableCell>{data.municipio}</TableCell>
                    <TableCell>{data.provincia}</TableCell>
                    <TableCell>
                      <IconButton
                        onClick={() => handleClickOpenDetailModal(index)}>
                        <VisibilityIcon fontSize="small" color="action" />
                      </IconButton>
                      <IconButton>
                        <EditIcon fontSize="small" color="action" />
                      </IconButton>
                      <IconButton>
                        <DeleteIcon fontSize="small" color="action" />
                      </IconButton>
                    </TableCell>
                  </StyledTableRow>
                ))}
                {emptyRows > 0 && (
                  <TableRow
                    style={{
                      height: 53 * emptyRows,
                    }}>
                    <TableCell colSpan={6} />
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </CardContent>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={data.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          labelRowsPerPage="Elementos por página"
        />
      </Card>

      <Dialog
        onClose={handleCloseDetailModal}
        open={showDetail}
        maxWidth="xl"
        fullWidth>
        <DialogTitle>Detalle</DialogTitle>
        <br />
        <DialogContent>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={6}>
              <TextField
                id="name"
                value={data[modalIndex].titulo}
                label="Nombre"
                disabled
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                disabled
                id="descripcion"
                value={data[modalIndex].descripcion}
                label="Descripción"
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                disabled
                id="municipio"
                value={data[modalIndex].municipio}
                label="Municipio"
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                disabled
                id="provincia"
                value={data[modalIndex].provincia}
                label="provincia"
                fullWidth
              />
            </Grid>
            {data[modalIndex].coordenadas &&
              data[modalIndex].coordenadas.map((element, index) => (
                <Grid item xs={12} key={index}>
                  <Accordion>
                    <AccordionSummary
                      expandIcon={<ExpandMoreIcon />}
                      aria-controls={"panel" + index + "d-content"}
                      id={"panel" + index + "d-header"}>
                      <Typography>{index + 1 + "- \t" + element.nombreParada}  </Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                      <Grid container spacing={2}>
                        <Grid item xs={12}>
                          <TextField
                            id={"nombreParada" + index}
                            label="Nombre"
                            fullWidth
                            value={element.nombreParada}
                            disabled
                          />
                        </Grid>
                        <Grid item xs={12}>
                          <TextField
                            id={"descripcionParada" + index}
                            label="Descripción"
                            value={element.descripcionParada}
                            multiline
                            minRows={2}
                            fullWidth
                            disabled
                          />
                        </Grid>
                      </Grid>
                    </AccordionDetails>
                  </Accordion>
                </Grid>
              ))}
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDetailModal}>Cerrar</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default MyRoutes;
