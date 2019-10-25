package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
}
