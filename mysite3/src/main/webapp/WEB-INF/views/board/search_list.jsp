<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>  
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.request.contextPath}/board/search" method="post">
					<input type="text" id="kwd" name="kwd" value="">
					<input type="submit" value="찾기">
				</form>
				
				<c:set var="count" value="${fn:length(list)}" />

					
    					<table class="tbl-ex">
    						<tr>
								<th>번호</th>
								<th>제목</th>
								<th>글쓴이</th>
								<th>조회수</th>
								<th>작성일</th>
								<th>&nbsp;</th>
							</tr>
							<c:forEach var="vo" items="${list}">
    						<tr>
								<td>[${vo.getNumbering() }]</td>
								<td style="text-align:left; padding-left:${20*vo.depth }px">
									<c:set var="depth" value="${vo.depth }" />
									<c:if test="${vo.depth > 0}">    
										<img src='${pageContext.request.contextPath }/assets/images/reply.png'>
									</c:if>
									<a href="${pageContext.request.contextPath}/board/view&no=${vo.getNo() }">${vo.getTitle() }</a>
								</td>
								<td>${vo.getUserName() }</td>
								<td>${vo.getHit() }</td>
								<td>${vo.getRegDate() }</td>
								<td><a href="${pageContext.request.contextPath}/board/delete&no=${vo.getNo() }" class="del">삭제</a></td>
							</tr>
							</c:forEach>
						</table>
						<br>				
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>   		
                    <c:if test="${pageNum != pageVo.getStartPage() }">
                        <li><a href="${pageContext.request.contextPath}/board/search?kwd=${kwd }&pageNum=${pageNum - 1 }&amount=${pageVo.getAmount()}">◀</a></li>
                    </c:if>
					<c:forEach var="num" begin="${pageVo.getStartPage() }" end="${pageVo.getEndPage() }">
	                      <li  class="${pageVo.getPageNum() eq num ? 'selected' : '' }"> 
	                      <a href="${pageContext.request.contextPath}/board/search?kwd=${kwd }&pageNum=${num }&amount=${pageVo.getAmount()}">${num }</a></li>
                 	</c:forEach>
					<c:if test="${pageNum != pageVo.getEndPage() }">
                        <li><a href="${pageContext.request.contextPath}/board/search?kwd=${kwd }&pageNum=${pageNum + 1 }&amount=${pageVo.getAmount()}">▶</a></li>
                    </c:if>
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				<div class="bottom">
					<a href="${pageContext.request.contextPath}/board/write" id="new-book">글쓰기</a>
				</div>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>
<c:if test='${param.writeSuccess == "success" }'>
	<script>alert('성공적으로 작성 하였습니다.')</script>
</c:if>
<c:if test='${param.writeSuccess == "failed" }'>
	<script>alert('회원만 작성할 수 있습니다.')</script>
</c:if>
<c:if test='${param.deleteSuccess == "success" }'>
	<script>alert('성공적으로 삭제 하였습니다.')</script>
</c:if>
<c:if test='${param.deleteSuccess == "failed" }'>
	<script>alert('글 작성자만 삭제할 수 있습니다.')</script>
</c:if>
<c:if test='${param.modifySuccess == "success" }'>
	<script>alert('성공적으로 수정 하였습니다.')</script>
</c:if>
<c:if test='${param.modifySuccess == "failed" }'>
	<script>alert('글 작성자만 수정할 수 있습니다.')</script>
</c:if>