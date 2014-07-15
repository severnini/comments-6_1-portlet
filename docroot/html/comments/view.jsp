<%@ include file="/html/comments/init.jsp" %>

<%

int delta = GetterUtil.getInteger(portletPreferences.getValue(SearchContainer.DEFAULT_DELTA_PARAM, StringPool.BLANK), SearchContainer.DEFAULT_DELTA);
String paginationType = GetterUtil.getString(portletPreferences.getValue("paginationType", "regular"));

%>

<aui:column columnWidth="100" cssClass="resultado-busca">
	<liferay-ui:search-container searchContainer="${sc}" emptyResultsMessage="no-records-were-found">
		
		<liferay-ui:search-container-results results="${commentList}" total="${commentCount}" />
	
		<liferay-ui:search-container-row className="com.liferay.portlet.messageboards.model.MBMessage" modelVar="mbMessage" keyProperty="messageId">
			<%@ include file="/html/comments/_comment_summary.jspf" %>
		</liferay-ui:search-container-row>
		
	</liferay-ui:search-container>
	
	<liferay-ui:search-iterator paginate="true" searchContainer="${sc}" type="<%=paginationType%>" />
	
</aui:column>
