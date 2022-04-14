package com.example.demo.repository.member;

import com.example.demo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    boolean existsByEmail(String email);

    @Query("select m from member m join fetch m.authorities a where m.username = :username")
    Optional<Member> findByUsernameWithAuthority(String username);

    Optional<Member> findByEmail(String email);
}
