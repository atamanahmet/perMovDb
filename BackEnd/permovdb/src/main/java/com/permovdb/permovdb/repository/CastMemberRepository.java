package com.permovdb.permovdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.permovdb.permovdb.domain.CastMember;

@Repository
public interface CastMemberRepository extends JpaRepository<CastMember, Integer> {

}
