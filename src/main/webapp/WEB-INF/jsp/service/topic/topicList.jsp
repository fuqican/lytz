<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>资讯信息</title>
</head>

<body>
    <div class="panel panel-info">
        <div class="panel-heading clearfix">
            <h4 class="panel-title pull-left">资讯</h4>
                <a href="${ctx}/topic/new" title="添加"><span
                            class="glyphicon glyphicon-plus" ></span></a> 
            <c:if test="${not empty message}">
                <div id="message" class="alert alert-success">
                    <button data-dismiss="alert" class="close">×</button>${message}</div>
            </c:if>
            
            <div class="btn-group pull-right">
                <form:form class="form-inline pull-right" action="${ctx}/topic" accept-charset="UTF-8" modelAttribute="topicQuery" method="GET">
                  <div class="form-group">
                    <label class="sr-only" for="searchText">关键字</label>
                    <input type="text" class="form-control" id="searchText" name="keyword" placeholder="关键字" value="${topicQuery.keyword}"/>
                  </div>
                  <div class="form-group">
                    <label class="sr-only" for="status">显示数目</label>
                    <input type="text" class="form-control" id="querySize" name="querySize" placeholder="显示数目" value="${topicQuery.querySize}"/>
                  </div>
                  <shiro:hasRole name="ROLE_ADMIN">
                      <div class="form-group">
                        <label for="status">状态</label>
                        <form:select class="form-control" id="status" path="status">
                            <form:options items="${topicStatus }" />
                        </form:select>
                      </div>
                  </shiro:hasRole>
                  <button type="submit" class="btn btn-default">查询</button>
                </form:form>
            </div>
            
        </div>
        <div class="panel-body">
            <ul class="list-group">
                <c:forEach items="${topics}" var="topic">
                    <li class="list-group-item">
                                <a class="badge"
                                    href="${ctx}/topic/delete/${topic.id}"><span
                                    class="glyphicon glyphicon-minus">删除</span></a>
                                <a class="badge"
                                    href="${ctx}/topic/update/${topic.id}"><span
                                    class="glyphicon glyphicon-edit">修改</span></a>
                                <a href="${ctx}/topic/view/${topic.id}">${topic.title}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="panel-footer">
            <p class="pull-left">第 ${topicPager.currentPage } 页 共 ${topicPager.totalPages } 页</p>
            <ul class="pagination pull-right" style="margin: -2px">
                <li <c:if test="${not topicPager.previousExists}"> class="disabled"</c:if>><a href="${ctx}/topic/list?pageNum=${topicPager.firstPage }" aria-label="First"> <span
                        aria-hidden="true">&laquo;</span>
                </a></li>
                <c:if test="${topicPager.previousMore}"><li><a href="#">...</a></li></c:if>
                <c:forEach var="displayPageNum" items="${topicPager.displayPages}">
                    <c:choose>
                        <c:when test="${displayPageNum eq topicPager.currentPage }">
                            <li class="active"><a href="${ctx}/topic/list?pageNum=${topicPager.currentPage }">${topicPager.currentPage }<span class="sr-only">(current)</span></a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${ctx}/topic/list?pageNum=${displayPageNum}">${displayPageNum}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${topicPager.nextMore}"><li><a href="#">...</a></li></c:if>
                <li <c:if test="${not topicPager.nextExists}"> class="disabled"</c:if>><a href="${ctx}/topic/list?pageNum=${topicPager.lastPage }" aria-label="Last"> <span
                        aria-hidden="true">&raquo;</span>
                </a></li>
            </ul>
            <div class="clearfix"></div>
        </div>
    </div>
</body>
</html>