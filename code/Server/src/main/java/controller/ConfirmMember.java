package controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.Apply;
import service.ApplyService;

@Controller
public class ConfirmMember {
	private static final long serialVersionUID = 1L;
	
	@Resource
	private ApplyService applyService;
	
	private List<Apply> applyList;
	
	@RequestMapping("/ConfirmMember")
	public String execute(@RequestParam("pageIndex")int pageIndex,
			HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		applyList = applyService.getAPageApply(pageIndex);
		request.setAttribute("applyList", applyList);
		request.setAttribute("pageIndex", pageIndex);
		return "confirm";
	}
	
	/*GET and SET*/
	public ApplyService getApplyService() {
		return applyService;
	}
	
	public void setApplyService(ApplyService s) {
		this.applyService = s;
	}
	
	public List<Apply> getApplyList(){
		return applyList;
	}
	
	public void setApplyList(List<Apply> s) {
		applyList = s;
	}
}
