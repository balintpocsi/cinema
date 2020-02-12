<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<img src="${item.posterPath}" alt="Poster">
<h2>${item.title}</h2>
<p>${item.description}</p>
<p>Type: ${item.type}</p>
<p>Release date: ${item.year}</p>
<p>Genres: ${item.genre}</p>
<p>Runtime: ${item.runtime}</p>
<p>imdb rating: ${item.imdbRating}</p>

<form:form modelAttribute="addToWatchListItem" action="addToWatchList" method="post" class="form-inline">
    <div class="form-group">
        <form:hidden path="itemId" value="${item.id}" />
        <label for="rating">Rate it</label>
        <form:select path="rating" id="rating" class="form-control mx-sm-2">
            <form:option value="1" label="1"/>
            <form:option value="2" label="2"/>
            <form:option value="3" label="3"/>
            <form:option value="4" label="4"/>
            <form:option value="5" label="5"/>
            <form:option value="6" label="6"/>
            <form:option value="7" label="7"/>
            <form:option value="8" label="8"/>
            <form:option value="9" label="9"/>
            <form:option value="10" label="10"/>
        </form:select>
        <button type="submit" class="btn btn-info">Add to list</button>
    </div>
</form:form>