<%@ include file="/html/comments/init.jsp" %>
<%

int delta = GetterUtil.getInteger(portletPreferences.getValue("delta", StringPool.BLANK), SearchContainer.DEFAULT_DELTA);
String paginationType = GetterUtil.getString(portletPreferences.getValue("paginationType", "none"));

%>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationActionURL" />
<liferay-portlet:renderURL portletConfiguration="true" varImpl="configurationRenderURL" />

<liferay-ui:header
	backURL="<%=themeDisplay.getURLCurrent().replace(PortletMode.EDIT.toString(), PortletMode.VIEW.toString())%>"	
	title='Configuração das preferências'
/>

<form action="<%=configurationActionURL%>" method="post" name="<portlet:namespace />fm">
	<input name="<portlet:namespace /><%=Constants.CMD%>" type="hidden" value="<%=Constants.UPDATE%>" />
	<aui:input name="<portlet:namespace />redirect" type="hidden" value="<%=redirect%>" />

	<aui:fieldset cssClass="general-display-settings">
	
		<aui:select label="maximum-items-to-display" name="preferences--delta--">
	
			<%
			int[] deltas = {1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100};
	
			for (int currentDelta: deltas) {
			%>
	
				<aui:option label="<%= currentDelta %>" selected="<%= (delta == currentDelta) %>" />
	
			<%
			}
			%>
		</aui:select>
	
		<aui:select label="pagination-type" name="preferences--paginationType--">
			<aui:option label="none" selected='<%= paginationType.equals("none") %>' />
			<aui:option label="simple" selected='<%= paginationType.equals("simple") %>' />
			<aui:option label="regular" selected='<%= paginationType.equals("regular") %>' />
		</aui:select>
	</aui:fieldset>

</form>