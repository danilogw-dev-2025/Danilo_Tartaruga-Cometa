<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // O Porteiro: Assim que alguém bater na porta principal (/trilha/),
    // ele manda direto para o menu.jsp
    response.sendRedirect("menu.jsp");
%>