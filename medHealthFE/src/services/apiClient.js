import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 20000,
});

apiClient.interceptors.request.use(
  (config) => config,
  (error) => Promise.reject(error),
);

export default apiClient;
