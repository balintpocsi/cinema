package com.thesis.recommenderapp.service;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thesis.recommenderapp.dao.WatchedDao;
import com.thesis.recommenderapp.domain.Item;
import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.domain.Watched;

@Service
public class RecommendationService {

    @Autowired
    private WatchedDao watchedDao;
    @Autowired
    private UserService userService;

    public List<Item> getTopTenPopularItems() {
        Map<Item, Integer> weightedItems = getWeightedItems(watchedDao.findAll());
        Map<Item, Integer> sorted = sortMap(weightedItems);
        List<Item> items = new ArrayList<>(sorted.keySet());
        return (items.size() > 10) ? items.subList(0, 10) : items;
    }

    public List<Item> getRecommendations(String userName) {
        User user = userService.getUserByUserName(userName);
        Iterable<Watched> watchedList = getWatchedListsOfFriends(user);
        List<Item> myWatchedItems = getMyWatchedItems(user);
        Map<Item, Integer> weightedItems = getWeightedItems(watchedList);
        Map<Item, Integer> sorted = sortMap(weightedItems);
        List<Item> items = new ArrayList<>(sorted.keySet());
        items = items.stream().filter(item -> !myWatchedItems.contains(item)).collect(Collectors.toList());
        return (items.size() > 5) ? items.subList(0, 5) : items;
    }

    private List<Item> getMyWatchedItems(User user) {
        return watchedDao.findAllByUserId(user.getId()).stream()
                    .map(Watched::getItem)
                    .collect(Collectors.toList());
    }

    private Iterable<Watched> getWatchedListsOfFriends(User user) {
        return user.getFriends().stream()
                    .flatMap(friend -> friend.getWatched().stream())
                    .collect(Collectors.toList());
    }

    private Map<Item, Integer> getWeightedItems(Iterable<Watched> watchedList) {
        Map<Item, Integer> itemsWithRating = new HashMap<>();
        for (Watched watched : watchedList) {
            assignValuesToItems(itemsWithRating, watched);
        }
        return itemsWithRating;
    }

    private void assignValuesToItems(Map<Item, Integer> itemsWithRating, Watched watched) {
        Item item = watched.getItem();
        if (itemsWithRating.containsKey(item)) {
            Integer value = itemsWithRating.get(item) + watched.getRating();
            itemsWithRating.replace(item, value);
        } else {
            itemsWithRating.put(item, watched.getRating());
        }
    }

    private Map<Item, Integer> sortMap(Map<Item, Integer> weightedItems) {
        return weightedItems.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

}
