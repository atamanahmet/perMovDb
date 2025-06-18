package com.permovdb.permovdb.Utility;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.permovdb.permovdb.domain.User;
import com.permovdb.permovdb.domain.UserList;

public class ListSelector {

        private static final Map<String, Map<String, Function<UserList, Set<Integer>>>> mediaMap;

        static {
                Map<String, Function<UserList, Set<Integer>>> movieMap = Map.of(
                                "watchlist", UserList::getMovieWatchlist,
                                "watchedlist", UserList::getMovieWatchedlist,
                                "lovedlist", UserList::getMovieLovedlist);

                Map<String, Function<UserList, Set<Integer>>> tvMap = Map.of(
                                "watchlist", UserList::getTvWatchlist,
                                "watchedlist", UserList::getTvWatchedlist,
                                "lovedlist", UserList::getTvLovedlist);

                mediaMap = Map.of(
                                "movie", movieMap,
                                "tv", tvMap);
        }

        public static Set<Integer> getIdListToEdit(User user, String mediaType, String listType) {
                return Optional.ofNullable(mediaMap.get(mediaType))
                                .map(innerMap -> innerMap.get(listType))
                                .map(f -> f.apply(user.getUserList()))
                                .orElseThrow(() -> new IllegalArgumentException("Invalid mediaType or listType"));
        }
}