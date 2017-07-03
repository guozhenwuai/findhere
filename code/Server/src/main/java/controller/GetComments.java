package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GetComments {
	private static final long serialVersionUID = 1L;
	
	@RequestMapping("/GetComments")
	public String execute(){
		return null;
	}
}
