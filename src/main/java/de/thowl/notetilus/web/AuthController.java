package de.thowl.notetilus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import de.thowl.notetilus.core.services.AuthenticationService;
import de.thowl.notetilus.storage.SessionRepository;
import de.thowl.notetilus.storage.UserRepository;
import de.thowl.notetilus.storage.entities.AccessToken;
import de.thowl.notetilus.storage.entities.Session;
import de.thowl.notetilus.storage.entities.User;
import de.thowl.notetilus.web.forms.LoginForm;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

	@Autowired
	private AuthenticationService authsvc;
	@Autowired
	private UserRepository users;
	@Autowired
	private SessionRepository sessions;

	/**
	 * Shows the login page
	 * @return index.html
	 */
	@GetMapping("/login")
	public String showLoginPage() {
		log.info("entering showLoginPage (GET-Method: /login)");
		return "login";
	}

	/**
	 * Performs a login action
	 * @return index.html
	 */
	@PostMapping("/login")
	public String doLogin(LoginForm form, Model model, HttpSession httpSession) {
		log.info("entering doLogin (POST-Method: /login)");

		String email = form.getEmail();
		String password = form.getPassword();

		AccessToken token = authsvc.login(email, password);
		// TODO: nit: would probably be better if we get the user via the token.
		User user = this.users.findByEmail(form.getEmail());

		if (null == token){
			// TODO: add localisation
			model.addAttribute("error", "E-Mail oder Passwort falsch");
			return "index";
		}

		httpSession.setAttribute("user", user);
		httpSession.setAttribute("token", token);

		return "redirect:/u/" + user.getUsername() + "/notes";
	}
	
	/**
	 * Performs a logout action
	 */
	// NOTE: GetMapping was easier than handling this via a post request 
	@GetMapping("/u/{username}/logout")
	public String doLogout(@PathVariable("username") String username,
			@SessionAttribute("token") AccessToken token) {
		authsvc.logout(token.getUSID());
		return "index";
	}
	
}

