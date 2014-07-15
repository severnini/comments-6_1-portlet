package br.com.severnini.comments.util;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;

/**
 *
 * Utility class
 *
 * @author Luiz Fernando Severnini <luizfernando.severnini@gmail.com>
 *
 */
public final class CommentsUtil {

	/**
	 *
	 * Get the {@link Group} by id, if not found, returns the default (global)
	 *
	 * @param groupId
	 * @return
	 */
	public static Group group(final long groupId) {
		try {
			Group group = GroupLocalServiceUtil.fetchGroup(groupId);

			if (group != null) {
				return group;
			}

			return GroupLocalServiceUtil.getCompanyGroup(PortalUtil.getDefaultCompanyId());

		} catch (SystemException e) {
			_log.error(e);
		} catch (PortalException e) {
			_log.error(e);
		}

		return null;
	}

	/**
	 *
	 * Group name
	 *
	 * @param groupId The group id
	 * @return
	 */
	public static String groupName(final long groupId) {
		Group group = group(groupId);

		if (group != null) {
			return group.getName();
		}

		try {
			return new String("grupo não encontrado".getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return StringPool.BLANK;
		}
	}

	/**
	 * 
	 * Asset title
	 * 
	 * @param message The {@link MBMessage}
	 * @param locale The {@link Locale}
	 * @return
	 */
	public static String assetTitle(final MBMessage message, final Locale locale) {
		
		String className = PortalUtil.getClassName(message.getClassNameId());
		long classPK = message.getClassPK();

		AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(className);

		if (assetRendererFactory == null) {
			if (Layout.class.getName().equals(className)) {
				try {
					Layout layout = LayoutLocalServiceUtil.fetchLayout(classPK);
					return layout.getName();
				} catch (SystemException e) {
					_log.error(e);
				}
			}
			return StringPool.BLANK;
		}

		AssetRenderer assetRenderer = null;

		try {
			assetRenderer = assetRendererFactory.getAssetRenderer(classPK);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		if ((assetRenderer == null) || !assetRenderer.isDisplayable()) {
			return StringPool.BLANK;
		}

		String title = assetRenderer.getTitle(locale);
		
		return title;
	}

//	/**
//	 * Returns true if a user can view a journal article
//	 * 
//	 * @param request
//	 * @param article
//	 * @return
//	 */
//	public static boolean userCanView(final PortletRequest request, final PermissionChecker permissionChecker, final JournalArticle article, final String actionId) {
//		boolean canAccess = false;
//
//		// forma 1
//		 try {
//			 MethodKey methodKey = new MethodKey("com.liferay.portlet.journal.service.permission.JournalArticlePermission",
//			 									 "check", PermissionChecker.class, JournalArticle.class, String.class);
//		 	 PortalClassInvoker.invoke(false, methodKey, permissionChecker, article, actionId);
//		
//		 	 canAccess = true; //If an exception wasn't thrown the the user is
//		 	 allowed access
//		
//		 } catch (PortalException e) {
//		 	//Stub: PortalException is thrown if the user does can not view the
//		 	resource._log.warn(e.getMessage());
//		 } catch (Exception e) {
//		 	_log.info("Unable to invoke the JournalArticlePermission.check method using the PortalClassInvoker");
//		 	_log.error(e);
//		 }
//		
//		 return canAccess;
//	}

//	/**
//	 * Returns true if a user can view a journal article
//	 * 
//	 * @param request
//	 * @param article
//	 * @return
//	 */
//	public static boolean userCanView(final PortletRequest request,
//			final PermissionChecker permissionChecker,
//			final JournalArticle article, final String actionId) {
//		boolean canAccess = false;
//
//		// forma 2 de checar
//		try {
//		
//			PortalClassInvoker
//					.invoke(false,
//							"com.liferay.portlet.journal.service.permission.JournalArticlePermission",
//							"check",
//							new String[] {
//									"com.liferay.portal.security.permission.PermissionChecker",
//									"com.liferay.portlet.journal.model.JournalArticle",
//									"java.lang.String" }, permissionChecker,
//							article, actionId);
//			canAccess = true; // If an exception wasn't thrown the the user is allowed access
//			
//			_log.info(PortalUtil.getPlidFromPortletId(article.getGroupId(), PortletKeys.JOURNAL));
//		
//		} catch (PortalException e) {
//			_log.warn(e.getMessage());
//		} catch (Exception e) {
//			_log.info("Unable to invoke the JournalArticlePermission.check method using the PortalClassInvoker");
//			_log.error(e);
//		}
//		
//		return canAccess;
//	}
	
	
//	/**
//	 * Returns true if a user can view a journal article
//	 * 
//	 * @param request
//	 * @param article
//	 * @return
//	 */
	/*
	public static boolean userCanView(final PortletRequest request, final PermissionChecker permissionChecker, final JournalArticle article, final String actionId) {
		Boolean hasPermission = StagingPermissionUtil.hasPermission(permissionChecker, article.getGroupId(),
									JournalArticle.class.getName(), article.getResourcePrimKey(), PortletKeys.JOURNAL, actionId);
		if (hasPermission != null) {
			return hasPermission.booleanValue();
		}		
		if (permissionChecker.hasOwnerPermission(
				article.getCompanyId(), JournalArticle.class.getName(),
				article.getResourcePrimKey(), article.getUserId(), actionId)) {
			return true;
		}
		
		return permissionChecker.hasPermission(
			article.getGroupId(), JournalArticle.class.getName(),
			article.getResourcePrimKey(), actionId);
	}
	*/

	/**
	 *
	 * Reply to an comment
	 *
	 * @param message The reply of an comment
	 * @return The comment
	 */
	public static MBMessage sourceMessageOfReply(final MBMessage message) {
		long parentMessageId = message.getParentMessageId();

		MBMessage parentMessage;
		try {
			parentMessage = MBMessageLocalServiceUtil.fetchMBMessage(parentMessageId);
			if (parentMessage != null && parentMessage.getParentMessageId() > 0L) {
				return parentMessage;
			}
		} catch (SystemException e) {
			_log.error(e);
		}

		return null;
	}

	/**
	 * 
	 * Description of the resource
	 * 
	 * @param classNameId 
	 * @param locale
	 * @return
	 */
	public static String resourceType(long classNameId, Locale locale) {
		String messageKey = ResourceActionsUtil.getModelResourceNamePrefix() + PortalUtil.getClassName(classNameId);
		String resourceType = LanguageUtil.get(locale, messageKey);
		
		return resourceType;
	}
	
	/**
	 * 
	 * Class name ids of content wich Liferay allow comments
	 * 
	 * @return An array of <code>long</code> class name ids
	 */
	public static long [] availableClassNameIds() {
		long[] availableClassNameIds = AssetRendererFactoryRegistryUtil.getClassNameIds();
		
		for (long classNameId : availableClassNameIds) {
			AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(PortalUtil.getClassName(classNameId));

			if (!assetRendererFactory.isSelectable()) {
				availableClassNameIds = ArrayUtil.remove(availableClassNameIds, classNameId);
			}
		}
		
		//Layout allows add comments
		long layoutClassNameId = PortalUtil.getClassNameId(Layout.class);
		availableClassNameIds = ArrayUtil.append(availableClassNameIds, layoutClassNameId);		
		
		return availableClassNameIds;
	}

	private static final Log _log = LogFactoryUtil.getLog(CommentsUtil.class);

}
