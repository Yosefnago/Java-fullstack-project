package com.v1.ui;


import com.v1.entity.AccountantUser;
import com.v1.entity.ClientDocuments;
import com.v1.entity.Clients;
import com.v1.entity.FinancialDetails;
import com.v1.security.SecurityService;
import com.v1.service.ClientsService;
import com.v1.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.hibernate.*;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.resource.jdbc.spi.PhysicalConnectionHandlingMode;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.InputStream;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Stream;


@Route("Home")
@PermitAll
public class HomeView extends VerticalLayout {
    private static final String FINANCIAL_DETAILS = "Financial details";
    private static final String FILES_DETAILS = "Files details";
    private static final String CUSTOMER_DETAILS = "Customer details";
    private final SecurityService securityService;

    VerticalLayout mainLayout;
    Button logout;

    @Autowired
    ClientsService clientsService;
    @Autowired
    private UserService userService;

    public HomeView(SecurityService securityService) {
        this.securityService = securityService;
        setSizeFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);

        AccountantUser user = (AccountantUser) VaadinSession.getCurrent().getAttribute(AccountantUser.class.getName());
        if (user == null) {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof AccountantUser)) {
                getUI().ifPresent(ui -> ui.navigate("login"));
                return;
            }
            user = (AccountantUser) auth.getPrincipal();
            VaadinSession.getCurrent().setAttribute(AccountantUser.class.getName(), user);
        }
        Notification.show("ברוך הבא " + user.getUsername(),2000, Notification.Position.MIDDLE);

        HorizontalLayout header = header();

        // Main Layout
        mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.setHeightFull();
        mainLayout.getStyle().set("flex-grow", "1");
        mainLayout.getStyle().setBackground("#f0f1f2");

        add(header, mainLayout);
    }
    public HorizontalLayout header(){
        HorizontalLayout header = new HorizontalLayout();

        header.setWidthFull();
        header.getStyle().setBackground("#cdf0ff");
        header.getStyle().set("box-shadow", "inset 0 4px 6px rgba(0, 0, 0, 0.2)");
        header.getStyle().setHeight("70px");
        header.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        header.setPadding(true);
        header.setAlignItems(HorizontalLayout.Alignment.CENTER);

        // Avatar
        Avatar avatar = new Avatar("User");
        avatar.setHeight("35px");
        avatar.setWidth("35px");
        avatar.getStyle().setLeft("10px");

        MenuBar menuBar = new MenuBar();
        menuBar.addItem("תיקי לקוחות",event -> setMainLayout(openCustomerCase()));
        menuBar.addItem("פתיחת תיק חדש",event -> setMainLayout(newCaseCustomer()));
        menuBar.addItem("הודעות",event -> setMainLayout(messagesLayout()));
        menuBar.addItem("בית",event -> resetMainLayout());
        menuBar.getStyle().setAlignItems(Style.AlignItems.END);


        logout = new Button("התנתק");
        logout.getStyle().setPosition(Style.Position.ABSOLUTE);
        logout.getStyle().setHeight("35px");
        logout.getStyle().setWidth("85px");
        logout.getStyle().setLeft("60px");
        logout.addClickListener(buttonClickEvent -> {
            securityService.logout();
        });


        header.add(avatar,logout,menuBar);

        return header;
    }
    public void resetMainLayout() {
        mainLayout.removeAll();
        mainLayout.add(homeLayout());
    }
    public void setMainLayout(Component content){
        mainLayout.removeAll();
        mainLayout.add(content);
    }
    public H1 homeLayout(){
        H1 h1 = new H1();
        h1.setText("hello home");
        return h1;
    }
    public H1 messagesLayout(){
        H1 h1 = new H1();
        h1.setText("whats-up");
        return h1;
    }

    public Component newCaseCustomer() {
        FormLayout formLayoutClientDetails = new FormLayout();
        formLayoutClientDetails.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("20em", 3)
        );
        formLayoutClientDetails.getElement().setAttribute("dir", "rtl");

        FormLayout formLayoutFinancialDetails = new FormLayout();
        formLayoutFinancialDetails.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("20em", 3)
        );
        formLayoutFinancialDetails.getElement().setAttribute("dir", "rtl");

        FormLayout formLayoutFiles = new FormLayout();
        formLayoutFiles.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("20em", 3)
        );
        formLayoutFiles.getElement().setAttribute("dir", "rtl");


        Tab clientDetails = new Tab("פרטי לקוח");
        Tab financialDetails = new Tab("פרטים פיננסיים");
        Tab filesDetails = new Tab("צירוף מסמכים");
        Tabs tabs = new Tabs(clientDetails, financialDetails, filesDetails);
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.getElement().setAttribute("dir", "rtl");


        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(clientDetails, formLayoutClientDetails);
        tabsToPages.put(financialDetails, formLayoutFinancialDetails);
        tabsToPages.put(filesDetails, formLayoutFiles);


        Div pages = new Div();
        pages.setSizeFull();
        pages.add(formLayoutClientDetails);
        tabs.addSelectedChangeListener(event -> {
            pages.removeAll();
            pages.add(tabsToPages.get(tabs.getSelectedTab()));
        });

        //Client details
        TextField businessName = new TextField("שם עסק");
        businessName.getElement().setAttribute("dir", "rtl");

        TextField clientName = new TextField("שם בעל עסק");
        clientName.getElement().setAttribute("dir", "rtl");

        TextField clientId = new TextField("ח.פ / ת\"ז");
        clientId.getElement().setAttribute("dir", "rtl");

        TextField clientEmail = new TextField("אימייל");
        clientEmail.getElement().setAttribute("dir", "rtl");

        TextField clientPhone = new TextField("מספר פלאפון");
        clientPhone.getElement().setAttribute("dir", "rtl");

        TextField clientAddress = new TextField("כתובת");
        clientAddress.getElement().setAttribute("dir", "rtl");

        Button clientButton = new Button("המשך");

        formLayoutClientDetails.add(businessName, clientName, clientId);
        formLayoutClientDetails.add(clientEmail, clientPhone, clientAddress);
        formLayoutClientDetails.add(clientButton);
        // פרטים פיננסיים
        DatePicker date = new DatePicker("תאריך פתיחת עסק");
        date.getElement().setAttribute("dir", "rtl");

        TextField businessType = new TextField("סוג עסק");
        businessType.getElement().setAttribute("dir", "rtl");

        TextField bankName = new TextField("שם בנק");
        bankName.getElement().setAttribute("dir", "rtl");

        TextField bankId = new TextField("מספר סניף");
        bankId.getElement().setAttribute("dir", "rtl");

        TextField accountNumber = new TextField("מספר חשבון");
        accountNumber.getElement().setAttribute("dir", "rtl");

        Button financeButton = new Button("המשך");

        formLayoutFinancialDetails.add(date, businessType);
        formLayoutFinancialDetails.add(bankName, bankId, accountNumber);
        formLayoutFinancialDetails.add(financeButton);


        TextField fileName = new TextField("שם מסמך");
        fileName.getElement().setAttribute("dir", "rtl");


        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            String fileName1 = event.getFileName();
            InputStream fileStream = buffer.getInputStream();

            Notification.show("הקובץ הועלה בהצלחה: " + fileName1);
        });

        Button saveButton = new Button("שמור");

        formLayoutFiles.add(fileName, upload);
        formLayoutFiles.add(saveButton);


        VerticalLayout layout = new VerticalLayout(tabs, pages);
        layout.getElement().setAttribute("dir", "rtl");

        layout.setSizeFull();


        Clients client = new Clients();
        FinancialDetails financial = new FinancialDetails();
        ClientDocuments clientDocuments = new ClientDocuments();

        Binder<Clients>clientsBinder = new Binder<>(Clients.class);
        Binder<FinancialDetails> financialDetailsBinder = new Binder<>(FinancialDetails.class);
        Binder<ClientDocuments> clientDocumentsBinder = new Binder<>(ClientDocuments.class);

        clientsBinder.setBean(client);
        financialDetailsBinder.setBean(financial);
        clientDocumentsBinder.setBean(clientDocuments);

        clientsBinder.forField(businessName).bind("clientName");
        clientsBinder.forField(clientName).bind("businessOwner");
        clientsBinder.forField(clientId)
                .withValidator(new StringLengthValidator("מספר זהות לא תקין",9,9))
                .bind("taxId");
        clientsBinder.forField(clientEmail)
                .withValidator(new EmailValidator("כתובת אימייל לא תקין")).bind("email");
        clientsBinder.forField(clientPhone)
                .withValidator(new StringLengthValidator("מספר לא תקין",10,10)).bind("phone");
        clientsBinder.forField(clientAddress).bind("address");

        financialDetailsBinder.forField(date).bind("businessStartDate");
        financialDetailsBinder.forField(businessType).bind("businessActivity");
        financialDetailsBinder.forField(bankName).bind("bankName");
        financialDetailsBinder.forField(bankId).bind("bankNumber");
        financialDetailsBinder.forField(accountNumber).bind("bankNumberAccount");

        clientDocumentsBinder.forField(fileName).bind("documentType");


        clientButton.addClickListener(e -> {
           if (clientsBinder.isValid()){
               clientDetails.add(VaadinIcon.CHECK.create());
               tabs.setSelectedTab(financialDetails);
           }else {
               Notification.show("נא למלא את כל השדות" ,2000, Notification.Position.MIDDLE);
           }
        });
        financeButton.addClickListener(e->{
           if (financialDetailsBinder.isValid()){
               financialDetails.add(VaadinIcon.CHECK.create());
               tabs.setSelectedTab(filesDetails);
           }else {
               Notification.show("נא למלא את כל השדות" ,2000, Notification.Position.MIDDLE);
           }
        });
        saveButton.addClickListener(e -> {
            Clients savedClient = clientsService.saveClient(client);
            if (savedClient == null) {
                Notification.show("שמירת הלקוח נכשלה!", 3000, Notification.Position.MIDDLE);
                return;
            }

            financial.setClient(savedClient);
            clientDocuments.setClient(savedClient);
            clientsService.saveFinancialDetails(financial);
            clientsService.saveClientDocuments(clientDocuments);

            Notification.show("תיק נשמר בהצלחה!", 3000, Notification.Position.MIDDLE);

            businessName.clear();
            clientName.clear();
            clientId.clear();
            clientEmail.clear();
            clientPhone.clear();
            clientAddress.clear();
            businessType.clear();
            bankName.clear();
            bankId.clear();
            accountNumber.clear();
            fileName.clear();
            accountNumber.clear();
        });

        return layout;
    }


    public Component openCustomerCase() {
        VerticalLayout layout = new VerticalLayout();
        Grid<Clients> grid = new Grid<>();


        grid.addColumn(Clients::getClientName).setHeader("שם עסק");
        grid.addColumn(Clients::getTaxId).setHeader("ח.פ / ת.ז");
        grid.addColumn(Clients::getBusinessOwner).setHeader("שם בעל העסק");
        grid.addColumn(Clients::getEmail).setHeader("מייל");
        grid.addColumn(Clients::getPhone).setHeader("פלאפון");

        grid.getStyle().setTextDecoration("rtl");
        grid.getStyle().setJustifyContent(Style.JustifyContent.END);

        // Need to fix this section
        AccountantUser user = getLoggedInAccountant();

        List<Clients> clientsList = clientsService.getClientsByAccountantId(user.getId());
        grid.setItems(clientsList);
        //////////////////////////

        layout.add(grid);
        return layout;
    }
    public AccountantUser getLoggedInAccountant() {
        Authentication userLoggedIn = SecurityContextHolder.getContext().getAuthentication();
        if (userLoggedIn != null && userLoggedIn.isAuthenticated()) {
            Object principal = userLoggedIn.getPrincipal();
            if (principal instanceof AccountantUser) {
                return (AccountantUser) principal;
            }
        }
        return null;

    }
}
