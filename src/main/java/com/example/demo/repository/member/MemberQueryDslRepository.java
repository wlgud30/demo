package com.example.demo.repository.member;

import com.example.demo.domain.QAuthority;
import com.example.demo.domain.QMember;
import com.example.demo.vo.MemberInfoVO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<MemberInfoVO> getUserInfo(Long user_id) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.constructor(
                        MemberInfoVO.class,
                        QMember.member.email,
                        QMember.member.username,
                        QAuthority.authority.role
                )).from(QMember.member)
                .leftJoin(QAuthority.authority)
                .on(QMember.member.eq(QAuthority.authority.member))
                .where(QMember.member.id.eq(user_id))
                .fetchOne());
    }


}
