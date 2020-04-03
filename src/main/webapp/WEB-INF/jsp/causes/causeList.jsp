<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="petTypes">
    <h2>Causes</h2>

    <table id="causeTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${causes}" var="cause">
            <tr>
                <td>
                    <c:out value="${cause.name}"/>
                </td>                
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form action="/cause/new">
        <button type="submit" class="btn btn-default">Add a cause</button>
    </form>
</petclinic:layout>