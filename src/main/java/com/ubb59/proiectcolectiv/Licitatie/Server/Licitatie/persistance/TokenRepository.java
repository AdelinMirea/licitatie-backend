package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.persistance;

import com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<VerificationToken, Integer> {

    public VerificationToken findByToken(String token);
}
