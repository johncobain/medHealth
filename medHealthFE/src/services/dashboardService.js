import { extractErrorMessage } from '../utils/errorHandler';

import apiClient from './apiClient';

const dashboardService = {
  getStats: async () => {
    try {
      const response = await apiClient.get('/dashboard/stats');
      const data = response.data;

      return {
        doctors: { total: data.totalDoctors || 0 },
        patients: { total: data.totalPatients || 0 },
        appointments: { 
          total: data.totalAppointments || 0, 
          pending: data.pendingAppointments || 0, 
          today: data.todayAppointments || 0,
        },
      };
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  }
};

export default dashboardService;