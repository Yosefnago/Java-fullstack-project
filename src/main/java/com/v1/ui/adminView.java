package com.v1.ui;





import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("admin")
public class adminView extends VerticalLayout  {

    BeforeEnterEvent beforeEnterEvent;
    public adminView() {
        setupUI();
    }
    private void setupUI() {
        H1 header = new H1("Welcome, Admin!");
        add(header);
    }
}




