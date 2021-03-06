<%@ page import="au.org.ala.volunteer.DarwinCoreField; au.org.ala.volunteer.Task; au.org.ala.volunteer.PicklistItem; au.org.ala.volunteer.Picklist" %>
<%
    def values = ["", "Metres", "Feet"]
    def picklist = Picklist.findByName(DarwinCoreField.measurementUnit.toString())
    if (picklist) {
        def items = PicklistItem.findAllByPicklistAndInstitutionCode(picklist, ((Task) taskInstance).project.picklistInstitutionCode ?: null)
        if (items) {
            values = [""] + items*.value
        }
    }
%>
<div class="unitRangeWidget control-group ${cssClass}" targetField="${field.fieldType}">
    <div class="row-fluid" style="vertical-align: bottom">

        %{--<div class="span2 muted">--}%
            %{--from--}%
        %{--</div>--}%
        <div class="span3">
            <g:textField tabindex="${tabindex}" name="${field.fieldType}.minValue" class="span12 rangeMinValue" value="" placeholder="From"/>
        </div>
        <div class="span1 muted" style="text-align: center">-
            </div>
        %{--<div class="span1 muted">--}%
            %{--to--}%
        %{--</div>--}%
        <div class="span3">
            <g:textField tabindex="${tabindex}" name="${field.fieldType}.maxValue" class="span12 rangeMaxValue" value="" placeholder="To"/>
        </div>
        <div class="span5">
            <g:select tabindex="${tabindex}" class="span12 rangeUnits" name="${field.fieldType}.units" value="" from="${values}" />
        </div>

    </div>
    <g:hiddenField id="recordValues.0.${field.fieldType}" name="recordValues.0.${field.fieldType}" value="${value}" validationRule="${field.validationRule}" />
</div>