package br.com.severnini.comments.portlet.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;

/**
 * 
 * Configuration class
 * 
 * @author Luiz Fernando Severnini <luizfernando.severnini@gmail.com>
 *
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {
	
	@Override
	public void processAction(PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		//TODO - validate params before storing
		super.processAction(portletConfig, actionRequest, actionResponse);
	}
	
	@Override
	public String render(PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws Exception {
		//this method is invoked when the user clicks the configuration icon
		return super.render(portletConfig, renderRequest, renderResponse);
	}

}
