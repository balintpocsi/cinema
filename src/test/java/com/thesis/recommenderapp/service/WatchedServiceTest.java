package com.thesis.recommenderapp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.thesis.recommenderapp.dao.WatchedDao;
import com.thesis.recommenderapp.domain.AddToWatchListItem;
import com.thesis.recommenderapp.domain.Item;
import com.thesis.recommenderapp.domain.User;
import com.thesis.recommenderapp.domain.Watched;

public class WatchedServiceTest {

    private static final Long TEST_ITEM_ID = 1L;
    private static final Integer TEST_NEW_RATING = 10;
    private static final Integer TEST_OLD_RATING = 9;
    private static final String TEST_USER_NAME = "";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_WATCHED_ID = 1L;

    @InjectMocks
    private WatchedService underTest;

    @Mock
    private WatchedDao watchedDao;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveWatchedShouldUpdateRatingIfAlreadyExists() {
        //GIVEN
        AddToWatchListItem addToWatchListItem = createAddToWatchListItem();
        User user = createUser();
        Item item = createItem();
        Watched watched = createWatched(item, TEST_OLD_RATING);
        user.addToWatched(watched);
        given(itemService.getItem(TEST_ITEM_ID)).willReturn(item);
        given(userService.getUserByUserName(TEST_USER_NAME)).willReturn(user);
        given(watchedDao.findAllByUserId(TEST_USER_ID)).willReturn(user.getWatched());
        //WHEN
        underTest.saveWatched(addToWatchListItem, TEST_USER_NAME);
        //THEN
        Assert.assertEquals(TEST_NEW_RATING, user.getWatched().get(0).getRating());
        then(itemService).should().getItem(TEST_ITEM_ID);
        then(userService).should().getUserByUserName(TEST_USER_NAME);
        then(userService).should().saveUser(user);
        then(watchedDao).should().findAllByUserId(TEST_USER_ID);
    }

    @Test
    public void testSaveWatchedShouldSaveIfWatchedDoesNotExistYet() {
        //GIVEN
        AddToWatchListItem addToWatchListItem = createAddToWatchListItem();
        User user = createUser();
        Item item = createItem();
        given(itemService.getItem(TEST_ITEM_ID)).willReturn(item);
        given(userService.getUserByUserName(TEST_USER_NAME)).willReturn(user);
        given(watchedDao.findAllByUserId(TEST_USER_ID)).willReturn(user.getWatched());
        //WHEN
        underTest.saveWatched(addToWatchListItem, TEST_USER_NAME);
        //THEN
        Assert.assertEquals(TEST_NEW_RATING, user.getWatched().get(0).getRating());
        then(itemService).should().getItem(TEST_ITEM_ID);
        then(userService).should().getUserByUserName(TEST_USER_NAME);
        then(userService).should().saveUser(user);
        then(watchedDao).should().findAllByUserId(TEST_USER_ID);
        then(watchedDao).should().save(any());
    }

    @Test
    public void testGetWatchedListShouldDelegateCallToDao() {
        //GIVEN
        Watched watched = new Watched();
        Page<Watched> watchedPage = new PageImpl<>(Arrays.asList(watched));
        Pageable pageable = Mockito.mock(Pageable.class);
        given(watchedDao.findAllByUserId(TEST_USER_ID, pageable)).willReturn(watchedPage);
        //WHEN
        Page<Watched> result = underTest.getWatchedList(TEST_USER_ID, pageable);
        //THEN
        Assert.assertEquals(watchedPage, result);
        then(watchedDao).should().findAllByUserId(TEST_USER_ID, pageable);
    }

    @Test
    public void testGetWatchedShouldDelegateCallToDao() {
        //GIVEN
        Watched watched = new Watched();
        User user = new User();
        Item item = new Item();
        given(watchedDao.findByUserAndItem(user, item)).willReturn(watched);
        //WHEN
        Watched result = underTest.getWatched(user, item);
        //THEN
        Assert.assertEquals(watched, result);
        then(watchedDao).should().findByUserAndItem(user, item);
    }

    @Test
    public void testDeleteWatchedShouldRemoveWatchedFromUsersList() {
        //GIVEN
        Watched watched = new Watched();
        watched.setId(TEST_WATCHED_ID);
        User user = new User();
        user.addToWatched(watched);
        given(watchedDao.findById(TEST_WATCHED_ID)).willReturn(java.util.Optional.of(watched));
        //WHEN
        underTest.deleteWatched(user, TEST_WATCHED_ID);
        //THEN
        Assert.assertFalse(user.getWatched().contains(watched));
        then(watchedDao).should().findById(TEST_WATCHED_ID);
        then(watchedDao).should().delete(watched);
        then(userService).should().saveUser(user);
    }

    private User createUser() {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setUserName(TEST_USER_NAME);
        return user;
    }

    private AddToWatchListItem createAddToWatchListItem() {
        AddToWatchListItem addToWatchListItem = new AddToWatchListItem();
        addToWatchListItem.setItemId(TEST_ITEM_ID);
        addToWatchListItem.setRating(TEST_NEW_RATING);
        return addToWatchListItem;
    }

    private Item createItem() {
        return Item.builder()
                .id(TEST_ITEM_ID)
                .build();
    }

    private Watched createWatched(Item item, Integer rating) {
        Watched watched = new Watched();
        watched.setItem(item);
        watched.setRating(rating);
        return watched;
    }

}
