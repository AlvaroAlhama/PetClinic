<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>


<petclinic:layout pageName="owners">
	<jsp:attribute name="customScript">
        <script>
									$(function() {
										$("#registerDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
										$("#releaseDate").datepicker({
											dateFormat : 'yy/mm/dd'
										});
									});
								</script>
    </jsp:attribute>
	<jsp:body>
        <h2>
			<c:if test="${residence['new']}">Nueva </c:if>residencia</h2>

        <b>Mascota</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Nombre</th>
                <th>Fecha de nacimiento</th>
                <th>Tipo</th>
                <th>Propietario</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${residence.pet.name}" /></td>
                <td><petclinic:localDate
						date="${residence.pet.birthDate}" pattern="yyyy/MM/dd" /></td>
                <td><c:out value="${residence.pet.type.name}" /></td>
                <td><c:out
						value="${residence.pet.owner.firstName} ${residence.pet.owner.lastName}" /></td>
            </tr>
        </table>

        <form:form modelAttribute="residence" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Fecha registro"
					name="registerDate" />
                <petclinic:inputField label="Fecha salida"
					name="releaseDate" />
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId"
						value="${residence.pet.id}" />
                    <button class="btn btn-default" type="submit">Anadir alojamiento</button>
                </div>
            </div>
        </form:form>

        <br />
        <b>Alojamientos previos</b>
        <table class="table table-striped">
            <tr>
                <th>Fecha registro</th>
                <th>Fecha salida</th>
            </tr>
            <c:forEach var="residence"
				items="${residence.pet.residences}">
                <c:if test="${!residence['new']}">
                    <tr>
                        <td><petclinic:localDate
								date="${residence.registerDate}" pattern="yyyy/MM/dd" /></td>
                        <td><petclinic:localDate
								date="${residence.releaseDate}" pattern="yyyy/MM/dd" /></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>
