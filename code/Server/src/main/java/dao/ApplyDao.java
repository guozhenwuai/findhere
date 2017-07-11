package dao;

import java.util.List;

import model.Apply;

public interface ApplyDao {
	public List<Apply> getAPageApply(int pageIndex);
	public void removeApply(String applyID);
}
