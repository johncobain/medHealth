/**
 * Extrai mensagem de erro do response do backend
 * @param {Object} error - Erro do axios
 * @param {string} defaultMessage - Mensagem padrão
 * @returns {string} Mensagem de erro formatada
 */
export const extractErrorMessage = (error, defaultMessage = 'Erro ao processar solicitação.') => {
  if (!error.response) {
    if (error.message === 'Network Error') {
      return 'Erro de conexão com o servidor';
    }
    return error.message || defaultMessage;
  }

  const { reason, errors, message, error: errorField } = error.response.data;

  if (reason) {
    return reason;
  }

  if (errors && typeof errors === 'object') {
    const errorMessages = Object.entries(errors)
      .map(([field, messages]) => {
        const msgs = Array.isArray(messages) ? messages : [messages];
        const formattedMsgs = msgs.map(msg => `${formatFieldName(field)}: ${msg}`)
        return formattedMsgs.join(', ');
      })
      .filter(Boolean)
      .join('\n');

    if (errorMessages) {
      return errorMessages;
    }
  }

  if (errorField) {
    return errorField;
  }

  if (message) {
    return message;
  }

  const status = error.response.status;
  switch (status) {
    case 400:
      return 'Dados inválidos. Verifique os campos e tente novamente.';
    case 401:
      return 'Sessão expirada. Faça login novamente.';
    case 403:
      return 'Você não tem permissão para realizar esta ação.';
    case 404:
      return 'Recurso não encontrado.';
    case 409:
      return 'Conflito: Este registro já existe.';
    case 500:
      return 'Erro interno do servidor. Tente novamente mais tarde.';
    default:
      return defaultMessage;
  }
};

const formatFieldName = (fieldName) => {
  const fieldMap = {
    fullName: 'Nome completo',
    email: 'E-mail',
    cpf: 'CPF',
    phone: 'Telefone',
    password: 'Senha',
    confirmPassword: 'Confirmação de senha',
    crm: 'CRM',
    specialty: 'Especialidade',
    birthDate: 'Data de nascimento',
    gender: 'Gênero',
    bloodType: 'Tipo sanguíneo',
    street: 'Rua',
    number: 'Número',
    neighborhood: 'Bairro',
    city: 'Cidade',
    state: 'Estado',
    zipCode: 'CEP',
    complement: 'Complemento',
  };

  return fieldMap[fieldName] || fieldName;
};

export const extractFieldErrors = (error) => {
  if (!error.response?.data?.errors) {
    return {};
  }

  const { errors } = error.response.data;
  const fieldErrors = {};

  Object.entries(errors).forEach(([field, messages]) => {
    const msgs = Array.isArray(messages) ? messages : [messages];
    fieldErrors[field] = msgs.join(', ');
  });

  return fieldErrors;
};