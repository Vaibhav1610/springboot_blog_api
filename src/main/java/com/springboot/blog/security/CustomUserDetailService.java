package com.springboot.blog.security;


import com.springboot.blog.entity.Role;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.entity.User;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUserNameOrEmail(userNameOrEmail,userNameOrEmail).orElseThrow(()->new UsernameNotFoundException("User Not Found with User name or email :"+ userNameOrEmail));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolestoAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolestoAuthorities(Set<Role> roles)
    {
        return roles.stream().map(role->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
