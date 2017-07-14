package dao;

import model.ARManager;

public interface ARManagerDao {
	public ARManager findOneByID(String ARManagerID);
	public String insertOne(ARManager arManager);
}
