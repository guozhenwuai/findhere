package service.Impl;

import java.util.List;

import javax.annotation.Resource;

import dao.ApplyDao;
import dao.UserDao;
import model.Apply;
import model.User;
import service.ApplyService;

public class ApplyServiceImpl implements ApplyService {
	
	@Resource
	private ApplyDao applyDao;
	
	@Resource
	private UserDao userDao;
	
	public List<Apply> getAPageApply(int pageIndex){
		return applyDao.getAPageApply(pageIndex);
	}
	
	public void  agreeMember(String userID, String applyID) {
		User user = userDao.findOneByID(userID);
		user.setIsMember(1);
		userDao.update(user);
		applyDao.removeApply(applyID);
	}
	
	public void rejectApply(String applyID) {
		applyDao.removeApply(applyID);
	}
	
	public void addApply(String userID, String realName, String nationalID, String incorporation) {
		Apply apply = new Apply();
		apply.setUserID(userID);
		apply.setName(realName);
		apply.setNationalID(nationalID);
		apply.setInc(incorporation);
		applyDao.addApply(apply);
	}
	
	/*GET and SET*/
	public ApplyDao getMongoTemplate() {
		return applyDao;
	}
	
	public void setApplyDao(ApplyDao s) {
		applyDao = s;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}
	
	public void setUserDao(UserDao dao) {
		userDao = dao;
	}
}
