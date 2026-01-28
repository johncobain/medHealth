import { Outlet, NavLink } from "react-router-dom";
import styles from "./MainLayout.module.css";

const MainLayout = () => {
  return (
    <div className={styles.layout}>
      <header className='header flex justify-between items-center'>
        <div>
          <h1>MedHealth</h1>
          <p className='text-light'>Sistema de Gestão Hospitalar</p>
        </div>
        <nav className={styles.nav}>
          <NavLink 
            to="/" 
            className={({ isActive }) => isActive ? styles.active : ''}
          >
            Home
          </NavLink>
          <NavLink 
            to="/agendamentos"
            className={({ isActive }) => isActive ? styles.active : ''}
          >
            Agendamentos
          </NavLink>
          <NavLink 
            to="/pacientes"
            className={({ isActive }) => isActive ? styles.active : ''}
          >
            Pacientes
          </NavLink>
        </nav>
      </header>
      <main className={`container mt-md ${styles.main}`}>
        <Outlet />
      </main>
      <footer className={styles.footer}>
        <p className='text-light text-sm'>© 2026 MedHealth - Sistema de Gestão Hospitalar</p>
      </footer>
    </div>
  )
}

export default MainLayout;