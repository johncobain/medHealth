import apiClient from "./apiClient";

const getAll = async (params = {}) => {
  try {
    // params: page, size, sort
    const response = await apiClient.get('/patients', { params });
    return {
      content: response.data.content || [],
      total: response.data.totalElements || 0,
      totalPages: response.data.totalPages || 0
    };
  } catch (error) {
    console.error("Erro ao buscar pacientes", error);
    throw error;
  }
};

const getById = async (id) => {
  const response = await apiClient.get(`/patients/${id}`);
  return response.data;
};

const getByEmail = async (email) => {
  const response = await apiClient.get(`/patients/email/${encodeURIComponent(email)}`);
  return response.data;
};

const getByCpf = async (cpf) => {
  const response = await apiClient.get(`/patients/cpf/${encodeURIComponent(cpf)}`);
  return response.data;
};

/**
 * Payload: PatientFormDto (fullName, email, phone, cpf, address)
 */
const create = async (patientData) => {
  const response = await apiClient.post('/patients', patientData);
  return response.data;
};

/**
 * PatientUpdateDto (fullName?, phone?, address?)
 */
const update = async (id, patientData) => {
  const response = await apiClient.put(`/patients/${id}`, patientData);
  return response.data;
};

const remove = async (id) => {
  await apiClient.delete(`/patients/${id}`);
};

const activate = async (id) => {
  await apiClient.patch(`/patients/${id}/activate`);
};

const patientService = {
  getAll,
  getById,
  getByEmail,
  getByCpf,
  create,
  update,
  delete: remove,
  activate,
};

export default patientService;