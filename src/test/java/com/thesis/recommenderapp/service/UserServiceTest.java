package com.thesis.recommenderapp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thesis.recommenderapp.dao.UserDao;
import com.thesis.recommenderapp.domain.RegistrationRequest;
import com.thesis.recommenderapp.domain.User;

public class UserServiceTest {

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_FRIEND_ID = 2L;
    private static final String TEST_USER_NAME = "";
    private static final String TEST_SUBSTRING = "test";
    private static final String TEST_ENCODED_PASSWORD = "encoded password";

    @InjectMocks
    private UserService underTest;

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserShouldDelegateCallToDao() {
        //GIVEN
        User user = new User();
        user.setId(TEST_FRIEND_ID);
        given(userDao.findById(TEST_FRIEND_ID)).willReturn(java.util.Optional.of(user));
        //WHEN
        User result = underTest.getUser(TEST_FRIEND_ID);
        //THEN
        Assert.assertEquals(user, result);
        then(userDao).should().findById(TEST_FRIEND_ID);
    }

    @Test
    public void testGetUserByUserNameShouldDelegateCallToDao() {
        //GIVEN
        User user = new User();
        user.setUserName(TEST_USER_NAME);
        given(userDao.findByUserName(TEST_USER_NAME)).willReturn(user);
        //WHEN
        User result = underTest.getUserByUserName(TEST_USER_NAME);
        //THEN
        Assert.assertEquals(user, result);
        then(userDao).should().findByUserName(TEST_USER_NAME);
    }

    @Test
    public void testGetUsersBySubstringShouldDelegateCallToDao() {
        //GIVEN
        User user = new User();
        Page<User> userPage = new PageImpl<>(Arrays.asList(user));
        Pageable pageable = Mockito.mock(Pageable.class);
        given(userDao.findAllByUserNameContainingIgnoreCase(TEST_SUBSTRING, pageable)).willReturn(userPage);
        //WHEN
        Page<User> result = underTest.getUsersBySubstring(TEST_SUBSTRING, pageable);
        //THEN
        Assert.assertEquals(userPage, result);
        then(userDao).should().findAllByUserNameContainingIgnoreCase(TEST_SUBSTRING, pageable);
    }

    @Test
    public void testGetFriendsShouldReturnPageImplementationOfUsersFriends() {
        //GIVEN
        User user = new User();
        List<User> friends = Arrays.asList(createUser(TEST_FRIEND_ID));
        user.setFriends(friends);
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<User> friendPage = new PageImpl<>(friends, pageable, friends.size());
        given(userDao.findByUserName(TEST_USER_NAME)).willReturn(user);
        //WHEN
        Page<User> result = underTest.getFriends(TEST_USER_NAME, pageable);
        //THEN
        Assert.assertEquals(friendPage, result);
        then(userDao).should().findByUserName(TEST_USER_NAME);
    }

    @Test
    public void testSaveUserShouldDelegateCallToDao() {
        //GIVEN
        User userToSave = new User();
        User savedUser = createUser(TEST_USER_ID);
        given(userDao.save(userToSave)).willReturn(savedUser);
        //WHEN
        Long result = underTest.saveUser(userToSave);
        //THEN
        Assert.assertEquals(savedUser.getId(), result);
        then(userDao).should().save(userToSave);
    }

    @Test
    public void testAddFriendShouldAddTwoUsersAsFriends() {
        //GIVEN
        User user = createUser(TEST_USER_ID);
        User friend = createUser(TEST_FRIEND_ID);
        given(userDao.findById(TEST_USER_ID)).willReturn(java.util.Optional.of(user));
        given(userDao.findById(TEST_FRIEND_ID)).willReturn(java.util.Optional.of(friend));
        //WHEN
        underTest.addFriend(friend.getId(), user.getId());
        //THEN
        Assert.assertTrue(user.getFriends().contains(friend));
        Assert.assertTrue(friend.getFriends().contains(user));
        then(userDao).should(times(2)).findById(any());
        then(userDao).should(times(2)).save(any());
    }

    @Test
    public void testDeleteFriendShouldRemoveUsersFromEachOthersList() {
        //GIVEN
        User user = createUser(TEST_USER_ID);
        User friend = createUser(TEST_FRIEND_ID);
        user.addFriend(friend);
        friend.addFriend(user);
        given(userDao.findById(TEST_USER_ID)).willReturn(java.util.Optional.of(user));
        given(userDao.findById(TEST_FRIEND_ID)).willReturn(java.util.Optional.of(friend));
        //WHEN
        underTest.deleteFriend(friend.getId(), user.getId());
        //THEN
        Assert.assertFalse(user.getFriends().contains(friend));
        Assert.assertFalse(friend.getFriends().contains(user));
        then(userDao).should(times(2)).findById(any());
        then(userDao).should(times(2)).save(any());
    }

    @Test
    public void testRegisterUserShouldSaveUserWithEncodedPasswordIfUserDoesNotExistYet() {
        //GIVEN
        RegistrationRequest registrationRequest = createRegistrationRequest();
        User userToBeRegistered = new User();
        User registeredUser = createUser(TEST_USER_ID);
        given(userDao.findByUserName("")).willReturn(null);
        given(passwordEncoder.encode("")).willReturn(TEST_ENCODED_PASSWORD);
        given(userDao.save(userToBeRegistered)).willReturn(registeredUser);
        //WHEN
        Long result = underTest.registerUser(registrationRequest);
        //THEN
        Assert.assertEquals(TEST_USER_ID, result);
        then(userDao).should().findByUserName("");
        then(passwordEncoder).should().encode("");
        then(userDao).should().save(userToBeRegistered);
    }

    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private RegistrationRequest createRegistrationRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUserName("");
        registrationRequest.setPassword("");
        return registrationRequest;
    }

}
