/**
 * Formata telefone automaticamente
 * @param {string} value - Valor a ser formatado
 * @returns {string} Telefone formatado (99) 99999-9999
 */
export const formatPhone = (value) => {
  const numbers = value.replace(/\D/g, '');
  if (numbers.length <= 2) return numbers;
  if (numbers.length <= 6) return `(${numbers.slice(0, 2)}) ${numbers.slice(2)}`;
  if (numbers.length <= 10) {
    return `(${numbers.slice(0, 2)}) ${numbers.slice(2, 6)}-${numbers.slice(6)}`;
  }
  return `(${numbers.slice(0, 2)}) ${numbers.slice(2, 7)}-${numbers.slice(7, 11)}`;
};

/**
 * Formata CEP automaticamente
 * @param {string} value - Valor a ser formatado
 * @returns {string} CEP formatado 99999-999
 */
export const formatZipCode = (value) => {
  const numbers = value.replace(/\D/g, '');
  if (numbers.length <= 5) return numbers;
  return `${numbers.slice(0, 5)}-${numbers.slice(5, 8)}`;
};

/**
 * Formata CPF automaticamente
 * @param {string} value - Valor a ser formatado
 * @returns {string} CPF formatado 999.999.999-99
 */
export const formatCPF = (value) => {
  const numbers = value.replace(/\D/g, '');
  if (numbers.length <= 3) return numbers;
  if (numbers.length <= 6) return `${numbers.slice(0, 3)}.${numbers.slice(3)}`;
  if (numbers.length <= 9) {
    return `${numbers.slice(0, 3)}.${numbers.slice(3, 6)}.${numbers.slice(6)}`;
  }
  return `${numbers.slice(0, 3)}.${numbers.slice(3, 6)}.${numbers.slice(6, 9)}-${numbers.slice(9, 11)}`;
};

/**
 * Formata CRM automaticamente
 * @param {string} value - Valor a ser formatado
 * @returns {string} CRM formatado CRM-UF-NUMERO
 */
export const formatCRM = (value) => {
  let cleaned = value.replace(/[^A-Z0-9]/gi, '').toUpperCase();
  
  if (!cleaned.startsWith('CRM')) {
    cleaned = 'CRM' + cleaned;
  }
  
  const withoutCRM = cleaned.substring(3);
  
  if (withoutCRM.length === 0) return 'CRM-';
  if (withoutCRM.length <= 2) return `CRM-${withoutCRM}`;
  
  const uf = withoutCRM.slice(0, 2);
  const numero = withoutCRM.slice(2, 8); 
  
  if (numero.length === 0) return `CRM-${uf}`;
  return `CRM-${uf}-${numero}`;
};