import apiClient from './apiClient';

const getStatus = async () => {
  const response = await apiClient.get('/');
  return response.data;
};
const statusService = {
  getStatus,
};

export default statusService;
