package com.bank.credit.credit_bank_server.entity.user;

import com.bank.credit.credit_bank_server.entity.action.Action;
import com.bank.credit.credit_bank_server.entity.clientType.ClientType;
import com.bank.credit.credit_bank_server.entity.demande.Demande;
import com.bank.credit.credit_bank_server.entity.permission.AgencePermission;
import com.bank.credit.credit_bank_server.entity.permission.HandlePermission;
import com.bank.credit.credit_bank_server.entity.permission.RegionPermission;
import com.bank.credit.credit_bank_server.entity.session.UserSession;
import com.bank.credit.credit_bank_server.entity.simulation.Simulation;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="_user")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private Date creationDate;
  private Boolean enabled;
  private Boolean expired;
  private Boolean locked;
  private Boolean credentialsExired;

  @Enumerated(value = EnumType.STRING)
  @Nullable
  private ClientType type;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<RoleUser> userRoles;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Action> actions;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Demande> demandes;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Simulation> simulations;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<AgencePermission> agencyPermissions;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<RegionPermission> regionPermissions;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<HandlePermission> handlePermissions;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserSession> sessions;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for(RoleUser userRole: userRoles){
      authorities.add(new SimpleGrantedAuthority(userRole.getRole().getDesignation()));
    }
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !expired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !credentialsExired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}

