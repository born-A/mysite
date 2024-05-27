package com.poscodx.mysite.controller;

import com.poscodx.mysite.controller.action.main.MainAction;

public class MainServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected Action getAction(String actionName) {
		return new MainAction();
	}

}
