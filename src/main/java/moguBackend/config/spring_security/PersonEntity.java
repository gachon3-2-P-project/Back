package moguBackend.config.spring_security;

import moguBackend.constant.Role;

public interface PersonEntity {

    public Long getId();
    public String getUsername();

    public Role getRole();

    public String getPassword();

    public void setUsername(String username);


    public void setPassword(String password);
}
