package com.v1.ui;

import com.v1.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import jakarta.annotation.security.PermitAll;


@Route("Main-view")
@PermitAll
public class MainView extends VerticalLayout implements RouterLayout{
    private final SecurityService securityService;
    Button logout,newCustomer;

    VerticalLayout mainLayout;
    public MainView(SecurityService securityService) {

        this.securityService = securityService;
        setSizeFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);

        // Header
        HorizontalLayout header = header();

        // Main Layout
        mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.setHeightFull();
        mainLayout.getStyle().set("flex-grow", "1");
        mainLayout.getStyle().setBackground("#f0f1f2");

        Div whatsNew = whatsNew();
        Div graphs = graphs();
        Div incomeAndOutCome = incomeAndOutCome();


        mainLayout.add(whatsNew,graphs,incomeAndOutCome);
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

        MenuBar menuBar = new MenuBar();
        menuBar.addItem("הגדרות");
        menuBar.addItem("ארכיון");
        menuBar.addItem("חשבוניות");
        menuBar.addItem("דוחות");
        menuBar.addItem("מסמכים");
        menuBar.addItem("בית",event -> resetMainLayout());

        menuBar.getStyle().setAlignItems(Style.AlignItems.END);


        TextField searchField = new TextField();
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.getStyle().setPosition(Style.Position.ABSOLUTE);
        searchField.getStyle().setHeight("10px");
        searchField.getStyle().setWidth("300px");
        searchField.setPlaceholder("חיפוש");
        searchField.getStyle().set("direction", "rtl");
        searchField.getStyle().setAlignItems(Style.AlignItems.CENTER);
        searchField.getStyle().setLeft("620px");

        // Avatar
        Avatar avatar = new Avatar("User");
        avatar.setHeight("35px");
        avatar.setWidth("35px");
        avatar.getStyle().setLeft("10px");



        logout = new Button("התנתק");
        logout.getStyle().setPosition(Style.Position.ABSOLUTE);
        logout.getStyle().setHeight("35px");
        logout.getStyle().setWidth("85px");
        logout.getStyle().setLeft("60px");
        logout.addClickListener(buttonClickEvent -> {
            securityService.logout();
        });

        newCustomer = new Button("חיתוך חדש");
        newCustomer.getStyle().setPosition(Style.Position.ABSOLUTE);
        newCustomer.getStyle().setHeight("35px");
        newCustomer.getStyle().setWidth("105px");
        newCustomer.getStyle().setLeft("165px");
        newCustomer.addClickListener(buttonClickEvent -> {
           getUI().ifPresent(ui -> ui.navigate("Home"));
        });


        header.add(avatar,logout,newCustomer,searchField,menuBar);

        return header;
    }

    /**
     * Main layout handling
     */
    public void resetMainLayout() {
        mainLayout.removeAll();
        mainLayout.add(whatsNew(), graphs(), incomeAndOutCome());
    }
    public void setMainLayout(Component content){
        mainLayout.removeAll();
        mainLayout.add(content);
    }
    public Div whatsNew(){

        Div div = new Div();


        div.getStyle().setPosition(Style.Position.ABSOLUTE);
        div.getStyle().setHeight("200px");
        div.getStyle().setWidth("450px");
        div.getStyle().setBackground("#00ff59");
        div.getStyle().setLeft("30px");
        div.setText("כאן יהיה הדרכה במערכת");

        return div;
    }
    public Div graphs(){

        Div div = new Div();

        div.getStyle().setTop("410px");
        div.getStyle().setPosition(Style.Position.ABSOLUTE);
        div.getStyle().setHeight("200px");
        div.getStyle().setWidth("450px");
        div.getStyle().setBackground("#00ff59");
        div.getStyle().setLeft("30px");
        div.setText("כאן יהיה גרף");

        return div;
    }
    public Div incomeAndOutCome(){

        Div div = new Div();
        div.setText("כאן הוצאות והכנסות");

        div.getStyle().setPosition(Style.Position.ABSOLUTE);
        div.getStyle().setTop("150px");
        div.getStyle().setRight("50px");
        div.getStyle().setWidth("400px");
        div.getStyle().setHeight("200px");
        div.getStyle().setBackground("#00ff59");
        div.getStyle().setBorderRadius("10px");
        div.getStyle().setPadding("20px");
        div.getStyle().setBoxSizing(Style.BoxSizing.BORDER_BOX);


        div.getStyle().setColor("white");
        div.getStyle().setFontSize("24px");
        div.getStyle().setFontWeight("bold");
        div.getStyle().setTextAlign(Style.TextAlign.CENTER);

        return div;
    }
}