import { Link, useLocation, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useAuth } from '../../context/AuthContext';
import styles from './LoginPage.module.css';
import Button from '../../components/button/Button';
import { useState } from 'react';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const from = location.state?.from?.pathname || '/';

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await login(email, password);
      toast.success('Login realizado com sucesso.');
      navigate(from, { replace: true });
    } catch (error) {
      const message = 'Falha no login. Verifique suas credenciais e tente novamente.';
      toast.error(message);
    }
  };

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h1 className={styles.title}>MedHealth</h1>
        <h2 className={styles.subtitle}>Login</h2>

        <div className="mt-md">
          <label htmlFor="email" className={styles.inputlabel}>
            Email
          </label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="input"
          />
        </div>

        <div className="mt-md">
          <label htmlFor="password" className={styles.inputlabel}>
            Senha
          </label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="input"
          />
        </div>

        <div className="text-right mt-sm mb-md">
          <Link to="/forgot-password" className="text-sm text-primary link">
            Esqueci minha senha
          </Link>
        </div>
        <Button type="submit">Entrar</Button>
      </form>
    </div>
  );
};

export default LoginPage;
