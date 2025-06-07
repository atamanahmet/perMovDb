package com.permovdb.permovdb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.permovdb.permovdb.domain.CastMember;
import com.permovdb.permovdb.repository.CastMemberRepository;

@Service
public class CastMemberService {

    @Autowired
    CastMemberRepository castMemberRepository;

    public void saveCastMembers(List<CastMember> members, Integer movieId) {
        for (CastMember member : members) {
            saveMember(member, movieId);
        }
    }

    public void saveMember(CastMember member, Integer movieId) {
        CastMember existingMember = null;
        if (member.getId() != null) {
            existingMember = castMemberRepository.findById(member.getId()).orElse(null);
        }

        if (existingMember != null) {
            if (existingMember.getCastMap() == null) {
                existingMember.setCastMap(new HashMap<>());
            }
            String role = (member.getCharacter() != null && !member.getCharacter().isEmpty()) ? member.getCharacter()
                    : (member.getJob() != null ? member.getJob() : "Unknown");

            existingMember.getCastMap().put(movieId, role);

            castMemberRepository.save(existingMember);
        } else {
            if (member.getCastMap() == null) {
                member.setCastMap(new HashMap<>());
            }
            String role = (member.getCharacter() != null && !member.getCharacter().isEmpty()) ? member.getCharacter()
                    : (member.getJob() != null ? member.getJob() : "Unknown");

            member.getCastMap().put(movieId, role);

            castMemberRepository.save(member);
        }
    }

    public CastMember findMovieById(Integer id) {
        return castMemberRepository.findById(id).orElse(null);
    }

    public List<CastMember> getAllMovies() {
        return castMemberRepository.findAll();
    }

    public List<CastMember> getMoviesFromIdSet(Set<Integer> idSet) {
        return castMemberRepository.findAllById(idSet);
    }
}
