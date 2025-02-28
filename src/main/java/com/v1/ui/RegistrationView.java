package com.v1.ui;

import com.v1.entity.AccountantUser;
import com.v1.service.UserService;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * RegistrationView class
 */
@Route(value = "register")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    Button saveButton;
    Div div;

    private Binder<AccountantUser>binder;
    private AccountantUser accountantUser;

    @Autowired
    UserService userService;

    public RegistrationView() {
        // Initialize layout settings and components
        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getStyle().set("justify-content", "center");

        H2 amsSignIn = new H2("Ams sign up");

        // Configure main div container
        div = new Div();
        div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        div.getStyle().setWidth("500px").setHeight("400px");
        div.getStyle().setPosition(Style.Position.RELATIVE);
        div.getStyle().setBackground("#e9eff3");

        // Set up form layout
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setHeight("360px");
        layout.getStyle().setPosition(Style.Position.RELATIVE);

        // Button layout at the bottom of the form
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.getStyle().setBottom("0px");
        horizontalLayout.getStyle().setHeight("40px");
        horizontalLayout.getStyle().setPosition(Style.Position.ABSOLUTE);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.getStyle().setJustifyContent(Style.JustifyContent.CENTER);

        saveButton = new Button("שמור");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Initialize fields and bind them to the accountantUser bean
        TextField username = new TextField("Username");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmpassword = new PasswordField("Confirm Password");
        TextField phone = new TextField("Phone");

        username.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);
        password.setRequiredIndicatorVisible(true);
        confirmpassword.setRequiredIndicatorVisible(true);
        phone.setRequiredIndicatorVisible(true);

        username.setPlaceholder("Username");
        email.setPlaceholder("Email");
        password.setPlaceholder("Password");
        confirmpassword.setPlaceholder("Confirm Password");
        phone.setPlaceholder("Phone");

        // Setup binder for data binding and validation
        binder = new Binder<>();
        accountantUser = new AccountantUser();

        binder.forField(username)
                .asRequired("Username is required")
                .bind(AccountantUser::getUsername, AccountantUser::setUsername);

        binder.forField(email)
                .asRequired("Email is required")
                .withValidator(new EmailValidator("Invalid email address"))
                .bind(AccountantUser::getEmailAddress, AccountantUser::setEmailAddress);

        binder.forField(password)
                .asRequired("Password is required")
                .withValidator(new StringLengthValidator("Password length must be between 6 and 20 characters.", 6, 20))
                .bind(AccountantUser::getPassword, AccountantUser::setPassword);

        binder.forField(phone)
                .asRequired("Phone number is required")
                .bind(AccountantUser::getPhone, AccountantUser::setPhone);

        binder.withValidator(user ->
                        password.getValue().equals(confirmpassword.getValue()),
                "Passwords do not match"
        );

        saveButton = new Button("שמור");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> saveUser());

        // Layout for form rows
        HorizontalLayout row1 = new HorizontalLayout();
        HorizontalLayout row2 = new HorizontalLayout();
        HorizontalLayout row3 = new HorizontalLayout();

        row1.add(username,email);
        row2.add(password,confirmpassword);

        // Configure and add components to the layout
        layout.getStyle().setJustifyContent(Style.JustifyContent.CENTER);
        layout.getStyle().setAlignItems(Style.AlignItems.CENTER);

        layout.add(row1, row2, row3);
        horizontalLayout.add(saveButton);

        // Add all components to the main div
        div.add(layout);
        div.add(horizontalLayout);
        add(amsSignIn);
        add(div);
    }
    /**
     * Attempts to save the user data entered in the registration form.
     * Validates the form, writes data to the accountantUser bean, and saves it
     * through the UserService. Notifies the user of the result.
     */
    private void saveUser() {
        try {
            binder.writeBean(accountantUser);

            AccountantUser savedUser = userService.saveUser(
                    accountantUser.getUsername(),
                    accountantUser.getPassword(),
                    accountantUser.getEmailAddress(),
                    accountantUser.getPhone(),
                    accountantUser.getLicenseNumber()
            );

            if (savedUser != null) {
                Notification.show("User saved successfully: " + savedUser.getUsername(), 3000, Notification.Position.TOP_CENTER);
                binder.readBean(new AccountantUser());
                getUI().ifPresent(ui -> ui.navigate("login"));
            }
        } catch (ValidationException e) {
            Notification.show("Please fill all required fields correctly.", 3000, Notification.Position.TOP_CENTER);
        } catch (Exception ex) {
            Notification.show("Error saving user: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }
}
