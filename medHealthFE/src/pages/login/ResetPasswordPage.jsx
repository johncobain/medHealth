import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import styles from './LoginPage.module.css';
import Button from '../../components/button/Button';
import authService from '../../services/authService';

const ResetPasswordPage = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const navigate = useNavigate();

  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      toast.error('As senhas não coincidem.');
      return;
    }

    try {
      await authService.resetPassword(token, password);
      toast.success('Senha redefinida com sucesso! Redirecionando para o login...');
      setSuccess(true);
      setTimeout(() => navigate('/login'), 3000);
    } catch (err) {
      const errorMessage =
        err.response?.data?.reason || 'Erro ao redefinir a senha. O link pode ter expirado.';
      const validationErrors = err.response?.data?.errors || {};
      const fullMessage = Object.keys(validationErrors).length
        ? `${errorMessage}\n${Object.values(validationErrors).join('\n')}`
        : errorMessage;
      toast.error(fullMessage);
    }
  };

  useEffect(() => {
    if (!token) toast.error('Token inválido ou ausente.');
  }, [token]);

  if (!token) {
    return (
      <div className={styles.container}>
        <div className={styles.form}>
          <p className={styles.error}>Token inválido ou ausente.</p>
          <Link to="/login">Voltar ao Login</Link>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <form className={styles.form} onSubmit={handleSubmit}>
        <h2 className={styles.title}>Redefinir Senha</h2>

        {success ? (
          <div className="text-center">
            <p className="text-bold text-success mb-md">Senha redefinida com sucesso!</p>
            <p>Redirecionando para o login...</p>
          </div>
        ) : (
          <>
            <div className="mt-md">
              <label htmlFor="pass" className={styles.inputlabel}>
                Nova Senha
              </label>
              <input
                type="password"
                id="pass"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className='input'
              />
            </div>
            <div className="mt-md">
              <label htmlFor="confPass" className={styles.inputlabel}>
                Confirmar Senha
              </label>
              <input
                type="password"
                id="confPass"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                className='input'
              />
            </div>
            <Button type="submit">Salvar Senha</Button>
          </>
        )}
      </form>
    </div>
  );
};

export default ResetPasswordPage;
