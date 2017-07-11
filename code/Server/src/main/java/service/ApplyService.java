package service;

import java.util.List;

import model.Apply;

public interface ApplyService {
	public List<Apply> getAPageApply(int pageIndex);
	public void  agreeMember(String userID, String applyID);
}
