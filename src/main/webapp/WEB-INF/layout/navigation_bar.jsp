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
</nav>