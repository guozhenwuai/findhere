package controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.ApplyService;

@Controller
public class AgreeMember {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private ApplyService applyService;
	
	@RequestMapping("/AgreeMember")
	public String execute(@RequestParam("userID")String userID, @RequestParam("applyID")String applyID, 
			@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		applyService.agreeMember(userID, applyID);
		return "redirect:/ConfirmMember?pageIndex="+pageIndex;
	}
	
	/*GET and SET*/
	public ApplyService getApplyService() {
		return applyService;
	}
	
	public void setApplyService(ApplyService s) {
		this.applyService = s;
	}
}
