<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%
//String host = "jdbc:mariadb://localhost:3306/gps"; //mariadb
String host = "jdbc:mysql://localhost:3306/gps"; //mysql
String user = "root";		// 사용 시 개인 sql ID 확인
String password = "1234";	// 사용 시 개인 sql 비밀번호 확인

Connection conn = null; 
PreparedStatement pst = null;
ResultSet rs = null;

//jdbc-odbc driver를 등록한다. 
try {
	Class.forName("com.mysql.jdbc.Driver"); //mysql
	//Class.forName("org.mariadb.jdbc.Driver"); //mariadb
} catch (ClassNotFoundException e ) {
	out.println("jdbc-odbc driver : " + e);
	return;
}
//DB와 연결 
try{
	conn = DriverManager.getConnection(host, user, password);
} catch (SQLException e) {
	out.println("DB : " + e);
	return;
}
%>