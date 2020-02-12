<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<h2>Add a new movie or series from imdb</h2>

<form:form modelAttribute="uploadItem" action="uploadItemPost">
    <div class="form-group row">
        <div class="col-sm-2">
            <label for="input_title_or_url">Imdb title or URL</label>
        </div>
        <div class="col-sm-10">
            <form:input path="titleOrURL" id="input_title_or_url" class="form-control" value="${substring}" />
        </div>
    </div>
    <div class="form-group row">
        <form:errors path="*" element="div" class="alert alert-danger" role="alert" />
    </div>
    <c:if test="${notSpecificEnough}">
        <div class="form-group row">
            <a class="btn btn-info" href="<c:url value='/confirmUpload' />">Confirm</a>
        </div>
    </c:if>
    <div class="form-group row">
        <div class="col-sm-1">
            <label for="movie">Movie</label>
        </div>
        <div class="col-xs-1">
            <form:radiobutton path="type" value="movie" id="movie" checked="checked"/>
        </div>
    </div>
    <div class="form-group row">
        <div class="col-sm-1">
            <label for="series">Series</label>
        </div>
        <div class="col-xs-1">
            <form:radiobutton path="type" value="series" id="series"/>
        </div>
    </div>
    <div class="form-row">
        <button type="submit" class="btn btn-info">Add</button>
    </div>
</form:form>