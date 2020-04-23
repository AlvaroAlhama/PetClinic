<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">

    <h2>Información de la causa</h2>

	<table class="table table-striped">
		<tr>
			<th>Nombre</th>
			<td><c:out value="${cause.name}"/></td>
		</tr>
		<tr>
			<th>Descripción</th>
			<td><c:out value="${cause.description}" /></td>
		</tr>
		<tr>
			<th>Fecha límite</th>
			<td><c:out value="${cause.endDate}" /></td>
		</tr>
		<tr>
			<th>Objetivo</th>
			<td><c:out value="${cause.objetive}" /></td>
		</tr>
	</table>
	
	<c:if test="${causeAmount != 0}">
		<h3>Donaciones</h3>
		<table id="donationTable" class="table table-striped">
			<thead>
				<tr>
					<th style="width: 150px;">Date</th>
		             <th style="width: 200px;">Amount</th>
		             <th style="width: 200px">Client</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cause.donations}" var="donation">
					<tr>
						<td><c:out value="${donation.date}"/></td>  
                    	<td><c:out value="${donation.amount}"/> (EUR)</td> 
                    	<td><c:out value="${donation.client}"/></td> 
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	
	<c:if test="${causeAmount == 0}">
		<h3>Esta causa todavía no tiene donaciones.</h3>
	</c:if>
	
	<c:if test="${causeAmount >= cause.objetive}">
		<h3>Esta causa ya ha llegado a su objetivo!</h3>
	</c:if>
	
	<c:if test="${causeAmount < cause.objetive}">
		<form action="/cause/${cause.id}/donations/new">
			<button type="submit" class="btn btn-default">Dona!</button>
		</form>
	</c:if>
	
</petclinic:layout>
