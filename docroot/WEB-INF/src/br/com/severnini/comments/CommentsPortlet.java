package br.com.severnini.comments;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import br.com.severnini.comments.util.CommentsUtil;

import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Order;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class CommentsPortlet
 */
public class CommentsPortlet extends MVCPortlet {
 

	@Override
	public void doView(final RenderRequest renderRequest,
			final RenderResponse renderResponse) throws IOException,
			PortletException {

		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		long companyId = themeDisplay.getCompanyId();

		try {
			//create SearchContainer
			int cur = ParamUtil.get(renderRequest, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_CUR);
			int delta = ParamUtil.get(renderRequest, SearchContainer.DEFAULT_DELTA_PARAM, SearchContainer.DEFAULT_DELTA);
			String orderByCol = ParamUtil.getString(renderRequest, SearchContainer.DEFAULT_ORDER_BY_COL_PARAM, "modifiedDate");
			String orderByType = ParamUtil.getString(renderRequest, SearchContainer.DEFAULT_ORDER_BY_TYPE_PARAM, "desc");

			//paginator url
			PortletURL iteratorURL = renderResponse.createRenderURL();
			iteratorURL.setParameter(SearchContainer.DEFAULT_CUR_PARAM, String.valueOf(cur));
			iteratorURL.setParameter(SearchContainer.DEFAULT_ORDER_BY_COL_PARAM, orderByCol);
			iteratorURL.setParameter(SearchContainer.DEFAULT_ORDER_BY_TYPE_PARAM, orderByType);

			SearchContainer<MBMessage> searchContainer = new SearchContainer<MBMessage>(renderRequest, iteratorURL, null, "label-nenhum-resultado");
			searchContainer.setOrderByCol(orderByCol);
			searchContainer.setOrderByType(orderByType);
			searchContainer.setDelta(delta);

			//create DynamicQuery
			Criterion criterionClassNameId = PropertyFactoryUtil.forName("classNameId").in(CommentsUtil.availableClassNameIds());			
			Criterion criterionCompanyId = PropertyFactoryUtil.forName("companyId").eq(companyId);
			Criterion criterionParentMessageId = PropertyFactoryUtil.forName("parentMessageId").ne(0L);
			Criterion criterionCategoryId = PropertyFactoryUtil.forName("categoryId").eq(-1L);

			DynamicQuery dynamicQueryCount = DynamicQueryFactoryUtil.forClass(MBMessage.class, PortalClassLoaderUtil.getClassLoader());
			dynamicQueryCount.add(criterionClassNameId)
							 .add(criterionCompanyId)
							 .add(criterionParentMessageId)
							 .add(criterionCategoryId);

			DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(MBMessage.class, PortalClassLoaderUtil.getClassLoader());
			Order dqOrderBy = null;
			if ("asc".equalsIgnoreCase(orderByType)) {
				dqOrderBy = OrderFactoryUtil.asc(orderByCol);
			} else {
				dqOrderBy = OrderFactoryUtil.desc(orderByCol);
			}
			dynamicQuery.add(criterionClassNameId)			
						.add(criterionCompanyId)
						.add(criterionParentMessageId)
						.add(criterionCategoryId)
						.addOrder(dqOrderBy);

			//results
			@SuppressWarnings("unchecked")
			List<MBMessage> commentList = MBMessageLocalServiceUtil.dynamicQuery(dynamicQuery, searchContainer.getStart(), searchContainer.getEnd());
			long commentCount = MBMessageLocalServiceUtil.dynamicQueryCount(dynamicQueryCount);
			
			//storing results in request context
			renderRequest.setAttribute("commentList", commentList);
			renderRequest.setAttribute("commentCount", commentCount);
			renderRequest.setAttribute("sc", searchContainer);

		} catch (SystemException e) {
			_log.error(e);
		}
		
		super.doView(renderRequest, renderResponse);
	}	

	private final Log _log = LogFactoryUtil.getLog(CommentsPortlet.class);	

}
