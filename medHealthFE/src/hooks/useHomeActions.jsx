import { useNavigate } from 'react-router-dom';
import { DoctorIcon, PatientIcon, AppointmentIcon, ProfileIcon } from '../components/icons/AppIcons';

export const useHomeActions = () => {
  const navigate = useNavigate();

  const adminQuickActions = [
    { label: 'Cadastrar Paciente', variant: 'primary', icon: <PatientIcon />, onClick: () => navigate('/patients?action=new') },
    { label: 'Cadastrar Médico', variant: 'secondary', icon: <DoctorIcon />, onClick: () => navigate('/doctors?action=new') },
    { label: 'Nova Consulta', variant: 'outline', icon: <AppointmentIcon />, onClick: () => navigate('/appointments?action=new') },
  ];

  const userQuickActions = [
    { label: 'Minhas Consultas', variant: 'primary', icon: <AppointmentIcon />, onClick: () => navigate('/appointments') },
    { label: 'Meu Perfil', variant: 'outline', icon: <ProfileIcon />, onClick: () => navigate('/settings') },
  ];

  return { adminQuickActions, userQuickActions };
};