package com.poscodx.mysite.controller;

import java.util.Map;

import com.poscodx.mysite.controller.action.board.DeleteAction;
import com.poscodx.mysite.controller.action.board.ListAction;
import com.poscodx.mysite.controller.action.board.ModifyAction;
import com.poscodx.mysite.controller.action.board.ModifyFormAction;
import com.poscodx.mysite.controller.action.board.ReplyAction;
import com.poscodx.mysite.controller.action.board.ReplyFormAction;
import com.poscodx.mysite.controller.action.board.SearchAction;
import com.poscodx.mysite.controller.action.board.ViewAction;
import com.poscodx.mysite.controller.action.board.WriteAction;
import com.poscodx.mysite.controller.action.board.WriteFormAction;

public class BoardServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;
	   
	private Map<String, Action> mapAction = Map.of(
		"modifyform", new ModifyFormAction(),
		"modify", new ModifyAction(),
		"view", new ViewAction(),
		"write", new WriteAction(),
		"writeform", new WriteFormAction(),
		"delete", new DeleteAction(),
		"reply", new ReplyAction(),
		"replyform", new ReplyFormAction(),
		"search", new SearchAction()
	);
	
	@Override
	protected Action getAction(String actionName) {
		return mapAction.getOrDefault(actionName, new ListAction());
	}
}

