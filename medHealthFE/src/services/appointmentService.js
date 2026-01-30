import apiClient from './apiClient';

const appointmentService = {
  getRecent: async () => {
      const response = await apiClient.get('/appointments/recent'); 
      return response.data;
  },

  complete: async (id) => {
      const response = await apiClient.patch(`/appointments/${id}/complete`);
      return response.data;
  },

  cancel: async (id) => {
      const response = await apiClient.patch(`/appointments/${id}/cancel`);
      return response.data;
  }
};

export default appointmentService;