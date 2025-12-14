import axios from "axios";

const API_URL = "http://localhost:8080/";

const axiosFormData = axios.create({
  baseURL: API_URL,
});

axiosFormData.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default axiosFormData;
