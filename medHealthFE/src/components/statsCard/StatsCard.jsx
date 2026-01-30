import styles from './StatsCard.module.css';
import Button from '../button/Button';

const StatsCard = ({
  icon,
  title,
  mainValue,
  mainLabel,
  subLabel,
  primaryAction,
  primaryActionLabel,
  secondaryAction,
  secondaryActionLabel,
}) => {
  return (
    <div className={styles.card}>
      <div className={styles.header}>
        <span className={styles.icon}>{icon}</span>
        <h3 className={styles.title}>{title}</h3>
      </div>

      <div className={styles.content}>
        <span className={styles.mainValue}>{mainValue}</span>
        <span className={styles.mainLabel}>{mainLabel}</span>
        {subLabel && <span className={styles.subLabel}>{subLabel}</span>}
      </div>

      <div className={styles.actions}>
        <button className={styles.actionBtn} onClick={primaryAction}>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z" />
            <circle cx="12" cy="12" r="3" />
          </svg>
          {primaryActionLabel}
        </button>
        <Button variant="outline" size="sm" onClick={secondaryAction}>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
          >
            <path d="M5 12h14" />
            <path d="M12 5v14" />
          </svg>
          {secondaryActionLabel}
        </Button>
      </div>
    </div>
  );
};

export default StatsCard;