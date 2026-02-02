import { Stethoscope, Users, Calendar, User, FileText } from 'lucide-react';

export const DoctorIcon = ({ size = 18 }) => (
  <Stethoscope size={size} strokeWidth={2} />
);

export const PatientIcon = ({ size = 18 }) => (
  <Users size={size} strokeWidth={2} />
);

export const AppointmentIcon = ({ size = 18 }) => (
  <Calendar size={size} strokeWidth={2} />
);

export const ProfileIcon = ({ size = 18 }) => (
  <User size={size} strokeWidth={2} />
);

export const RequestIcon = ({ size = 18 }) => (
  <FileText size={size} strokeWidth={2} />
);
