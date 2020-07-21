package ru.otus.hw12.servlet;

import ru.otus.hw12.core.service.DBServiceUser;
import ru.otus.hw12.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AdminServlet extends HttpServlet {
    private static final String TEMPLATE_ATTR_USER_LIST = "userList";
    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";

    private final DBServiceUser dbServiceUser;
    private final TemplateProcessor templateProcessor;

    public AdminServlet(TemplateProcessor templateProcessor, DBServiceUser dbServiceUser) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_USER_LIST, dbServiceUser.getAllUsers());
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, paramsMap));
    }

}
