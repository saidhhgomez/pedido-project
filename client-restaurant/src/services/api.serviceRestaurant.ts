import axios from "axios";

const API_URL = "http://localhost:8900/";

const axiosClient = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});


export default axiosClient;