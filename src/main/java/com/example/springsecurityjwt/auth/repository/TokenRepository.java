package com.example.springsecurityjwt.auth.repository;

import com.example.springsecurityjwt.auth.domain.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByEmail(String email);

    @Query("DELETE FROM Token AS t WHERE t.email = :email")
    void deleteByMemberId(@Param("email") String email);

}
