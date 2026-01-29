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

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'));
};

const forgotPassword = async (email) => {
  const response = await apiClient.post('/auth/forgot-password', { email });
  return response.data;
}

const resetPassword = async (token, newPassword) => {
  const response = await apiClient.post('/auth/reset-password', { token, newPassword });
  return response.data;
}

const changePassword = async (currentPassword, newPassword) => {
  const response = await apiClient.post('/auth/change-password', { currentPassword, newPassword });
  return response.data;
}

const authService = {
  login,
  logout,
  getCurrentUser,
  forgotPassword,
  resetPassword,
  changePassword,
};

export default authService;