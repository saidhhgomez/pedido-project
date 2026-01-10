import { Box, Typography } from "@mui/material";
import CardPlate from "../../components/RendeProducto";
import { useGetPlate } from "../../services/plate.service";
import type { Plate } from "../../types/Plate.type";


export default function Home() {
  const { data } = useGetPlate();

  return (
    <>

    <Box sx={{    m: 1,       // margin en todos los lados
}}>
<Typography
  sx={{
    fontSize: "2rem", // tamaño más grande, ajusta a tu gusto
  }}
>
Platos 
</Typography>

</Box>
      <Box  className="grid grid-cols-3 gap-x-8 gap-y-4">
                {
          data?.data?.map((el:Plate)=>(
            <CardPlate Plate={el}/>
          )
          )

        }
        </Box>
    </>
  );
}