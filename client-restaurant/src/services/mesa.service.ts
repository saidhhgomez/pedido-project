import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";



const PATH="/rest-restaurant-api/api/mesas"

 function createMesa(data:any ) {
  return axiosClient.post(
    `${PATH}`,
data
  );
}

export function useCreateMesa(){
  return useMutation(
    {
      mutationFn:createMesa,
      mutationKey:["createMesa"],
    }
  )
}


function deleteMesa(id:number){
return axiosClient.delete(`${PATH}/${id}`);
}

 function getAllMesaAdmin (idMesa: number) {
  return axiosClient.get(
    `${PATH}/admin/${idMesa}`,
  );
}



 function getAllMesaMesero (idSucursal: any) {
  return axiosClient.get(
    `${PATH}/mesero/${idSucursal}`,
  );
}



export function useGetMesaMesero(idSucursal: any) {
  return useQuery({
    queryKey: ["mesas-mesero", idSucursal],
    queryFn: () => getAllMesaMesero(idSucursal).then(res => res.data),
    refetchInterval: 5000, // se actualiza solo cada 5s
  });
}






export function useDeleteMesa(){
  return useMutation(
  {
    mutationFn: deleteMesa,
    mutationKey:["deleteMesa"]
  }
  );
}




export function useGetMesaById(idMesa?: number) {
  return useQuery({
    queryKey: ["getMesaById", idMesa],
    queryFn: () => getAllMesaAdmin(idMesa!),
    enabled: !!idMesa,
  });
}




function updateMesa({
  id,
  data,
}: {
  id: number;
  data: any;
}) {
  return axiosClient.put(`${PATH}/${id}`, data);
}



export const useUpdateMesa = () => {
  return useMutation({
    mutationFn: updateMesa,
  });
};