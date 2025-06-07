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

    public void saveCastMembers(List<CastMember> members, int movieId) {
        for (CastMember member : members) {
            saveMember(member, movieId);
        }
    }

    public void saveMember(CastMember member, int movieId) {
        if (member.getCastMap() == null) {
            member.setCastMap(new HashMap<>());
        }
        String role = (member.getCharacter() != null && !member.getCharacter().isEmpty()) ? member.getCharacter()
                : (member.getJob() != null ? member.getJob() : "Unknown");

        member.getCastMap().put(movieId, role);

        castMemberRepository.save(member);
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

    // public void saveCSV(Movie movie){
    // try(FileWriter writer = new FileWriter())

    // return

    // }

    // public void saveCurrentRecommendList(Movie movie) {
    // movieBuffer.add(movie);
    // if (movieBuffer.size() >= BATCH_SIZE) {
    // saver.saveBatch(movieBuffer);
    // movieBuffer.clear();
    // }
    // }

    // public void flush() {
    // if (!movieBuffer.isEmpty()) {
    // saver.saveBatch(movieBuffer);
    // movieBuffer.clear();
    // }
    // }
}
