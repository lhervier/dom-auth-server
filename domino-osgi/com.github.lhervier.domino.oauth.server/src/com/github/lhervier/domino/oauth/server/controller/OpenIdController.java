package com.github.lhervier.domino.oauth.server.controller;

import javax.servlet.http.HttpServletResponse;

import lotus.domino.NotesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.lhervier.domino.oauth.server.NotesPrincipal;
import com.github.lhervier.domino.oauth.server.aop.ann.ctx.ServerRootContext;
import com.github.lhervier.domino.oauth.server.aop.ann.security.Bearer;
import com.github.lhervier.domino.oauth.server.ex.NotAuthorizedException;
import com.github.lhervier.domino.oauth.server.ext.openid.IdToken;
import com.github.lhervier.domino.oauth.server.services.OpenIdUserInfoService;

/**
 * Controller to manage userInfo openId end point
 * @author Lionel HERVIER
 */
@Controller
@ServerRootContext
public class OpenIdController {

	/**
	 * The opend id service
	 */
	@Autowired
	private OpenIdUserInfoService userInfoSvc;
	
	/**
	 * We are unable to inject this bean as a method parameter
	 */
	@Autowired
	private NotesPrincipal userInfoUser;
	
	/**
	 * The userInfo end point
	 * @return
	 * @throws NotesException 
	 */
	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	@Bearer
	public @ResponseBody IdToken userInfo(HttpServletResponse response) throws NotesException, NotAuthorizedException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return this.userInfoSvc.userInfo(this.userInfoUser);
	}
}
