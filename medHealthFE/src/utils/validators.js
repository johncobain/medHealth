export const PHONE_MASK = /^\(\d{2}\) \d{4,5}-\d{4}$/;
export const CPF_MASK = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;
export const ZIP_MASK = /^\d{5}-\d{3}$/;
export const EMAIL_MASK = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

/**
 * Valida telefone
 * @param {string} phone - Telefone a ser validado
 * @returns {boolean}
 */
export const validatePhone = (phone) => {
  if (!phone) return true; // opcional
  return PHONE_MASK.test(phone);
};

/**
 * Valida CPF
 * @param {string} cpf - CPF a ser validado
 * @returns {boolean}
 */
export const validateCPF = (cpf) => {
  if (!cpf) return false;
  return CPF_MASK.test(cpf);
};

/**
 * Valida CEP
 * @param {string} zipCode - CEP a ser validado
 * @returns {boolean}
 */
export const validateZipCode = (zipCode) => {
  if (!zipCode) return true; // opcional
  return ZIP_MASK.test(zipCode);
};

/**
 * Valida email
 * @param {string} email - Email a ser validado
 * @returns {boolean}
 */
export const validateEmail = (email) => {
  if (!email) return false;
  return EMAIL_MASK.test(email);
};

/**
 * Valida endereço completo
 * @param {Object} address - Objeto de endereço
 * @returns {{valid: boolean, message?: string}}
 */
export const validateAddress = (address) => {
  const { state, city, neighborhood, street, zipCode } = address;
  const hasAnyField = [state, city, neighborhood, street, zipCode].some((v) => v?.trim());
  
  if (!hasAnyField) {
    return { valid: true }; // endereço é opcional
  }

  if (!state?.trim() || !city?.trim() || !neighborhood?.trim() || !street?.trim()) {
    return { valid: false, message: 'Preencha estado, cidade, bairro e rua.' };
  }

  if (!validateZipCode(zipCode)) {
    return { valid: false, message: 'CEP no formato 99999-999.' };
  }

  return { valid: true };
};