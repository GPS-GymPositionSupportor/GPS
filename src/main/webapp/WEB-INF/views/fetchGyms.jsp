<%@page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%
    List<Map<String, Object>> gyms = new ArrayList<>();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        // 사용자 위치 정보 가져오기
        double userLat = Double.parseDouble(request.getParameter("lat"));
        double userLng = Double.parseDouble(request.getParameter("lng"));

        // DB 연결 설정
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gps", "root", "rhkdgml0421");

        // SQL 쿼리 (3km 이내의 헬스장)
        String sql = "SELECT g_name, g_latitude, g_longitude FROM gym " +
                     "WHERE (6371 * acos(cos(radians(?)) * cos(radians(g_latitude)) * cos(radians(g_longitude) - radians(?)) + sin(radians(?)) * sin(radians(g_latitude)))) < 3";

        pstmt = conn.prepareStatement(sql);
        pstmt.setDouble(1, userLat);
        pstmt.setDouble(2, userLng);
        pstmt.setDouble(3, userLat);

        // 실행
        rs = pstmt.executeQuery();

        while (rs.next()) {
            Map<String, Object> gym = new HashMap<>();
            gym.put("g_name", rs.getString("g_name"));
            gym.put("g_latitude", rs.getDouble("g_latitude"));
            gym.put("g_longitude", rs.getDouble("g_longitude"));
            gyms.add(gym);
        }

        // JSON 형식으로 응답
        response.setContentType("application/json");
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("[");
        boolean first = true;

        for (Map<String, Object> gym : gyms) {
            if (!first) {
                jsonResponse.append(",");
            }
            jsonResponse.append("{")
                        .append("\"g_name\":\"").append(gym.get("g_name")).append("\",")
                        .append("\"g_latitude\":").append(gym.get("g_latitude")).append(",")
                        .append("\"g_longitude\":").append(gym.get("g_longitude"))
                        .append("}");
            first = false;
        }

        jsonResponse.append("]");
        response.getWriter().write(jsonResponse.toString());

    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류");
    } finally {
        // 자원 해제
        if (rs != null) try { rs.close(); } catch (SQLException e) {}
        if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
        if (conn != null) try { conn.close(); } catch (SQLException e) {}
    }
%>