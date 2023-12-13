package moguBackend.config.spring_security.auth;

import lombok.extern.slf4j.Slf4j;
import moguBackend.common.Role;
import moguBackend.config.spring_security.PersonEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class PrincipalDetails implements UserDetails {

    private PersonEntity person;


    public PrincipalDetails(PersonEntity person) {
        this.person = person;
        System.out.println("=====" + person.getUsername());
        System.out.println("====" + person.getRole());
    }

    public PersonEntity getPerson() {
        return person;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        log.info( " -----");
//        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        log.info(person.getRole() + " -----");
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + person.getRole().toString()));
//        return authorities;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + person.getRole().toString()));
        return authorities;
    }


}
