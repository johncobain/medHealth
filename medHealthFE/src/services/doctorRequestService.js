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
    console.error('Erro ao buscar solicitações de médicos', error);
    throw error;
  }
};

const accept = async (id) => {
  const response = await apiClient.post(`/doctors-request/accept/${id}`);
  return response.data;
};

const decline = async (id) => {
  const response = await apiClient.post(`/doctors-request/decline/${id}`);
  return response.data;
};

const doctorRequestService = {
  getAll,
  accept,
  decline,
};

export default doctorRequestService;
