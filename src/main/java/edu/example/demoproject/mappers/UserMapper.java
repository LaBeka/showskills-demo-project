package edu.example.demoproject.mappers;

import edu.example.demoproject.dtos.user.UserCreateDto;
import edu.example.demoproject.dtos.user.UserDto;
import edu.example.demoproject.entities.RoleEntity;
import edu.example.demoproject.entities.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "encodedPassword", target = "password")
    @Mapping(source = "roles", target = "roles")
    @Mapping(constant = "true", target = "enabled")
    UserEntity dtoToEntity(List<RoleEntity> roles, String encodedPassword, UserCreateDto dto);

    UserDto entityToDto(UserEntity entity);
}
