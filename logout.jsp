<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    session.removeAttribute("userID"); // 세션에 저장된 사용자 ID를 삭제합니다.
    response.sendRedirect("index.jsp"); // 루트 페이지로 리다이렉트합니다.
%>