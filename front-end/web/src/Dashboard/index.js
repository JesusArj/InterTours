import { useLocalState } from "../util/useLocalStorage";
import React, { useEffect, useState } from "react";
import Typography from "@mui/material/Typography";
import PersistentDrawerLeft from "../Sidebar/index.js";
import { DrawerHeader } from "../Sidebar/DrawerHeader";
import { width } from "@mui/system";
const Dashboard = () => {
  const [jwt, setJwt] = useLocalState("", "jwt");

  return (
      <PersistentDrawerLeft>
          <div> JWT token: {jwt} </div>
      </PersistentDrawerLeft>
  );
};

export default Dashboard;
