package com.asmin.service;

import java.util.List;

import com.asmin.entity.AppliedJobs;
import com.asmin.entity.Commun;
import com.asmin.entity.Company;
import com.asmin.entity.Jobs;
import com.asmin.entity.User;

public interface JobsService {
	List<Jobs> getAllJobs();
	List<Company> getAllCompany();
	public User addUser(String email,String name,String password,String state,String education) throws Exception;
	public Company addCompany(String compemail,String name,String password,String state,String regyear) throws Exception;
	public boolean validateUser(String email, String password);
	public boolean validateCompany(String email, String password,String regyear);
	public Jobs addjob(String compname,String title,String exp,String regyear,String date,boolean b,String email,String skills,String salary,String addr) throws Exception;
	public List<Jobs> getCompDataByEmail(String email);
	public AppliedJobs addJobs(String name,String email,String message,String Degree,boolean status);
	public Commun addcommunJobs(String name,String email,String cemail,String degree,String message,boolean status,String respMesg,int icon,String compName,String title,String skills,String addr);
	public List<Commun> findByCEmail(String email);
	public List<Commun> findByUEmail(String email);
	public void deleteAllByEmail(String email);
	public void deleteAllByCemail(String cemail);
}
