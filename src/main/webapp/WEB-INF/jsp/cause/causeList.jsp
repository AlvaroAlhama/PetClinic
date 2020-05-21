<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="petTypes">
    <h2>Causas</h2>

    <table id="causeTable" class="table table-striped">
        <thead>
        <tr>
             <th style="width: 150px;">Nombre</th>
             <th style="width: 200px;">Descripcion</th>
             <th style="width: 120px">Fecha final</th>
             <th style="width: 120px">Objetivo</th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${causes}" var="cause">
            <tr>
            	<td>
	            	<spring:url value="/cause/{causeId}" var="showCauseUrl">
						<spring:param name="causeId" value="${cause.id}" />
					</spring:url>
					<a href="${fn:escapeXml(showCauseUrl)}">
		        		<c:out value="${cause.name}"/>
		        	</a>
		        </td>  
                <td><c:out value="${cause.description}"/> </td> 
                <td><c:out value="${cause.endDate}"/> </td> 
                <td><c:out value="${cause.objetive}"/> (EUR) </td>                
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form action="/cause/new">
        <button type="submit" class="btn btn-default">Añadir una causa</button>
    </form>
</petclinic:layout>