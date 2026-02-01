import apiClient from "./apiClient";

const getAll = async (params = {}) => {
  try {
    // params: page, size, sort
    const response = await apiClient.get('/doctors', { params });
    return {
      content: response.data.content || [],
      total: response.data.totalElements || 0,
      totalPages: response.data.totalPages || 0
    };
  } catch (error) {
    console.error("Erro ao buscar médicos", error);
    throw error;
  }
};

const getById = async (id) => {
  const response = await apiClient.get(`/doctors/${id}`);
  return response.data;
};

const getByEmail = async (email) => {
  const response = await apiClient.get(`/doctors/email/${encodeURIComponent(email)}`);
  return response.data;
};

const getByCpf = async (cpf) => {
  const response = await apiClient.get(`/doctors/cpf/${encodeURIComponent(cpf)}`);
  return response.data;
};

const getByCrm = async (crm) => {
  const response = await apiClient.get(`/doctors/crm/${encodeURIComponent(crm)}`);
  return response.data;
};

const getBySpecialty = async (specialty, params = {}) => {
  const response = await apiClient.get(`/doctors/specialty/${specialty}`, { params });
  return {
    content: response.data.content || [],
    total: response.data.totalElements || 0,
    totalPages: response.data.totalPages || 0
  };
};

/**
 * DoctorFormDto (fullName, email, phone, cpf, crm, address, specialty)
 */
const create = async (doctorData) => {
  const response = await apiClient.post('/doctors', doctorData);
  return response.data;
};

/**
 * DoctorUpdateDto (fullName?, phone?, address?)
 */
const update = async (id, doctorData) => {
  const response = await apiClient.put(`/doctors/${id}`, doctorData);
  return response.data;
};

const remove = async (id) => {
  await apiClient.delete(`/doctors/${id}`);
};

const getMyData = async () => {
  const response = await apiClient.get('/doctors/me');
  return response.data;
};

const updateMyData = async (doctorData) => {
  const response = await apiClient.put('/doctors/me', doctorData);
  return response.data;
};

const doctorService = {
  getAll,
  getById,
  getByEmail,
  getByCpf,
  getByCrm,
  getBySpecialty,
  create,
  update,
  delete: remove,
  getMyData,
  updateMyData,
};

export default doctorService;