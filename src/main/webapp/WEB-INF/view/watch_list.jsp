<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
    <c:when test="${empty user}">
        <h2>Your list</h2>
        <c:choose>
            <c:when test="${empty items}">
                <p>List is empty</p>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <tr>
                        <c:choose>
                            <c:when test="${sortBy == 'item.title' && sortDirection == 'ASC'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.title,DESC' />">Title</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.title,ASC' />">Title</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'rating' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=rating,DESC&sort=item.title,ASC' />">Rating</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=rating,ASC&sort=item.title,ASC' />">Rating</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'item.year' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.year,DESC&sort=item.title,ASC' />">Release date</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.year,ASC&sort=item.title,ASC' />">Release date</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'item.imdbRating' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.imdbRating,DESC&sort=item.title,ASC' />">Imdb rating</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.imdbRating,ASC&sort=item.title,ASC' />">Imdb rating</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'item.type' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.type,DESC&sort=item.title,ASC' />">Type</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?page=${current}&sort=item.type,ASC&sort=item.title,ASC' />">Type</a></th>
                            </c:otherwise>
                        </c:choose>
                        <th>Action</th>
                    </tr>
                    <c:forEach var="watched" items="${items}">
                        <tr>
                            <td><a href="<c:url value='/details?itemId=${watched.item.id}' />">${watched.item.title}</a></td>
                            <td>${watched.rating}</td>
                            <td>${watched.item.year}</td>
                            <td>${watched.item.imdbRating}</td>
                            <td>${watched.item.type}</td>
                            <td><a href="<c:url value='/deleteWatched?watchedId=${watched.id}&page=${current}' />">Delete</a></td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="row">
                    <nav>
                        <ul class="pagination">
                            <li class="page-item ${previous == current ? 'disabled' : ''}"><a class="page-link" href="<c:url value='/watch_list?page=${previous}' />">Previous</a></li>
                            <c:if test="${previous-1 ge 0}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?page=${previous-1}' />">${previous-1}</a></li>
                            </c:if>
                            <c:if test="${previous lt current}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?page=${previous}' />">${previous}</a></li>
                            </c:if>
                            <li class="page-item active"><a class="page-link" href="#">${current}</a></li>
                            <c:if test="${next lt totalPages}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?page=${next}' />">${next}</a></li>
                            </c:if>
                            <c:if test="${next+1 lt totalPages}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?page=${next+1}' />">${next+1}</a></li>
                            </c:if>
                            <c:if test="${next+2 lt totalPages}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?page=${next+2}' />">${next+2}</a></li>
                            </c:if>
                            <li class="page-item ${next == totalPages ? 'disabled' : ''}"><a class="page-link" href="<c:url value='/watch_list?page=${next lt totalPages ? next : current}' />">Next</a></li>
                        </ul>
                    </nav>
                </div>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <h2>${user.userName}&#39;s list</h2>
        <c:choose>
            <c:when test="${isFriend}">
                <a href="<c:url value='/deleteFriend?userId=${user.id}' />">Delete friend</a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value='/addFriend?userId=${user.id}' />">Add friend</a>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${empty items}">
                <p>List is empty</p>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <tr>
                        <c:choose>
                            <c:when test="${sortBy == 'item.title' && sortDirection == 'ASC'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.title,DESC' />">Title</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.title,ASC' />">Title</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'rating' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=rating,DESC&sort=item.title,ASC' />">Rating</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=rating,ASC&sort=item.title,ASC' />">Rating</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'item.year' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.year,DESC&sort=item.title,ASC' />">Release date</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.year,ASC&sort=item.title,ASC' />">Release date</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'item.imdbRating' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.imdbRating,DESC&sort=item.title,ASC' />">Imdb rating</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.imdbRating,ASC&sort=item.title,ASC' />">Imdb rating</a></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${sortBy == 'item.type' && sortDirection == 'ASC,item.title'}">
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.type,DESC&sort=item.title,ASC' />">Type</a></th>
                            </c:when>
                            <c:otherwise>
                                <th><a class="btn btn-link" href="<c:url value='/watch_list?userId=${user.id}&page=${current}&sort=item.type,ASC&sort=item.title,ASC' />">Type</a></th>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <c:forEach var="watched" items="${items}">
                        <tr>
                            <td><a href="<c:url value='/details?itemId=${watched.item.id}' />">${watched.item.title}</a></td>
                            <td>${watched.rating}</td>
                            <td>${watched.item.year}</td>
                            <td>${watched.item.imdbRating}</td>
                            <td>${watched.item.type}</td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="row">
                    <nav>
                        <ul class="pagination">
                            <li class="page-item ${previous == current ? 'disabled' : ''}"><a class="page-link" href="<c:url value='/watch_list?userId=${user.id}&page=${previous}' />">Previous</a></li>
                            <c:if test="${previous-1 ge 0}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?userId=${user.id}&page=${previous-1}' />">${previous-1}</a></li>
                            </c:if>
                            <c:if test="${previous lt current}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?userId=${user.id}&page=${previous}' />">${previous}</a></li>
                            </c:if>
                            <li class="page-item active"><a class="page-link" href="#">${current}</a></li>
                            <c:if test="${next lt totalPages}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?userId=${user.id}&page=${next}' />">${next}</a></li>
                            </c:if>
                            <c:if test="${next+1 lt totalPages}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?userId=${user.id}&page=${next+1}' />">${next+1}</a></li>
                            </c:if>
                            <c:if test="${next+2 lt totalPages}">
                                <li class="page-item"><a class="page-link" href="<c:url value='/watch_list?userId=${user.id}&page=${next+2}' />">${next+2}</a></li>
                            </c:if>
                            <li class="page-item ${next == totalPages ? 'disabled' : ''}"><a class="page-link" href="<c:url value='/watch_list?userId=${user.id}&page=${next lt totalPages ? next : current}' />">Next</a></li>
                        </ul>
                    </nav>
                </div>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
