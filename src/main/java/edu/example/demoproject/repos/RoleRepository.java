package edu.example.demoproject.repos;

import edu.example.demoproject.entities.RoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepository extends BaseRepository<RoleEntity, Number>{

    public List<RoleEntity> getRoles() {
        return em.createQuery("""
                        select r
                        from RoleEntity r
                        """, RoleEntity.class)
                .getResultList();
    }

    public RoleEntity findByName(String roleUser) {
        return em.createQuery("""
                        select r
                        from RoleEntity r
                        where r.name = :name
                        """, RoleEntity.class)
                .setParameter("name", roleUser)
                .getSingleResult();
    }
}
