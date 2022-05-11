package com.example.demo.repository.authority;

import com.example.demo.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {

    @Query("select a from Authority a inner join a.member m where m.id = :user_id")
    Optional<List<Authority>> findByUserId(Long user_id);

}
