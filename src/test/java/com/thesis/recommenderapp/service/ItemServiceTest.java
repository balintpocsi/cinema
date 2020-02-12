package com.thesis.recommenderapp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.io.IOException;
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

import com.thesis.recommenderapp.dao.ItemDao;
import com.thesis.recommenderapp.domain.Item;
import com.thesis.recommenderapp.domain.UploadItemRequest;
import com.thesis.recommenderapp.service.exceptions.ImdbException;

public class ItemServiceTest {

    private static final Long TEST_ID = 1L;
    private static final String TEST_SUBSTRING = "test";
    private static final String TEST_IMDB_ID = "tt_imdb_id";
    private static final String TEST_SEARCH_RESULT = "";
    private static final String URL_CONTAINING_IMDB_ID = "title/tt_imdb_id";

    @InjectMocks
    private ItemService underTest;

    @Mock
    private ItemDao itemDao;

    @Mock
    private ImdbAPIGetService imdbAPIGetService;

    @Mock
    private JsonParserService jsonParserService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItemShouldDelegateCallToDao() {
        //GIVEN
        Item item = createItem();
        given(itemDao.findById(TEST_ID)).willReturn(java.util.Optional.ofNullable(item));
        //WHEN
        Item result = underTest.getItem(TEST_ID);
        //THEN
        Assert.assertEquals(item, result);
        then(itemDao).should().findById(TEST_ID);
    }

    @Test
    public void testGetItemsBySubstringShouldDelegateCallToDao() {
        //GIVEN
        Item item = createItem();
        Page<Item> itemPage = new PageImpl<>(Arrays.asList(item));
        Pageable pageable = Mockito.mock(Pageable.class);
        given(itemDao.findAllByTitleContainingIgnoreCase(TEST_SUBSTRING, pageable)).willReturn(itemPage);
        //WHEN
        Page<Item> result = underTest.getItemsBySubstring(TEST_SUBSTRING, pageable);
        //THEN
        Assert.assertEquals(itemPage, result);
        then(itemDao).should().findAllByTitleContainingIgnoreCase(TEST_SUBSTRING, pageable);
    }

    @Test
    public void testSaveByImdbIdShouldGetItemIdIfItemAlreadyExists() {
        //GIVEN
        Item item = createItem();
        given(itemDao.existsByImdbId(TEST_IMDB_ID)).willReturn(true);
        given(itemDao.findByImdbId(TEST_IMDB_ID)).willReturn(item);
        //WHEN
        Long result = underTest.saveByImdbId(TEST_IMDB_ID);
        //THEN
        Assert.assertEquals(TEST_ID, result);
        then(itemDao).should().existsByImdbId(TEST_IMDB_ID);
        then(itemDao).should().findByImdbId(TEST_IMDB_ID);
    }

    @Test
    public void testSaveByImdbIdShouldReturnIdOfNewlyCreatedItemIfNotAlreadyExists() throws IOException {
        //GIVEN
        Item itemToSave = Item.builder().build();
        Item savedItem = createItem();
        given(itemDao.existsByImdbId(TEST_IMDB_ID)).willReturn(false);
        given(imdbAPIGetService.getSpecificSearchResults(TEST_IMDB_ID)).willReturn(TEST_SEARCH_RESULT);
        given(jsonParserService.getItem(TEST_SEARCH_RESULT)).willReturn(itemToSave);
        given(itemDao.save(itemToSave)).willReturn(savedItem);
        //WHEN
        Long result = underTest.saveByImdbId(TEST_IMDB_ID);
        //THEN
        Assert.assertEquals(TEST_ID, result);
        then(itemDao).should().existsByImdbId(TEST_IMDB_ID);
        then(imdbAPIGetService).should().getSpecificSearchResults(TEST_IMDB_ID);
        then(jsonParserService).should().getItem(TEST_SEARCH_RESULT);
        then(itemDao).should().save(itemToSave);
    }

    @Test(expected = ImdbException.class)
    public void testSaveByImdbIdShouldThrowExceptionIfImdbAPIFailed() throws IOException {
        //GIVEN
        given(imdbAPIGetService.getSpecificSearchResults(any())).willThrow(new IOException());
        //WHEN
        Long result = underTest.saveByImdbId(TEST_IMDB_ID);
        //THEN exception
    }

    @Test
    public void testSaveItemShouldSaveByTitleIfImdbIdIsNotPresent() throws IOException {
        //GIVEN
        Item itemToSave = Item.builder().build();
        Item savedItem = createItem();
        UploadItemRequest uploadItemRequest = createUploadItemRequest("");
        given(imdbAPIGetService.getGeneralSearchResults(uploadItemRequest)).willReturn(TEST_SEARCH_RESULT);
        given(jsonParserService.getImdbId(TEST_SEARCH_RESULT)).willReturn(TEST_IMDB_ID);
        given(itemDao.existsByImdbId(TEST_IMDB_ID)).willReturn(false);
        given(imdbAPIGetService.getSpecificSearchResults(TEST_IMDB_ID)).willReturn(TEST_SEARCH_RESULT);
        given(jsonParserService.getItem(TEST_SEARCH_RESULT)).willReturn(itemToSave);
        given(itemDao.save(itemToSave)).willReturn(savedItem);
        //WHEN
        Long result = underTest.saveItem(uploadItemRequest);
        //THEN
        Assert.assertEquals(TEST_ID, result);
        then(imdbAPIGetService).should().getGeneralSearchResults(uploadItemRequest);
        then(jsonParserService).should().getImdbId(TEST_SEARCH_RESULT);
        then(itemDao).should().existsByImdbId(TEST_IMDB_ID);
        then(imdbAPIGetService).should().getSpecificSearchResults(TEST_IMDB_ID);
        then(jsonParserService).should().getItem(TEST_SEARCH_RESULT);
        then(itemDao).should().save(itemToSave);
    }

    @Test
    public void testSaveItemShouldSaveByURLIfImdbIdIsPresent() throws IOException {
        //GIVEN
        Item itemToSave = Item.builder().build();
        Item savedItem = createItem();
        UploadItemRequest uploadItemRequest = createUploadItemRequest(URL_CONTAINING_IMDB_ID);
        given(itemDao.existsByImdbId(TEST_IMDB_ID)).willReturn(false);
        given(imdbAPIGetService.getSpecificSearchResults(TEST_IMDB_ID)).willReturn(TEST_SEARCH_RESULT);
        given(jsonParserService.getItem(TEST_SEARCH_RESULT)).willReturn(itemToSave);
        given(itemDao.save(itemToSave)).willReturn(savedItem);
        //WHEN
        Long result = underTest.saveItem(uploadItemRequest);
        //THEN
        Assert.assertEquals(TEST_ID, result);
        then(itemDao).should().existsByImdbId(TEST_IMDB_ID);
        then(imdbAPIGetService).should().getSpecificSearchResults(TEST_IMDB_ID);
        then(jsonParserService).should().getItem(TEST_SEARCH_RESULT);
        then(itemDao).should().save(itemToSave);
    }



    @Test(expected = ImdbException.class)
    public void testSaveItemShouldThrowExceptionIfImdbAPIFailed() throws IOException {
        //GIVEN
        UploadItemRequest uploadItemRequest = createUploadItemRequest("");
        given(imdbAPIGetService.getGeneralSearchResults(any())).willThrow(new IOException());
        //WHEN
        Long result = underTest.saveItem(uploadItemRequest);
        //THEN exception
    }

    private Item createItem() {
        return Item.builder()
                .id(TEST_ID)
                .build();
    }

    private UploadItemRequest createUploadItemRequest(String titleOrURL) {
        UploadItemRequest uploadItemRequest = new UploadItemRequest();
        uploadItemRequest.setTitleOrURL(titleOrURL);
        return uploadItemRequest;
    }

}
