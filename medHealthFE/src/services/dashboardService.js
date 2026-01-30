import apiClient from './apiClient';

const dashboardService = {
  getStats: async () => {
    try {
      const response = await apiClient.get('/dashboard/stats');
      return response.data;
    } catch (error) {
      console.error('Error fetching dashboard stats', error);
      throw error;
    }
  }
};

export default dashboardService;