import { extractErrorMessage } from '../utils/errorHandler';

import apiClient from './apiClient';

const appointmentService = {
  getAll: async (params = {}) => {
    try {
      // params: page, size, sort, doctorId, patientId, status, startDate, endDate
      const response = await apiClient.get('/appointments', { params });
      return {
        content: response.data.content || [],
        total: response.data.totalElements || 0,
        totalPages: response.data.totalPages || 0
      };
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },
  
  getRecent: async () => {
    try {
      const response = await apiClient.get('/appointments/recent');
      return response.data.content || [];
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },

  getById: async (id) => {
    try {
      const response = await apiClient.get(`/appointments/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },

  create: async (appointmentData) => {
    try {
      const response = await apiClient.post('/appointments', appointmentData);
      return response.data;
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },

  complete: async (id) => {
    try {
      const response = await apiClient.patch(`/appointments/${id}/complete`);
      return response.data;
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },

  cancel: async (id, reason, reasonMessage) => {
    try {
      const payload = {
        reason: reason || 'OTHER',
        message: reasonMessage || 'Cancelado pelo usuário via Dashboard'
      };
      const response = await apiClient.patch(`/appointments/${id}/cancel`, payload);
      return response.data;
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },

  getCancellationReasons: async () => {
    try {
      const response = await apiClient.get('/appointments/getCancellationReasons');
      return response.data || [];
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  }
};

export default appointmentService;