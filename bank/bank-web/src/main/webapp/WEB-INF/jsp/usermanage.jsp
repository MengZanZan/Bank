<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>名单管理</title>

<style type="text/css">
th,td{width: 150px;border: 2px solid gray;text-align: center;}
body{text-align: center;}
a{text-decoration: none;}
table {border-collapse: collapse;}
</style>

</head>
<body>



 <form action="${pageContext.request.contextPath }/list/usershow.action" method="post">
 
 <input type="hidden" name="userid_t"  value="${userid_t}"/>
 <input type="hidden" name="selectusertype"  value="${selectusertype}"/>
 
<h2 align="center">用户信息</h2>
<table align="center">
<tr><td>用户id</td><td>用户名</td><td>用户密码</td><td>用户类型</td><td>操作</td></tr>
</table>
<table align="center">
<c:forEach items="${listss}" var="user">
<tr>
<td class="hidden-480">${user.getId()}</td>
<td class="hidden-480">${user.getUsername() }</td>
<td class="hidden-480">${user.getPassword()}</td>
<td class="hidden-480">
<c:choose>
<c:when test="${user.getUsertype() == 0}">黑名单</c:when>
<c:when test="${user.getUsertype() == 1}">白名单</c:when>
<c:when test="${user.getUsertype() == 2}">灰名单</c:when>
<c:when test="${user.getUsertype() == 3}">高风险账户</c:when>
<c:when test="${user.getUsertype() == 4}">高风险ip</c:when>
<c:when test="${user.getUsertype() == 5}">高风险手机号</c:when>
</c:choose>
</td>
<td class="hidden-480"><a href="${pageContext.request.contextPath }/list/userupdate/${user.id}">修改</a></td>
</tr>
</c:forEach>
</table>
<br>
<c:if test="${pageNos>1 }">
<a href="${pageContext.request.contextPath }/list/usershow.action?pageNos=1&userid=${userid_t}&usertype=${selectusertype}" >首页</a>
<a href="${pageContext.request.contextPath }/list/usershow.action?pageNos=${pageNos-1 }&userid=${userid_t}&usertype=${selectusertype}">上一页</a>
</c:if>
<c:if test="${pageNos <recordCount }">
<a href="${pageContext.request.contextPath }/list/usershow.action?pageNos=${pageNos+1 }&userid=${userid_t}&usertype=${selectusertype}">下一页</a>
<a href="${pageContext.request.contextPath }/list/usershow.action?pageNos=${recordCount }&userid=${userid_t}&usertype=${selectusertype}">末页</a>
</c:if>

<h4 align="center">共${recordCount}页  
<input type="text" value="${pageNos}" name="pageNos" size="1" onkeyup="value=value.replace(/[^\d]/g,'')">页
<input type="submit" value="到达">
</h4>


条件选择:<br/>


用户id:<input type="text" name="userid"  onkeyup="value=value.replace(/[^\d]/g,'')" value="${userid_t}" /> &nbsp;&nbsp;-&nbsp;&nbsp; 



用户类型:<select id="usertype" name="usertype" >
<option value = "0"  <c:if test='${selectusertype==0}'>selected="selected"</c:if>>黑名单</option>
<option value = "1"  <c:if test='${selectusertype==1}'>selected="selected"</c:if>>白名单</option>
<option value = "2"  <c:if test='${selectusertype==2}'>selected="selected"</c:if>>灰名单</option>
<option value = "3"  <c:if test='${selectusertype==3}'>selected="selected"</c:if>>高风险账户</option>
<option value = "4"  <c:if test='${selectusertype==4}'>selected="selected"</c:if>>高风险ip</option>
<option value = "5"  <c:if test='${selectusertype==5}'>selected="selected"</c:if>>高风险手机号</option>
<option value = "7"  <c:if test='${selectusertype==7}'>selected="selected"</c:if>>全部</option>
</select>

<br/>

<input type="submit" value="搜索">

</form>

<table align="center">
<tr><td>总用户数</td><td>黑名单用户数</td><td>白名单用户数</td><td>灰名单用户数</td><td>高风险用户数</td></tr>
</table>

<table align="center">
<tr>
<td class="hidden-480">${user_total} </td>
<td class="hidden-480">${blackuser} </td>
<td class="hidden-480">${whiteuser} </td>
<td class="hidden-480">${grayuser} </td>
<td class="hidden-480">${user_danger} </td>

</tr>
</table>



<a href="/">返回首页</a>




</body>
</html>