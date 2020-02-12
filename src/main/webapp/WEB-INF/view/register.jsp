<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h2>Registration</h2>

<form:form modelAttribute="registerUser" action="registerUserPost">
    <div class="form-group row">
        <form:errors path="*" element="div" class="alert alert-danger" role="alert" />
    </div>
    <div class="form-group row">
        <div class="col-sm-1">
            <label for="input_username">Username</label>
        </div>
        <div class="col-sm-3">
            <form:input path="userName" id="input_username" class="form-control" />
        </div>
    </div>
    <div class="form-group row">
        <div class="col-sm-1">
            <label for="input_password">Password</label>
        </div>
        <div class="col-sm-3">
            <form:password path="password" id="input_password" class="form-control" />
        </div>
    </div>
    <div class="form-row">
        <button type="submit" class="btn btn-info">Register</button>
    </div>
</form:form>