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
      console.error("Erro ao buscar consultas", error);
      throw error;
    }
  },
  
  getRecent: async () => {
    try {
      const response = await apiClient.get('/appointments/recent');
      return response.data.content || [];
    } catch (error) {
      console.error("Erro ao buscar consultas recentes", error);
      throw error;
    }
  },

  getById: async (id) => {
    const response = await apiClient.get(`/appointments/${id}`);
    return response.data;
  },

  create: async (appointmentData) => {
    const response = await apiClient.post('/appointments', appointmentData);
    return response.data;
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