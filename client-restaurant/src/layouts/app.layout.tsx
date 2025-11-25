import { Box, Grid } from "@mui/material";
import { Outlet } from "react-router";
import TemporaryDrawer from "../components/Drawer";
import { useState } from "react";

export default function AuthLayout() {
      const [open, setOpen] = useState(false);
    
  return (
    <Grid container>
      <Grid size={2}>
        <TemporaryDrawer open={open} setOpen={setOpen}/>
          </Grid>
      <Grid size={10}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            height: "90vh",
            gap: 2,
          }}
        >
          <Outlet />
        </Box>
      </Grid>
    </Grid>
  );
}