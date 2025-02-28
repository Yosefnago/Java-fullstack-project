package com.v1.ui;

import com.v1.entity.AccountantUser;
import com.v1.repository.UserRepository;
import com.v1.security.SecurityService;
import com.v1.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.awt.*;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout  {

    private TextField usernameField;
    private PasswordField passwordField;
    Button loginButton,signButton;
    Div loginForm;


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    SecurityService securityService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginView() {
        createLoginForm();
    }
    private void createLoginForm() {

        // login form div
        loginForm = new Div();
        loginForm.getStyle()
                .set("background-color", "#e9eff3")
                .set("padding", "20px")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.1)")
                .set("width", "300px")
                .set("height","400px")
                .set("text-align", "center");

        // login title
        Span title = new Span("Log in");
        title.getStyle()
                .set("font-size", "24px")
                .set("font-weight", "bold")
                .set("margin-top","10px")
                .set("margin-bottom", "20px");
        loginForm.add(title);
        Button button = new Button("Button");
        button.setThemeName("primary");


        // username field
        usernameField = new TextField("Username");
        usernameField.setWidth("100%");
        loginForm.add(usernameField);

        // password field
        passwordField = new PasswordField("Password");
        passwordField.setWidth("100%");
        loginForm.add(passwordField);

        // login button
        loginButton = new Button("היכנס");
        loginButton.getStyle()
                .set("background-color", "#218bcb")
                .set("color", "white")
                .set("border", "none")
                .set("cursor", "pointer");
        loginButton.setWidth("100%");
        loginForm.add(loginButton);

        // sign in button
        signButton = new Button("הרשמה");
        signButton.getStyle()
                .set("background-color", "#218bcb")
                .set("color", "white")
                .set("border", "none")
                .set("cursor", "pointer");
        signButton.setWidth("100%");


        loginForm.add(signButton);

        Span forgot = new Span("Forgot password?");
        forgot.getStyle()
                .set("font-size", "14px")
                .set("font-weight", "bold")
                .set("margin-top", "auto")
                .set("bottom", "0px");
        loginForm.add(forgot);

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("justify-content", "center");

        signButton.addClickListener(event -> {
            getUI().ifPresent(ui -> ui.navigate("register"));
        });

        loginButton.addClickListener(buttonClickEvent -> {
            login();
        });

        add(loginForm);
    }

    private void login() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (authentication.isAuthenticated()) {

            AccountantUser user = userRepository.findByUsername(username);
            VaadinSession.getCurrent().setAttribute(AccountantUser.class.getName(), user);

            Authentication authWithUser = new UsernamePasswordAuthenticationToken(
                    user, authentication.getCredentials(), authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authWithUser);


            UI.getCurrent().navigate(HomeView.class);
        } else {
            Notification.show("סיסמה שגויה", 3000, Notification.Position.MIDDLE);
        }
    }
}
