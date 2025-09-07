package edu.example.demoproject.repos;

import edu.example.demoproject.dtos.user.UserDto;
import edu.example.demoproject.entities.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<UserEntity, Number> {

    public Optional<UserDto> checkIfUserByEmailExists(String email) {
        return Optional.ofNullable(em.createQuery("""
                        select new edu.example.demoproject.dtos.user.UserDto(
                        p.id, p.fullName, p.accountName, p.email, p.password, 
                        p.enabled)
                        from UserEntity p
                        where p.email = :email
                        """, UserDto.class)
                .setParameter("email", email)
                .getSingleResult());
    }

    public Optional<UserEntity> getByEmail(String email) {
        return em.createQuery("""
                        select u
                        from UserEntity u
                        where u.email = :email
                        """, UserEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}
