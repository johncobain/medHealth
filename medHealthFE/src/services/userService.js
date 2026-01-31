import apiClient from './apiClient';

const getMe = async () => {
  const response = await apiClient.get('/users/me');
  return response.data;
};
const userServices = {
  getMe,
};

export default userServices;