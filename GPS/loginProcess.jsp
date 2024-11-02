<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="javax.servlet.*"%>
<%@page import="javax.servlet.http.*"%>
<%@ include file="DBC.jsp" %> <!-- DBC.jsp 파일 포함 -->
<%
    String m_id = request.getParameter("username"); // m_id는 사용자 이름 입력 필드와 연결
    String m_password = request.getParameter("password"); // m_password는 비밀번호 입력 필드와 연결

    try {
        // SQL 쿼리 준비
        String sql = "SELECT * FROM member WHERE m_id = ? AND m_password = ?";
        pst = conn.prepareStatement(sql); // DBC.jsp에서 선언한 pst 사용
        pst.setString(1, m_id);
        pst.setString(2, m_password);
        rs = pst.executeQuery();

        // 로그인 검증
        if (rs.next()) {
            // 로그인 성공, 세션에 사용자 ID 저장
            session.setAttribute("userID", rs.getString("m_id")); // m_id를 세션에 저장
            session.setAttribute("userName", rs.getString("name")); // 이름도 세션에 저장
            session.setAttribute("authority", rs.getString("authority")); // 권한 정보도 저장
            response.sendRedirect("index.jsp"); // 로그인 성공 후 메인 페이지로 리다이렉트
        } else {
            // 로그인 실패
            session.setAttribute("loginError", "등록되지 않은 아이디이거나<br>잘못된 아이디 또는 비밀번호입니다.");
            response.sendRedirect("index.jsp");
        }
    } catch (Exception e) {
        e.printStackTrace();
        // 서버 오류 발생 시 세션에 오류 메시지 저장
        session.setAttribute("loginError", "일시적인 오류로 로그인할 수 없습니다.<br>잠시 후 다시 이용해 주세요.");
        response.sendRedirect("index.jsp"); // 오류 발생 시 로그인 페이지로 리다이렉트
    } finally {
        // 자원 해제
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            // conn.close(); // DBC.jsp에서 관리
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>