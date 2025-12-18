import axiosClient from "./api.service";
import { useMutation, useQuery } from "@tanstack/react-query";



const PATH="/rest-restaurant-api/api/mesas/"

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


 function getAllMesa () {
  return axiosClient.get(
    `${PATH}gestion`,
  );
}


export function useGetMesa() {
  return useQuery({
    queryKey: ["getAllMesa"], 
    queryFn: getAllMesa,
  });
}



export const updateMesa = async (id: number, data: any) => {
  return axiosClient.put(`${PATH}/admin/${id}`, data);
};


export const useUpdateMesa = () => {
  return useMutation(updateMesa);
};