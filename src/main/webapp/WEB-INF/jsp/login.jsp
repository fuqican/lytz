<%@ include file="/WEB-INF/jsp/includes/common.jspf"%>
<html>
<head>
<%@ include file="/WEB-INF/jsp/includes/header.jspf"%>
<title><fmt:message key="webapp.name"/></title>
</head>
<body>

    <div class="container  col-md-12 lytz_spacer">

<div class="modal fade" id="signinModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4>
                    <span class="glyphicon glyphicon-lock"></span>
                    <fmt:message key="login.heading" />
                </h4>
                <c:if test="${not empty shiroLoginFailure}">
                    <c:set var="errormsg" value="${shiroLoginFailure}"/>
                    <c:choose>
                        <c:when test="${fn:contains(errormsg,'IncorrectCredentialsException')}">
                            <fmt:message key="errors.password.mismatch" />
                        </c:when>
                        <c:otherwise>
                            <c:out value="${shiroLoginFailure}" />
                        </c:otherwise>
                    </c:choose>
                    
                </c:if>
            </div>
            <div class="modal-body">
                <form method="post" action="${ctx}/login" role="form"
                    accept-charset="UTF-8">
                    <div class="form-group">
                        <label for="usrname"><span
                            class="glyphicon glyphicon-user"></span>
                        <fmt:message key="label.username" /></label> <input
                            class="form-control" type="text"
                            placeholder="Username" id="username"
                            name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="password"><span
                            class="glyphicon glyphicon-eye-open"></span>
                        <fmt:message key="label.password" /> </label> <input
                            class="form-control" type="password"
                            placeholder="Password" id="password"
                            name="password" required>
                    </div>
                    <div class="form-group">
                        <img src=""
                            onclick="document.getElementById('signin_img').src = '${ctx}/kaptcha.jpg?signin=' + Math.random(); 
                       return false"
                            id="signin_img" class="img-thumbnail"
                            alt="click click image to refresh if u can't recognize the content" />
                        <input type="text" name="captcha"
                            class="form-control" placeholder="Captcha"
                            name="captcha" required>
                    </div>
                    <div class="checkbox">
                        <label><input type="checkbox" name="rememberMe" value="true"
                            checked>
                        <fmt:message key="login.rememberMe" /></label>
                    </div>
                    <button type="submit"
                        class="btn btn-success btn-block">
                        <span class="glyphicon glyphicon-off"></span>
                        <fmt:message key="button.login" />
                    </button>
                </form>
            </div>
            <div class="modal-footer">
                <%-- <button type="submit"
                    class="btn btn-danger btn-default pull-left"
                    data-dismiss="modal">
                    <span class="glyphicon glyphicon-remove"></span>
                    <fmt:message key="button.cancel" />
                </button> --%>
                <p>
                    <fmt:message key="login.signup" />
                </p>
                <p>
                    <a href="#">Sign In with QQ</a>
                </p>
                <p>
                    <a href="#">Sign In with WeChat</a>
                </p>
                <p>
                    <fmt:message key="login.passwordHint" />
                </p>
            </div>
        </div>

    </div>
</div>
    </div>
<script>
    $("#signin_img").attr("src", '${ctx}/kaptcha.jpg?signin=' + Math.random());
    $("#signinModal").modal();
</script>
    
</body>

</html>