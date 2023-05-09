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
  const [showDelete, setShowDelete] = useState(false);
  const [showEdit, setShowEdit] = useState(false);
  const location = useLocation();
  const [jwt, setJwt] = useLocalState("", "jwt");
  const [data, setData] = useState([{}]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [modalIndex, setModalIndex] = useState(0);
  const [editModalInfo, setEditModalInfo] = useState({
    titulo: "",
    descripcion: "",
  });
  const [editStops, setEditStops] = useState([{}]);
  const [alerts, setAlerts] = useState({
    showAddRouteSuccess: true,
    showDeleteRouteSuccess: false,
    showEditModalInfoAlert: true,
    showEditRouteSuccess: false,
  });

  const handleClickOpenDetailModal = (index) => {
    console.log(data);
    console.log(index);
    console.log(data[index]);
    setModalIndex(index);
    setShowDetail(true);
  };

  const handleClickOpenDeleteModal = (index) => {
    setModalIndex(index);
    setShowDelete(true);
  };

  const handleClickOpenEditModal = (index) => {
    setAlerts((prev) => ({ ...prev, showEditModalInfoAlert: true }));
    setModalIndex(index);
    editModalInfo.titulo = visibleRows[index] ? visibleRows[index].titulo : "";
    editModalInfo.descripcion = visibleRows[index]
      ? visibleRows[index].descripcion
      : "";
    setEditStops(
      visibleRows[index] && visibleRows[index].coordenadas
        ? visibleRows[index].coordenadas
        : ""
    );
    setShowEdit(true);
  };

  const handleCloseDetailModal = () => {
    setShowDetail(false);
  };

  const handleCloseDeleteModal = () => {
    setShowDelete(false);
  };

  const handleEditRouteChange = (e, fieldName) => {
    setEditModalInfo((prev) => ({ ...prev, [fieldName]: e.target.value }));
  };

  const handleCloseEditModal = () => {
    setShowEdit(false);
    console.log(editStops);
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

  const handleAlertClose = (alertName) => {
    setAlerts((prev) => ({ ...prev, [alertName]: false }));
  };

  const setDescriptionInArray = (value, arrayIndex) => {
    const updatedStops = [...editStops]; // Copia el arreglo original
    updatedStops[arrayIndex] = {
      ...updatedStops[arrayIndex], // Copia el objeto original en la posición específica
      descripcionParada: value, // Actualiza la propiedad descripcionParada
    };
    setEditStops(updatedStops); // Actualiza el arreglo completo
  };

  const handleAcceptDeleteModal = () => {
    fetch(`api/rutas/eliminarRuta/${visibleRows[modalIndex].idRuta}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    }).then((response) => {
      if (response.status === 200) {
        setShowDelete(false);
        data.forEach((element, index) => {
          if (element.idRuta === visibleRows[modalIndex].idRuta) {
            data.splice(index);
          }
        });
        visibleRows.splice(modalIndex);
        handleChangePage(null, 0);
        console.log(visibleRows);
        setAlerts((prev) => ({ ...prev, showDeleteRouteSuccess: true }));
        return response.text();
      }
    });
  };

  const handleAcceptEditModal = () => {
    const ReqData = {
      titulo: editModalInfo.titulo,
      descripcion: editModalInfo.descripcion,
      coordenadas: editStops,
    };

    fetch(`api/rutas/editarRuta/${visibleRows[modalIndex].idRuta}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(ReqData),
    }).then((response) => {
      if (response.status === 200) {
        setShowEdit(false);
        handleChangePage(null, 0);
        setAlerts((prev) => ({ ...prev, showEditRouteSuccess: true }));
        return response.json();
      }
    }).then((dataResponse) => {
      console.log(dataResponse);
      data.forEach((element, index) => {
        if (element.idRuta === visibleRows[modalIndex].idRuta) {
          data[index] = dataResponse;
        }
      });
          visibleRows[modalIndex] = dataResponse;
    });
  };
  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - data.length) : 0;
  return (
    <>
      {location.state &&
        location.state.shouldShowAlert &&
        alerts.showAddRouteSuccess && (
          <Alert
            severity="success"
            onClose={() => {
              handleAlertClose("showAddRouteSuccess");
            }}>
            Ruta creada con éxito
          </Alert>
        )}
      {alerts.showDeleteRouteSuccess && (
        <Alert
          severity="success"
          onClose={() => {
            handleAlertClose("showDeleteRouteSuccess");
          }}>
          Ruta eliminada correctamente
        </Alert>
      )}
      {alerts.showEditRouteSuccess && (
        <Alert
          severity="success"
          onClose={() => {
            handleAlertClose("showEditRouteSuccess");
          }}>
          Ruta editada correctamente
        </Alert>
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
                      <IconButton
                        onClick={() => handleClickOpenEditModal(index)}>
                        <EditIcon fontSize="small" color="action" />
                      </IconButton>
                      <IconButton
                        onClick={() => handleClickOpenDeleteModal(index)}>
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
        </CardContent>
      </Card>

      {/* MODALES */}
      <Dialog
        onClose={handleCloseDeleteModal}
        open={showDelete}
        maxWidth="md"
        fullWidth>
        <DialogTitle>Eliminar ruta</DialogTitle>
        <br />
        <DialogContent>
          <DialogContentText>
            ¿Está seguro de que desea eliminar la ruta?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleAcceptDeleteModal} color="primary" autoFocus>
            Eliminar
          </Button>
          <Button onClick={handleCloseDeleteModal} color="primary">
            Cancelar
          </Button>
        </DialogActions>
      </Dialog>

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
                value={
                  visibleRows[modalIndex] ? visibleRows[modalIndex].titulo : ""
                }
                label="Nombre"
                disabled
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                disabled
                id="descripcion"
                value={
                  visibleRows[modalIndex]
                    ? visibleRows[modalIndex].descripcion
                    : ""
                }
                label="Descripción"
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                disabled
                id="municipio"
                value={
                  visibleRows[modalIndex]
                    ? visibleRows[modalIndex].municipio
                    : ""
                }
                label="Municipio"
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                disabled
                id="provincia"
                value={
                  visibleRows[modalIndex]
                    ? visibleRows[modalIndex].provincia
                    : ""
                }
                label="provincia"
                fullWidth
              />
            </Grid>
            {visibleRows[modalIndex] &&
              visibleRows[modalIndex].coordenadas &&
              visibleRows[modalIndex].coordenadas.map((element, index) => (
                <Grid item xs={12} key={index}>
                  <Accordion>
                    <AccordionSummary
                      expandIcon={<ExpandMoreIcon />}
                      aria-controls={"panel" + index + "d-content"}
                      id={"panel" + index + "d-header"}>
                      <Typography>
                        {index + 1 + "- \t" + element.nombreParada}{" "}
                      </Typography>
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

      <Dialog
        onClose={handleCloseEditModal}
        open={showEdit}
        maxWidth="xl"
        fullWidth>
        <DialogTitle>Editar ruta</DialogTitle>
        {alerts.showEditModalInfoAlert && (
          <Alert
            severity="info"
            onClose={() => {
              handleAlertClose("showEditModalInfoAlert");
            }}>
            {" "}
            Recuerda que solo algunos campos son editables. Si necesitas nuevas
            paradas, por favor, genera una nueva ruta.{" "}
          </Alert>
        )}
        <br />
        <DialogContent>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={6}>
              <TextField
                id="name"
                value={
                  editModalInfo.titulo
                }
                label="Nombre"
                onChange={(e) => {
                  handleEditRouteChange(e, "titulo");
                }}
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                id="descripcion"
                value={editModalInfo.descripcion}
                onChange={(e) => {
                  handleEditRouteChange(e, "descripcion");
                }}
                label="Descripción"
                fullWidth
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                id="municipio"
                value={
                  visibleRows[modalIndex]
                    ? visibleRows[modalIndex].municipio
                    : ""
                }
                label="Municipio"
                fullWidth
                disabled
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                id="provincia"
                value={
                  visibleRows[modalIndex]
                    ? visibleRows[modalIndex].provincia
                    : ""
                }
                label="provincia"
                fullWidth
                disabled
              />
            </Grid>
            {visibleRows[modalIndex] &&
              visibleRows[modalIndex].coordenadas &&
              visibleRows[modalIndex].coordenadas.map((element, index) => (
                <Grid item xs={12} key={index}>
                  <Accordion>
                    <AccordionSummary
                      expandIcon={<ExpandMoreIcon />}
                      aria-controls={"panel" + index + "d-content"}
                      id={"panel" + index + "d-header"}>
                      <Typography>
                        {index + 1 + "- \t" + element.nombreParada}{" "}
                      </Typography>
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
                            value={
                              editStops[index] &&
                              editStops[index].descripcionParada
                                ? editStops[index].descripcionParada
                                : ""
                            }
                            onChange={(e) => {
                              setDescriptionInArray(e.target.value, index);
                            }}
                            multiline
                            minRows={2}
                            fullWidth
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
          <Button onClick={handleAcceptEditModal}>Editar</Button>
          <Button onClick={handleCloseEditModal}>Cerrar</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default MyRoutes;
