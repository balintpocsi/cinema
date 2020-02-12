package com.thesis.recommenderapp.service;

import com.thesis.recommenderapp.dao.ItemDao;
import com.thesis.recommenderapp.domain.Item;
import com.thesis.recommenderapp.domain.UploadItemRequest;
import com.thesis.recommenderapp.service.exceptions.ImdbException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ImdbAPIGetService imdbAPIGetService;
    @Autowired
    private JsonParserService jsonParserService;

    public Item getItem(Long id) {
        return itemDao.findById(id).get();
    }

    public Page<Item> getItemsBySubstring(String substring, Pageable pageable) {
        return itemDao.findAllByTitleContainingIgnoreCase(substring, pageable);
    }

    public Long saveByImdbId(String imdbId) {
        try {
            return save(imdbId);
        } catch (IOException e) {
            throw new ImdbException(e.getMessage());
        }
    }

    public Long saveItem(UploadItemRequest uploadItemRequest) {
        try {
            return saveByTitleOrURL(uploadItemRequest);
        } catch (IOException e) {
            throw new ImdbException(e.getMessage());
        }
    }

    private Long saveByTitleOrURL(UploadItemRequest uploadItemRequest) throws IOException {
        Long id;
        if (!uploadItemRequest.getTitleOrURL().contains("title/tt")) {
            id = saveByTitle(uploadItemRequest);
        } else {
            id = saveByURL(uploadItemRequest);
        }
        return id;
    }

    private Long saveByTitle(UploadItemRequest uploadItemRequest) throws IOException {
        String generalSearchResult = imdbAPIGetService.getGeneralSearchResults(uploadItemRequest);
        return save(jsonParserService.getImdbId(generalSearchResult));
    }

    private Long saveByURL(UploadItemRequest uploadItemRequest) throws IOException {
        List<String> urlParts = Arrays.asList(uploadItemRequest.getTitleOrURL().split("/"));
        String imdbId = urlParts.get(urlParts.indexOf("title") + 1);
        return save(imdbId);
    }

    private Long save(String imdbId) throws IOException {
        Long id;
        if (!itemDao.existsByImdbId(imdbId)) {
            String specificSearchResult = imdbAPIGetService.getSpecificSearchResults(imdbId);
            id = itemDao.save(jsonParserService.getItem(specificSearchResult)).getId();
        } else {
            id = itemDao.findByImdbId(imdbId).getId();
        }
        return id;
    }

}
