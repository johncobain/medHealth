import authService from "../../services/authService";
import { useState } from "react";
import { toast } from "react-toastify";
import Button from "../../components/button/Button";

const Settings = () => {
  const [formData, setFormData] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.newPassword !== formData.confirmPassword) {
      toast.error('A nova senha e a confirmação não coincidem.');
      return;
    }

    setLoading(true);
    try {
      await authService.changePassword(formData.oldPassword, formData.newPassword);
      toast.success('Senha alterada com sucesso.');
      setFormData({ oldPassword: '', newPassword: '', confirmPassword: '' });
    } catch (error) {
      const errorMessage = error.response?.data?.reason || 'Erro ao alterar a senha. Tente novamente.';
      const validationErrors = error.response?.data?.errors;
      const fullMessage = validationErrors
        ? `${errorMessage}\n${Object.values(validationErrors).filter(Boolean).join('\n')}`
        : errorMessage;
      toast.error(fullMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <h2 className="text-2xl text-bold mb-lg text-primary">Configurações da Conta</h2>
      
      <div className="mb-xl flex flex-col gap-md bt-sm pt-md">
        <h3 className="text-lg mb-md p-sm">Alterar Senha</h3>

        <form onSubmit={handleSubmit} className="flex flex-col gap-md">
          <div>
            <label className="block text-sm mb-xs">Senha Atual</label>
            <input
              type="password"
              name="oldPassword"
              value={formData.oldPassword}
              onChange={handleChange}
              required
              className="input"
              style={{ width: '37%' }}
            />
          </div>

          <div>
            <label className="block text-sm mb-xs">Nova Senha</label>
            <input
              type="password"
              name="newPassword"
              value={formData.newPassword}
              onChange={handleChange}
              required
              className="input"
              style={{ width: '37%' }}
            />
          </div>

          <div>
            <label className="block text-sm mb-xs">Confirmar Nova Senha</label>
            <input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
              className="input"
              style={{ width: '37%' }}
            />
          </div>

          <div className="mt-sm">
            <Button type="submit" disabled={loading}>
              {loading ? 'Salvando...' : 'Atualizar Senha'}
            </Button>
          </div>
        </form>
      </div>
    </>
  );
};

export default Settings;
