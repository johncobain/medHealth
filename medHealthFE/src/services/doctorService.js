import { extractErrorMessage } from "../utils/errorHandler";

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
    throw new Error(extractErrorMessage(error));
  }
};

const getById = async (id) => {
  try {
    const response = await apiClient.get(`/doctors/${id}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getByEmail = async (email) => {
  try {
    const response = await apiClient.get(`/doctors/email/${encodeURIComponent(email)}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getByCpf = async (cpf) => {
  try {
    const response = await apiClient.get(`/doctors/cpf/${encodeURIComponent(cpf)}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getByCrm = async (crm) => {
  try {
    const response = await apiClient.get(`/doctors/crm/${encodeURIComponent(crm)}`);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getBySpecialty = async (specialty, params = {}) => {
  try {
    const response = await apiClient.get(`/doctors/specialty/${specialty}`, { params });
    return {
      content: response.data.content || [],
      total: response.data.totalElements || 0,
      totalPages: response.data.totalPages || 0
    };
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

/**
 * DoctorFormDto (fullName, email, phone, cpf, crm, address, specialty)
 */
const create = async (doctorData) => {
  try {
    const response = await apiClient.post('/doctors', doctorData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

/**
 * DoctorUpdateDto (fullName?, phone?, address?)
 */
const update = async (id, doctorData) => {
  try {
    const response = await apiClient.put(`/doctors/${id}`, doctorData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const remove = async (id) => {
  try {
    await apiClient.delete(`/doctors/${id}`);
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getMyData = async () => {
  try {
    const response = await apiClient.get('/doctors/me');
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const updateMyData = async (doctorData) => {
  try {
    const response = await apiClient.put('/doctors/me', doctorData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getSpecialties = async () => {
  try {
    const response = await apiClient.get('/doctors/getSpecialties');
    return response.data || [];
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
}

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
  getSpecialties
};

export default doctorService;