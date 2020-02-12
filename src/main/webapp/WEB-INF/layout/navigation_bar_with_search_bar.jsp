<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <ul class="navbar-nav mr-auto">
        <li class="nav-item active">
            <a class="nav-link" href="<c:url value='/' />">Home</a>
        </li>
        <li class="nav-item active">
            <a class="nav-link" href="<c:url value='/watch_list' />">Watch List</a>
        </li>
        <li class="nav-item active">
            <a class="nav-link" href="<c:url value='/friends_list' />">Friends List</a>
        </li>
        <sec:authorize access="isAnonymous()">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value='/register' />">Register</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value='/login' />">Login</a>
            </li>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value='/logout' />">Logout</a>
            </li>
        </sec:authorize>
    </ul>
    <form:form modelAttribute="searchString" action="searchBySubstring" method="post" class="form-inline my-2 my-lg-0">
        <form:input class="form-control mr-sm-2" path="searchSubstring" id="search_bar" placeholder="Search item or user" aria-label="Search" />
        <div class="btn-group">
            <input type="submit" value="Search item" name="itemSearch" class="btn btn-outline-info my-2 my-sm-0">
            <input type="submit" value="Search user" name="userSearch" class="btn btn-outline-info my-2 my-sm-0">
        </div>
    </form:form>
</nav>