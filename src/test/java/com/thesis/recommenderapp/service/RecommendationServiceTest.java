package com.thesis.recommenderapp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.thesis.recommenderapp.dao.WatchedDao;
import com.thesis.recommenderapp.domain.Item;
import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.domain.Watched;

public class RecommendationServiceTest {

    private static final Long TEST_ID_1 = 1L;
    private static final Long TEST_ID_2 = 2L;
    private static final Integer TEST_RATING_1 = 7;
    private static final Integer TEST_RATING_2 = 10;
    private static final String TEST_SEARCH_RESULT = "";
    private static final String URL_CONTAINING_IMDB_ID = "title/tt_imdb_id";

    @InjectMocks
    private RecommendationService underTest;

    @Mock
    private WatchedDao watchedDao;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTopTenPopularItemsShouldReturnTheTenBestRatedItemsInDescendingOrder() {
        //GIVEN
        List<Watched> watchedList = createWatchedWithItems();
        List<Item> sortedItems = Arrays.asList(watchedList.get(1).getItem(), watchedList.get(0).getItem());
        given(watchedDao.findAll()).willReturn(watchedList);
        //WHEN
        List<Item> result = underTest.getTopTenPopularItems();
        //THEN
        Assert.assertEquals(sortedItems, result);
        then(watchedDao).should().findAll();
    }

    @Test
    public void testGetRecommendationsShouldReturnFiveBestRatedItemsInOrderBasedOnFriendsRatings() {
        //GIVEN
        List<Watched> watchedList = createWatchedWithItems();
        List<Item> sortedItems = Arrays.asList(watchedList.get(1).getItem());
        User user = new User();
        user.setFriends(createFriends(watchedList));
        given(userService.getUserByUserName(any())).willReturn(user);
        given(watchedDao.findAllByUserId(any())).willReturn(Arrays.asList(watchedList.get(0)));
        //WHEN
        List<Item> result = underTest.getRecommendations("");
        //THEN
        Assert.assertEquals(sortedItems, result);
        then(userService).should().getUserByUserName(any());
        then(watchedDao).should().findAllByUserId(any());
    }

    private List<Watched> createWatchedWithItems() {
        Item item1 = Item.builder()
                .id(TEST_ID_1)
                .build();
        Item item2 = Item.builder()
                .id(TEST_ID_2)
                .build();
        Watched watched1 = new Watched();
        watched1.setItem(item1);
        watched1.setRating(TEST_RATING_1);
        Watched watched2 = new Watched();
        watched2.setItem(item2);
        watched2.setRating(TEST_RATING_2);
        return Arrays.asList(watched1, watched2);
    }

    private List<User> createFriends(List<Watched> watchedList) {
        User friend = new User();
        friend.setWatched(watchedList);
        return Arrays.asList(friend);
    }

}
