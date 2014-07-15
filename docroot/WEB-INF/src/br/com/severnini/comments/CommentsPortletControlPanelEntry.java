package br.com.severnini.comments;

import com.liferay.portal.model.Portlet;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portlet.BaseControlPanelEntry;


/**
 * 
 * Control panel entry class CommentsPortletControlPanelEntry
 * 
 */
public class CommentsPortletControlPanelEntry extends BaseControlPanelEntry {

    @Override
    public boolean isVisible(PermissionChecker permissionChecker, Portlet portlet)
            throws Exception {
    	//TODO - implement check
        return true;
    }

}