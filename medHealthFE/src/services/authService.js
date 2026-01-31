import apiClient from './apiClient';

const login = async (email, password) => {
  const response = await apiClient.post('/auth/login', { email, password });
  if (response.data.token) {
    localStorage.setItem('user', JSON.stringify(response.data));
  }
  return response.data;
};

const logout = () => {
  localStorage.removeItem('user');
};

// PatientFormDto (fullName, email, phone, cpf, address: { ... })
const register = async (patientData) => {
  const response = await apiClient.post('/auth/register', patientData);
  return response.data;
};

// DoctorRequestFormDto (fullName, email, phone, cpf, crm, specialty, state, city, neighborhood, street, number, complement, zipCode.)
const requestRegister = async (doctorData) => {
  const response = await apiClient.post('/auth/request-register', doctorData);
  return response.data;
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};

const getProfile = async () => {
  const response = await apiClient.get('/auth/profile');
  return response.data;
};

const forgotPassword = async (email) => {
  const response = await apiClient.post('/auth/forgot-password', { email });
  return response.data;
};

const resetPassword = async (token, newPassword) => {
  const response = await apiClient.post('/auth/reset-password', { token, newPassword });
  return response.data;
};

const changePassword = async (oldPassword, newPassword) => {
  const response = await apiClient.post('/auth/change-password', { oldPassword, newPassword });
  return response.data;
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
