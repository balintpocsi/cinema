package com.thesis.recommenderapp.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.thesis.recommenderapp.domain.UploadItemRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImdbAPIGetService {

    private OkHttpClient okHttpClient = new OkHttpClient();

    public String getGeneralSearchResults(UploadItemRequest uploadItemRequest) throws IOException {
        Request request = new Request.Builder()
            .url(createGeneralSearchURL(uploadItemRequest))
            .get()
            .addHeader("x-rapidapi-host", "movie-database-imdb-alternative.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "2dc4d4daedmshd5b3479ae310a95p1efc8fjsn62e3e5e0ad7e")
            .build();

        return okHttpClient.newCall(request).execute().body().string();
    }

    public String getSpecificSearchResults(String imdbId) throws IOException {
        Request request = new Request.Builder()
            .url(createSpecificSearchURL(imdbId))
            .get()
            .addHeader("x-rapidapi-host", "movie-database-imdb-alternative.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "2dc4d4daedmshd5b3479ae310a95p1efc8fjsn62e3e5e0ad7e")
            .build();

        return okHttpClient.newCall(request).execute().body().string();
    }

    private String createGeneralSearchURL(UploadItemRequest uploadItemRequest) {
        String type = uploadItemRequest.getType();
        String title = transform(uploadItemRequest.getTitleOrURL());
        return String.format("https://movie-database-imdb-alternative.p.rapidapi.com/?page=1&r=json&type=%s&s=%s", type, title);
    }

    private String createSpecificSearchURL(String imdbId) {
        return String.format("https://movie-database-imdb-alternative.p.rapidapi.com/?r=json&i=%s", imdbId);
    }

    private String transform(String title) {
        return String.join("%20", title.split(" "));
    }

}
