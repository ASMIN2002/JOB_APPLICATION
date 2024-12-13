package com.asmin.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.asmin.entity.AppliedJobs;
import com.asmin.entity.ComanyDto;
import com.asmin.entity.Commun;
import com.asmin.entity.Company;
import com.asmin.entity.Jobs;
import com.asmin.entity.JobsDto;
import com.asmin.entity.User;
import com.asmin.entity.UserDto;
import com.asmin.repository.ApplyRepo;
import com.asmin.repository.CommunRepo;
import com.asmin.repository.CompRepo;
import com.asmin.repository.JobsRepo;
import com.asmin.repository.UserRepo;
import com.asmin.service.JobsService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private JobsService serv;

	@Autowired
	private JobsRepo jrepo;

	@Autowired
	private UserRepo repo;

	@Autowired
	private CompRepo crepo;

	@Autowired
	private CommunRepo comrepo;

	@Autowired
	private ApplyRepo arepo;

//	FORMALITIES +++++++++++++++++++++++++++++++++++++++++++++

	@GetMapping
	public String home(Model model) {
		List<Jobs> jobs = serv.getAllJobs();
		if (jobs != null && !jobs.isEmpty()) {
			model.addAttribute("jobs", jobs);
		} else {
			model.addAttribute("notanyjobs", "NO DATA AVAILABLE");
		}
		return "home";
	}

	@GetMapping("/comps")
	public String comps(Model model) {
		List<Company> comps = crepo.findAll(Sort.by(Sort.Direction.DESC, "cid"));
		if (comps != null && !comps.isEmpty()) {
			model.addAttribute("comps", comps);
		} else {
			model.addAttribute("notanycomps", "NO DATA AVAILABLE");
		}
		return "comps";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/complogin")
	public String complogin() {
		return "complogin";
	}

	@GetMapping("/register")
	public String reg() {
		return "register";
	}

	@GetMapping("/compreg")
	public String compreg() {
		return "compreg";
	}

	@PostMapping("/adduser")
	public String addUser(@RequestParam String email, @RequestParam("name") String name,
			@RequestParam("password") String password, @RequestParam("education") String education,
			@RequestParam("state") String state, Model model) throws Exception {

		boolean b = repo.existsByEmail(email);
		if (b) {
			model.addAttribute("error", "OOPS! Email already exist.");
		} else {
			serv.addUser(email, name, password, state, education);
			model.addAttribute("successMessage", "User added successfully!");
		}
		return "register";
	}

	@PostMapping("/login")
	public String handleLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			Model model) throws InterruptedException {
		boolean isValid = serv.validateUser(email, password);

		if (isValid) {
			UserDto dto = new UserDto();
			dto.setEmail(email);
			model.addAttribute("message", "Login successful!");
			model.addAttribute("email12", email);
			model.addAttribute("email", dto.getEmail());
			return "redirect:/userHome/" + email;
		} else {
			model.addAttribute("error", "Invalid User! Please Try Again");
			return "login";
		}
	}

	@PostMapping("/complogin")
	public String companyLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("regyear") String regyear, Model model, HttpSession session) {
		boolean isValid = serv.validateCompany(email, password, regyear);

		if (isValid) {
			session.setAttribute("email", email);
			model.addAttribute("message", "Login successful!");
			return "redirect:/compHist/" + email;
		} else {
			model.addAttribute("error", "Invalid Company.");
			return "complogin";
		}
	}

	@PostMapping("/compreg")
	public String addCompany(@RequestParam("email") String email, @RequestParam("name") String name,
			@RequestParam("password") String password, @RequestParam("regyear") String regyear,
			@RequestParam("loc") String loc, Model model) throws Exception {

		boolean b = crepo.existsByEmail(email);

		if (b) {
			model.addAttribute("exist", "OOPS! Email already exist.");
		} else {
			serv.addCompany(email, name, password, loc, regyear);
			model.addAttribute("compName", name);
			model.addAttribute("successMessage", "Registration");
		}
		return "compreg";
	}

//	COMPANY MODULE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	@GetMapping("/compHist/{email}")
	public String compHist(@PathVariable("email") String email, Model model) {
		List<Jobs> jobs = serv.getCompDataByEmail(email);
		Company comps = crepo.findByEmail(email);
		model.addAttribute("comps", comps);

		if (jobs.isEmpty() || jobs == null) {
			model.addAttribute("nulljobs", true);
			return "compHist";
		}
		
		model.addAttribute("job", true);
		model.addAttribute("jobs", jobs);
		model.addAttribute("email", email);
		return "compHist";
	}

	@PostMapping("/editCompJobs/{id}")
	public String editCompJobs(@PathVariable("id") long id, Model model, @ModelAttribute JobsDto jdto) {
		Jobs jobs = jrepo.findById(id).orElse(null);
		model.addAttribute("jobs", jobs);
		model.addAttribute("id", id);

		jobs.setTitle(jdto.getTitle());
		jobs.setSkills(jdto.getSkills());
		jobs.setAddr(jdto.getAddr());
		jobs.setExp(jdto.getExp());
		jobs.setSalary(jdto.getSalary());
		jobs.setStatus(jdto.isStatus());

		jrepo.save(jobs);

		return "redirect:/compHist/" + jobs.getEmail();
	}

	@PostMapping("/updateCompany/{email}")
	public String updateCompany(@PathVariable("email") String email, Model model, @RequestParam("compname") String name,
			@ModelAttribute ComanyDto dto) throws Exception {
		Company jobs = crepo.findByEmail(email);
		if (jobs != null) {
			jobs.setCompname(dto.getCompname());
			jobs.setLoc(dto.getLoc());
			jobs.setPassword(dto.getPassword());
			jobs.setRegyear(dto.getRegyear());
			crepo.save(jobs);
		}

		return "redirect:/checkCompProfile/" + email;
	}

	@GetMapping("/CompNotify/{email}")
	public String compNotify(@PathVariable("email") String email, Model model) {
		List<Commun> commun = serv.findByCEmail(email);
		if (commun == null || commun.isEmpty()) {
			model.addAttribute("empcomun", "No Data Available");
		} else {
			model.addAttribute("commun", commun);
		}
		return "compNotification";
	}

	@GetMapping("/checkCompProfile/{email}")
	public String checkCompProfile(@PathVariable("email") String email, Model model) {
		Company comp = crepo.findByEmail(email);
		ComanyDto dto = new ComanyDto();
		dto.setCompname(comp.getCompname());
		dto.setEmail(comp.getEmail());
		dto.setLoc(comp.getLoc());
		dto.setPassword(comp.getPassword());
		dto.setRegyear(comp.getRegyear());

		model.addAttribute("comp", comp);
		model.addAttribute("cdto", dto);
		model.addAttribute(dto);
		return "checkCompProfile";
	}

	@GetMapping("/jobPost")
	public String postJob(@RequestParam String email, Model model) {
		model.addAttribute("email", email);
		return "jobPost";
	}

	@GetMapping("/jobs/{email}")
	public String jobs(@PathVariable("email") String email, Model model) {
		List<Jobs> jobs = serv.getAllJobs();
		if (jobs != null && !jobs.isEmpty()) {
			model.addAttribute("jobs", jobs);
		} else {
			model.addAttribute("notanyjobs", "NO DATA AVAILABLE");
		}
		return "compJobs";
	}

	@GetMapping("/deleteComp/{email}")
	public String deleteCompany(@PathVariable("email") String email) {
		Company comp = crepo.findByEmail(email);
		if (comp != null) {
			crepo.delete(comp);
			serv.deleteAllByEmail(email);
			serv.deleteAllByCemail(email);
		}
		return "redirect:/";
	}

	@PostMapping("/jobCompPost")
	public String postCompjob(@RequestParam("email") String email, @RequestParam("compname") String compname,
			@RequestParam("exp") String exp, @RequestParam("regyear") String regyear,
			@RequestParam("skills") String skills, @RequestParam("addr") String addr,
			@RequestParam("title") String title, @RequestParam("salary") String salary,
			RedirectAttributes redirectAttributes, Model model) throws Exception {

		LocalDate today = LocalDate.now();
		String date1 = String.valueOf(today);

		Jobs job = serv.addjob(compname, title, exp, regyear, date1, true, email, skills, salary, addr);
		if (job != null) {
			redirectAttributes.addFlashAttribute("done", "Added");
		}

		return "redirect:/compHist/" + email;
	}

//	USER MODULE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	@GetMapping("/userHome/{email}")
	public String userHome(@PathVariable("email") String email, Model model) {
		model.addAttribute("email", email);

		try {
			User user = repo.findByEmail(email);
			model.addAttribute("user", user);
			model.addAttribute("NAME", user.getName());
		} catch (Exception e) {

		}
		return "userHome";
	}

	@GetMapping("/userJobs/{email}")
	public String viewUserJobs(@PathVariable("email") String email, Model model) {
		List<Jobs> jobs = jrepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
		if (jobs == null || jobs.isEmpty()) {
			model.addAttribute("empjobs", "NO DATA AVAILABLE");
		} else {
			model.addAttribute("jobs", jobs);
		}
		model.addAttribute("email", email);
		return "userJobs";

	}

	@GetMapping("/userComps/{email}")
	public String viewUserComps(@PathVariable("email") String email, Model model) {
		List<Company> comps = crepo.findAll();
		if (comps == null || comps.isEmpty()) {
			model.addAttribute("empjobs", "NO DATA AVAILABLE");
		} else {
			model.addAttribute("comps", comps);
		}
		return "userComps";

	}

	@GetMapping("/userNotify/{email}")
	public String userNotifications(@PathVariable("email") String email, Model model) {
		List<Commun> coms = serv.findByUEmail(email);
		if (coms == null || coms.isEmpty()) {
			model.addAttribute("empjobs", "NO DATA AVAILABLE");
		} else {
			model.addAttribute("coms", coms);
		}
		return "userNotification";
	}

	@GetMapping("/userJobs/applyJob/{email}/{cemail}/{id}")
	public String applyJobs(@PathVariable("email") String email, @PathVariable("cemail") String cemail,
			@PathVariable("id") long id, Model model) {
		model.addAttribute("email", email);
		model.addAttribute("cemail", cemail);
		model.addAttribute("id", id);
		return "applyJob";
	}

	@PostMapping("/jobApply")
	public String postJobs(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("id") long id, @RequestParam("cemail") String cemail, @RequestParam("message") String message,
			@RequestParam("degree") String degree, RedirectAttributes redirectAttributes, Model model) {
		AppliedJobs jobs = serv.addJobs(name, email, message, degree, true);
		Jobs job2 = jrepo.findById(id).orElse(null);
		if (jobs != null) {
			redirectAttributes.addFlashAttribute("success", "Added");
		}
		serv.addcommunJobs(name, email, cemail, degree, message, false, null, 0, job2.getCompname(), job2.getTitle(),
				job2.getSkills(), job2.getAddr());
		
		return "redirect:/userJobs/applyJob/" + email + "/" + cemail + "/" + 1;
	}

	@GetMapping("/register/{id}")
	public String register() {
		return "register";
	}

	@GetMapping("/checkUserProfile/{email}")
	public String checkuserProfile(@PathVariable("email") String email, Model model) {
		User user = repo.findByEmail(email);
		UserDto uDto = new UserDto();
		uDto.setName(user.getName());
		uDto.setEmail(user.getEmail());
		uDto.setEducation(user.getEducation());
		uDto.setPassword(user.getPassword());
		uDto.setState(user.getState());

		model.addAttribute("user", user);
		model.addAttribute("uDto", uDto);
		model.addAttribute(uDto);
		List<Commun> communs = comrepo.findByUemail(email);
		if (communs == null || communs.isEmpty()) {
			model.addAttribute("histo", communs);
		}else {
			model.addAttribute("hist", communs);
			
		}
		return "checkUserProfile";
	}

	@PostMapping("approve")
	public String approve(@RequestParam("id") long id, @RequestParam("email") String email,
			@RequestParam("btn1") int btn, Model model) {

		Commun commun = comrepo.findById(id).orElse(null);
		if (btn == 1) {
			commun.setIcon(1);
			commun.setRespoMesg("Congratulation ! Your Application was sortlisted.Please Contact With " + email);
			comrepo.save(commun);
		}
		if (btn == 2) {
			commun.setIcon(2);
			commun.setRespoMesg(
					"Sorry ! Your Application was rejected. We appriciate Your Time. Thank You, Best Of Luck");
			comrepo.save(commun);
		}
		return "redirect:/CompNotify/" + email;
	}

	@GetMapping("editJobs/{id}/{email}")
	public String jobsEdit(@PathVariable("id") long id, @PathVariable("email") String email, Model model) {
		Jobs jobs = jrepo.findById(id).orElse(null);
		JobsDto jdto = new JobsDto();
		jdto.setTitle(jobs.getTitle());
		jdto.setExp(jobs.getExp());
		jdto.setSalary(jobs.getSalary());
		jdto.setSkills(jobs.getSkills());
		jdto.setStatus(jobs.isStatus());
		jdto.setAddr(jobs.getAddr());

		if (jobs.isStatus()) {
			model.addAttribute("active", 0);
		} else {
			model.addAttribute("inactive", 1);
		}

		model.addAttribute("jobs", jobs);
		model.addAttribute("jdto", jdto);
		model.addAttribute(jdto);
		model.addAttribute("email", email);
		return "editJobs";
	}

	@GetMapping("/delete/{id}")
	public String deleteJobs(@PathVariable long id) {
		Jobs job = jrepo.findById(id).orElse(null);
		if (job != null) {
			jrepo.deleteById(id);
		}
		return "redirect:/compHist/" + job.getEmail();
	}

	@PostMapping("/updateUser/{email}")
	public String updateUser(@PathVariable("email") String email, Model model, @RequestParam("name") String name,
			@ModelAttribute UserDto udto) throws Exception {
		User jobs = repo.findByEmail(email);
		if (jobs != null) {
			jobs.setName(udto.getName());
			jobs.setEducation(udto.getEducation());
			jobs.setPassword(udto.getPassword());
			jobs.setState(udto.getState());
			repo.save(jobs);
		}

		return "redirect:/checkUserProfile/" + email;
	}

	@GetMapping("/back/{email}")
	public String back(@PathVariable("email") String email, Model model) {
		return "redirect:/compProfile?email=" + email;
	}

	@GetMapping("/deleteUser/{email}")
	public String deleteUser(@PathVariable("email") String email) {
		User user = repo.findByEmail(email);
		if (user != null) {
			repo.delete(user);
			arepo.deleteAllByEmail(email);
		}
		return "redirect:/";
	}
}
