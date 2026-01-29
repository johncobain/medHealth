import { useState } from 'react';
import { useSearchParams, useNavigate, Link } from 'react-router-dom';
import styles from './LoginPage.module.css';
import Button from '../../components/button/Button';
import authService from '../../services/authService';

const ResetPasswordPage = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const navigate = useNavigate();

  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (password !== confirmPassword) {
      setError('As senhas não coincidem');
      return;
    }

    try {
      await authService.resetPassword(token, password);
      setSuccess(true);
      setTimeout(() => navigate('/login'), 3000);
    } catch (err) {
      const errorMessage =
        err.response?.data?.reason || 'Erro ao redefinir a senha. O link pode ter expirado.';
      const validationError = err.response?.data?.errors || {};
      setError(errorMessage + '\n' + Object.values(validationError).join('\n'));
    }
  };

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
            {error && <p className={styles.error}>{error}</p>}
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
