// in security 모듈
package kr.elroy.aigoya.security;

import kr.elroy.aigoya.store.domain.Store;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Store store;

    public CustomUserDetails(Store store) { this.store = store; }

    public Store getStore() { return this.store; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_STORE"));
    }
    @Override
    public String getPassword() { return store.getPassword(); }
    @Override
    public String getUsername() { return store.getEmail(); }
    // ... isEnabled() 등 나머지 메서드들 ...
}