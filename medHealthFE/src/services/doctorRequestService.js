import { extractErrorMessage } from '../utils/errorHandler';

import apiClient from './apiClient';

const getAll = async (params = {}) => {
  try {
    // params: page, size, sort
    const response = await apiClient.get('/doctors-request', { params });
    return {
      content: response.data.content || [],
      total: response.data.totalElements || 0,
      totalPages: response.data.totalPages || 0,
    };
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const accept = async (id) => {
  try {
    const response = await apiClient.post(`/doctors-request/accept/${id}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const decline = async (id) => {
  try {
    const response = await apiClient.post(`/doctors-request/decline/${id}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const doctorRequestService = {
  getAll,
  accept,
  decline,
};

export default doctorRequestService;
