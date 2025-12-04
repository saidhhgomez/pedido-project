import CardPlate from "../../components/RendeProducto";
import { useGetPlate } from "../../services/plate.service";
import type { Plate } from "../../types/Plate.type";


export default function Home() {
  const { data } = useGetPlate();

  return (
    <>
      <h1>Hello</h1>

                {
          data?.data?.map((el:Plate)=>(
            <CardPlate Plate={el}/>
          )
          )

        }
    </>
  );
}