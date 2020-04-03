<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="causes">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#endDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>New cause</h2>
        <form:form action="/cause/new" modelAttribute="cause" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Enter name" name="name"/>
                <petclinic:inputField label="Enter description" name="description"/>
                <petclinic:inputField label="Enter objetive (€)" name="objetive"/>
                <petclinic:inputField label="Cause end date" name="endDate"/>
                
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Add cause</button>
                </div>
            </div>
        </form:form>
    </jsp:body>

</petclinic:layout>