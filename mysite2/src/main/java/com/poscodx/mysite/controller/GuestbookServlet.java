package com.poscodx.mysite.controller;

import java.util.Map;

import com.poscodx.mysite.controller.action.guestbook.AddAction;
import com.poscodx.mysite.controller.action.guestbook.DeleteAction;
import com.poscodx.mysite.controller.action.guestbook.DeleteFormAction;
import com.poscodx.mysite.controller.action.guestbook.ListAction;

public class GuestbookServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Action> mapAction = Map.of(
			"add", new AddAction(),
			"deleteform", new DeleteFormAction(),
			"delete", new DeleteAction()
	);
		
	@Override
	protected Action getAction(String actionName) {
		return mapAction.getOrDefault(actionName, new ListAction());
	}
}
