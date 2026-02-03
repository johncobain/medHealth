import { extractErrorMessage } from "../utils/errorHandler";

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
    throw new Error(extractErrorMessage(error));
  }
};

const getById = async (id) => {
  try {
    const response = await apiClient.get(`/patients/${id}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getByEmail = async (email) => {
  try {
    const response = await apiClient.get(`/patients/email/${encodeURIComponent(email)}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getByCpf = async (cpf) => {
  try {
    const response = await apiClient.get(`/patients/cpf/${encodeURIComponent(cpf)}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

/**
 * Payload: PatientFormDto (fullName, email, phone, cpf, address)
 */
const create = async (patientData) => {
  try {
    const response = await apiClient.post('/patients', patientData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

/**
 * PatientUpdateDto (fullName?, phone?, address?)
 */
const update = async (id, patientData) => {
  try {
    const response = await apiClient.put(`/patients/${id}`, patientData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const remove = async (id) => {
  try {
    await apiClient.delete(`/patients/${id}`);
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getMyData = async () => {
  try {
    const response = await apiClient.get('/patients/me');
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const updateMyData = async (patientData) => {
  try {
    const response = await apiClient.put('/patients/me', patientData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const patientService = {
  getAll,
  getById,
  getByEmail,
  getByCpf,
  create,
  update,
  delete: remove,
  getMyData,
  updateMyData,
};

export default patientService;