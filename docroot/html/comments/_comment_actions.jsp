
<%@ include file="/html/comments/init.jsp" %>

<%      
ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
MBMessage mbMessage = (MBMessage) row.getObject();

//TODO build the link to the content
%>

<liferay-ui:icon-menu>
	<liferay-ui:icon
		message="label-view-content"
		image="view"
		target="_blank"
		url="#"
	/>
	<liferay-ui:icon
		message="label-view-comment-tree"
		image="view"
		target="_blank"
		url="#$"
	/>
	
</liferay-ui:icon-menu>
