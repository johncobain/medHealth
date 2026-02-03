import { extractErrorMessage } from '../utils/errorHandler';

import apiClient from './apiClient';

const getStatus = async () => {
  try {
    const response = await apiClient.get('/');
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};
const statusService = {
  getStatus,
};

export default statusService;
