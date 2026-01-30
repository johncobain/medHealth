import apiClient from './apiClient';

const appointmentService = {
  getRecent: async () => {
    try {
      const response = await apiClient.get('/appointments/recent');
      if (response.data && response.data.content) {
        return response.data.content;
      }
      return [];
    } catch (error) {
      console.error("Erro ao buscar consultas recentes", error);
      throw error;
    }
  },

  complete: async (id) => {
    const response = await apiClient.patch(`/appointments/${id}/complete`);
    return response.data;
  },

  cancel: async (id, reason, reasonMessage) => {
    const payload = {
      reason: reason || 'OTHER',
      message: reasonMessage || 'Cancelado pelo usuário via Dashboard'
    };
    const response = await apiClient.patch(`/appointments/${id}/cancel`, payload);
    return response.data;
  }
};

export default appointmentService;