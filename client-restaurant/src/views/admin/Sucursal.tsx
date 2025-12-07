import { useState } from "react";
import { Button, Box } from "@mui/material";
import FormModalSucursal from "../../components/Modals/FormModalSucursal";
import CardSucursal from "../../components/RenderSucursales";
import { useGetAllSucursales, useCreateSucursal, useUpdateSucursal } from "../../services/sucursales.service";
import type { Sucursal } from "../../types/sucursales.type";
import Swal from "sweetalert2";

export default function RenderSucursal() {
  const { data, refetch } = useGetAllSucursales();
  const { mutate: createSucursal } = useCreateSucursal();
  const { mutate: updateSucursal } = useUpdateSucursal();

  const [open, setOpen] = useState(false);
  const [editingSucursal, setEditingSucursal] = useState<Sucursal | undefined>(undefined);

  const handleSubmit = (sucursal: Sucursal) => {
    if (editingSucursal) {
      // Editar
      updateSucursal({ id: editingSucursal.idSucursal!, data: sucursal }, {
        onSuccess: () => { refetch(); 
          setEditingSucursal(undefined); 
       Swal.fire({
  position: "center",
  icon: "success",
  title: "Sucursal Registrada",
  showConfirmButton: false,
  timer: 1500
}); },
        onError: () => 
          Swal.fire({
  position: "center",
  icon: "error",
  title: "Sucursal No registrada",
  showConfirmButton: false,
  timer: 1500
}),
      });
    } else {
      // Crear
      createSucursal(sucursal, {
        onSuccess: () => { refetch(); alert("Sucursal creada correctamente"); },
        onError: () => alert("Error al crear la sucursal"),
      });
    }
  };

  const handleEdit = (sucursal: Sucursal) => {
    setEditingSucursal(sucursal);
    setOpen(true);
  };

  return (
    <Box>
      <Button variant="contained" onClick={() => { setEditingSucursal(undefined); setOpen(true); }}>
        Nueva Sucursal
      </Button>

      <FormModalSucursal
        open={open}
        onClose={() => setOpen(false)}
        onSubmit={handleSubmit}
        initialData={editingSucursal}
      />

      <Box sx={{ mt: 2, display: "flex", flexWrap: "wrap", gap: 2 }}>
        {data?.data?.map((sucursal: Sucursal) => (
          <CardSucursal key={sucursal.idSucursal} sucursal={sucursal} />
        ))}
      </Box>
    </Box>
  );
}
