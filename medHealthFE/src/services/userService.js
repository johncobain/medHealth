import { extractErrorMessage } from '../utils/errorHandler';

import apiClient from './apiClient';

const getMe = async () => {
  try {
    const response = await apiClient.get('/users/me');
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};
const userServices = {
  getMe,
};

export default userServices;