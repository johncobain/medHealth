/**
 * Extrai mensagem de erro do response do backend
 * @param {Object} error - Erro do axios
 * @param {string} defaultMessage - Mensagem padrão
 * @returns {string} Mensagem de erro formatada
 */
export const extractErrorMessage = (error, defaultMessage = 'Erro ao processar solicitação.') => {
  if (!error.response?.data) {
    return defaultMessage;
  }

  const { reason, errors, message } = error.response.data;

  if (reason) {
    return reason;
  }

  if (errors && typeof errors === 'object') {
    const errorMessages = Object.entries(errors)
      .map(([, messages]) => {
        const msgs = Array.isArray(messages) ? messages : [messages];
        return msgs.join(', ');
      })
      .filter(Boolean)
      .join('\n');

    if (errorMessages) {
      return errorMessages;
    }
  }

  if (message) {
    return message;
  }

  return defaultMessage;
};