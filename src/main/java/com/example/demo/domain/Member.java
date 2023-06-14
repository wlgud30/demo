package com.example.demo.domain;

import com.example.demo.enums.LoginProvider;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Entity(name = "member")
@Table(name = "member")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "provider")
    private LoginProvider provider;

    @Column(name = "providerId")
    private String providerId;

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonManagedReference
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    @Builder
    public Member(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    public List<String> getRoles() {
        return authorities.stream()
                .map(Authority::getRole)
                .collect(toList());
    }

    public Member update(String username){
        this.username = username;
        return this;
    }

}
