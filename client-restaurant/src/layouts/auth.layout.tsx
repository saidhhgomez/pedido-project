import { Box, Grid } from "@mui/material";
import { Outlet } from "react-router";
import logo from "../assets/react.svg"

export default function AuthLayout() {
  return (
    <Grid container>
      <Grid size={6}>
        <Box
          sx={{
            backgroundImage: `url(${logo})`,
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover",
            height: "100vh",
          }}
        />
      </Grid>
      <Grid size={6}>
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