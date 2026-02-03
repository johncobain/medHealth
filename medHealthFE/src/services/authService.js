import { extractErrorMessage } from '../utils/errorHandler';

import apiClient from './apiClient';

const login = async (email, password) => {
  try {
    const response = await apiClient.post('/auth/login', { email, password });
    if (response.data.token) {
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const logout = () => {
  localStorage.removeItem('user');
};

// PatientFormDto (fullName, email, phone, cpf, address: { ... })
const register = async (patientData) => {
  try {
    const response = await apiClient.post('/auth/register', patientData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

// DoctorRequestFormDto (fullName, email, phone, cpf, crm, specialty, state, city, neighborhood, street, number, complement, zipCode.)
const requestRegister = async (doctorData) => {
  try {
    const response = await apiClient.post('/auth/request-register', doctorData);
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};

const getProfile = async () => {
  try {
    const response = await apiClient.get('/auth/profile');
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const forgotPassword = async (email) => {
  try {
    const response = await apiClient.post('/auth/forgot-password', { email });
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const resetPassword = async (token, newPassword) => {
  try {
    const response = await apiClient.post('/auth/reset-password', { token, newPassword });
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const changePassword = async (oldPassword, newPassword) => {
  try {
    const response = await apiClient.post('/auth/change-password', { oldPassword, newPassword });
    return response.data;
  } catch (error) {
    throw new Error(extractErrorMessage(error));
  }
};

const authService = {
  login,
  logout,
  register,
  requestRegister,
  getCurrentUser,
  getProfile,
  forgotPassword,
  resetPassword,
  changePassword,
};

export default authService;
