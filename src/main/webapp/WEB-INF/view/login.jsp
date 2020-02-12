<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h2>Login</h2>

<form:form modelAttribute="loginRequest" action="login">
    <div class="form-group row">
        <form:errors path="*" element="div" class="alert alert-danger" role="alert" />
    </div>
    <div class="form-group row">
        <div class="col-sm-1">
            <label for="username">Username</label>
        </div>
        <div class="col-sm-3">
            <form:input path="username" id="username" placeholder="Username" class="form-control" />
        </div>
    </div>
    <div class="form-group row">
        <div class="col-sm-1">
            <label for="password">Password</label>
        </div>
        <div class="col-sm-3">
            <form:password path="password" id="password" placeholder="Password" class="form-control" />
        </div>
    </div>
    <div class="form-row">
        <button type="submit" class="btn btn-info">Login</button>
    </div>
</form:form>