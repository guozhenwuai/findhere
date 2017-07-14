package dao;

import java.util.List;

public interface UserTargetDao {
	public List<String> findUserTargetIDs(String userID);
}
