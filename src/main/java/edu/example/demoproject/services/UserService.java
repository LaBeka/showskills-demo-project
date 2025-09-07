package edu.example.demoproject.services;

import edu.example.demoproject.dtos.user.UserCreateDto;
import edu.example.demoproject.dtos.user.UserDto;
import edu.example.demoproject.entities.RoleEntity;
import edu.example.demoproject.entities.user.UserEntity;
import edu.example.demoproject.mappers.UserMapper;
import edu.example.demoproject.repos.RoleRepository;
import edu.example.demoproject.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public UserDto registerNewUser(UserCreateDto dto) {
        List<RoleEntity> roles = Arrays.asList(roleRepository.findByName("ROLE_USER"));
        UserEntity entity = userMapper.dtoToEntity(
                roles,
                passwordEncoder.encode(dto.getPassword()),
                dto);
        userRepository.persist(entity);
        return userMapper.entityToDto(entity);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' is not found", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public Optional<UserEntity> findByUsername(String username)  {
        return userRepository.getByEmail(username);
    }
}
