package br.edu.ifba.inf012.medHealthAPI.models.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "person_id", nullable = false, unique = true)
  private Person person;

  @Column(nullable = false, length = 72)
  private String password;

  @Column(name = "password_reset_token")
  private String passwordResetToken;

  @Column(name = "password_reset_expires")
  private LocalDateTime passwordResetExpires;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "roles_id")
  )
  private Set<Role> roles = new HashSet<>();

  @Column(name = "created_at", updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @PrePersist
  protected void onCreate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    updatedAt = now;
  }

  public User() {}

  public User(Person person, String password, Set<Role> roles) {
    this.person = person;
    this.password = password;
    this.roles = roles;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Person getPerson() { return person; }
  public void setPerson(Person person) { this.person = person; }

  @Override
  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getPasswordResetToken() { return passwordResetToken; }
  public void setPasswordResetToken(String passwordResetToken) {
    this.passwordResetToken = passwordResetToken;
  }

  public LocalDateTime getPasswordResetExpires() { return passwordResetExpires; }
  public void setPasswordResetExpires(LocalDateTime passwordResetExpires) {
    this.passwordResetExpires = passwordResetExpires;
  }

  public Set<Role> getRoles() { return roles; }
  public void setRoles(Set<Role> roles) { this.roles = roles; }

  public Timestamp getCreatedAt() { return createdAt; }
  public Timestamp getUpdatedAt() { return updatedAt; }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
        .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
      return this.person.getEmail();
  }

  public void setRole(Role role) {
        this.roles.add(role);
    }
}
