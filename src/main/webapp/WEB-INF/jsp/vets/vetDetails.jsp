<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">

    <h2>Informacion del veterinario</h2>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${vet.firstName} ${vet.lastName}"/></b></td>
        </tr>
    </table>
    
    <spring:url value="{vetId}/edit" var="editUrl">
        <spring:param name="vetId" value="${vet.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Veterinario</a>
    <spring:url value="{vetId}/delete" var="deleteUrl">
        <spring:param name="vetId" value="${vet.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar Veterinario</a>

    <br/>
    <br/>
    <br/>
    <h2>Especialidades</h2>

    <table class="table table-striped">
        <c:forEach var="specialty" items="${vet.specialties}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Nombre</dt>
                        <dd><c:out value="${specialty.name}"/></dd>
                    </dl>
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>
