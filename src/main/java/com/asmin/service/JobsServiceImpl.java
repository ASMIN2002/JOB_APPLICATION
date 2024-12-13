package com.asmin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.asmin.entity.AppliedJobs;
import com.asmin.entity.Commun;
import com.asmin.entity.Company;
import com.asmin.entity.Jobs;
import com.asmin.entity.User;
import com.asmin.repository.ApplyRepo;
import com.asmin.repository.CommunRepo;
import com.asmin.repository.CompRepo;
import com.asmin.repository.JobsRepo;
import com.asmin.repository.UserRepo;

@Service
public class JobsServiceImpl implements JobsService {

	@Autowired
	private JobsRepo repo;

	@Autowired
	private UserRepo urepo;

	@Autowired
	private CompRepo crepo;

	@Autowired
	private ApplyRepo arepo;
	
	@Autowired
	private CommunRepo comrepo;

	@Override
	public List<Jobs> getAllJobs() {
		List<Jobs> jobs = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		return jobs;
	}

	@Override
	public User addUser(String email, String name, String password, String state, String education) throws Exception {
		if (urepo.existsByEmail(email)) {
			throw new Exception("Email address already exists!");
		}

		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setEducation(education);
		user.setPassword(password);
		user.setState(state);
		return urepo.save(user);
	}

	public boolean validateUser(String email, String password) {
		User user = urepo.findByEmail(email);
		return user != null && user.getPassword().equals(password);
	}

	@Override
	public Company addCompany(String email, String name, String password, String loc, String regyear) throws Exception {
		if (crepo.existsByEmail(email)) {
			throw new Exception("Email address already exists!");
		}

		Company comp = new Company();
		comp.setEmail(email);
		comp.setCompname(name);
		comp.setLoc(loc);
		comp.setPassword(password);
		comp.setRegyear(regyear);
		return crepo.save(comp);
	}

	@Override
	public boolean validateCompany(String email, String password, String regyear) {
		Company comp = crepo.findByEmail(email);
		return comp != null && comp.getPassword().equals(password) && comp.getRegyear().equals(regyear);
	}

	@Override
	public Jobs addjob(String compname, String title, String exp, String regyear, String date, boolean b, String email,
			String skils, String salary, String addr) throws Exception {

		Jobs jobs = new Jobs();
		jobs.setEmail(email);
		jobs.setCompname(compname);
		jobs.setTitle(title);
		jobs.setExp(exp);
		jobs.setRegyear(regyear);
		jobs.setDate(date);
		jobs.setStatus(b);
		jobs.setSkills(skils);
		jobs.setSalary(salary);
		jobs.setAddr(addr);
		return repo.save(jobs);
	}

	@Override
	public List<Jobs> getCompDataByEmail(String email) {
		return repo.findCompsByEmail(email);
	}

	@Override
	public List<Company> getAllCompany() {
		List<Company> comps = crepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		return comps;
	}

	@Override
	public AppliedJobs addJobs(String name, String email, String message, String Degree, boolean status) {
		AppliedJobs ajobs = new AppliedJobs();
		ajobs.setName(name);
		ajobs.setEmail(email);
		ajobs.setDegree(Degree);
		ajobs.setMessage(message);
		ajobs.setStatus(status);
		return arepo.save(ajobs);
	}

	@Override
	public Commun addcommunJobs(String name, String email, String cemail, String degree, String message,
			boolean status,String respMesg,int icon,String compName,String title,String skills,String addr) {
		Commun c = new Commun();
		c.setCemail(cemail);
		c.setDegree(degree);
		c.setMessage(message);
		c.setName(name);
		c.setUemail(email);
		c.setStatus(status);
		c.setRespoMesg(null);
		c.setIcon(0);
		c.setCompName(compName);
		c.setJobTitle(title);
		c.setSkills(skills);
		c.setAddr(addr);
		return comrepo.save(c);
	}

	@Override
	public List<Commun> findByCEmail(String email) {
		return comrepo.findByCemail(email);
	}
	
	@Override
	public List<Commun> findByUEmail(String email) {
		return comrepo.findByUemail(email);
	}

	@Override
	public void deleteAllByEmail(String email) {
		repo.deleteAllByEmail(email);
	}
	
	@Override
	public void deleteAllByCemail(String cemail) {
		comrepo.deleteAllByCemail(cemail);
	}

}
