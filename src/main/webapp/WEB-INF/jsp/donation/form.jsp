<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="causes">
        <h2><c:if test="${donation['new']}">Nueva </c:if>donación</h2>

        <b>Causa</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Nombre</th>
                <th>Descripción</th>
                <th>Fecha límite</th>
                <th>Objetivo</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${donation.cause.name}"/></td>
                <td><c:out value="${donation.cause.description}"/></td>
                <td><petclinic:localDate date="${donation.cause.endDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${donation.cause.objetive}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="donation" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Cantidad (EUR)" name="amount"/>
                <petclinic:inputField label="Cliente" name="client"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="causeId" value="${donation.cause.id}"/>
                    <button class="btn btn-default" type="submit">Anadir donación</button>
                </div>
            </div>
        </form:form>

</petclinic:layout>
