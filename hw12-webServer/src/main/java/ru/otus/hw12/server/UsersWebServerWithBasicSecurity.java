package ru.otus.hw12.server;

import com.google.gson.Gson;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.hw12.core.service.DBServiceUser;
import ru.otus.hw12.services.TemplateProcessor;

import java.util.ArrayList;
import java.util.List;

public class UsersWebServerWithBasicSecurity extends UsersWebServerSimple {
    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final String CONSTRAINT_NAME = "auth";
    private static final String ADMIN_CONSTRAINT_NAME = "admin_auth";
    private static final String ADMIN_PATH = "/admin";
    private static final String USERS_PATH = "/users";
    private static final String CREATE_USER_API_PATH = "/api/user";
    private static final String GET_USER_API_PATH = "/api/user/*";

    private final LoginService loginService;

    public UsersWebServerWithBasicSecurity(int port,
                                           LoginService loginService,
                                           DBServiceUser dbServiceUser,
                                           Gson gson,
                                           TemplateProcessor templateProcessor) {
        super(port, dbServiceUser, gson, templateProcessor);
        this.loginService = loginService;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler) {
        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        constraintMappings.add(createConstraintMapping(USERS_PATH, CONSTRAINT_NAME, ROLE_NAME_ADMIN, ROLE_NAME_USER));
        constraintMappings.add(createConstraintMapping(ADMIN_PATH, ADMIN_CONSTRAINT_NAME, ROLE_NAME_ADMIN));

        constraintMappings.add(createConstraintMapping(GET_USER_API_PATH, CONSTRAINT_NAME, ROLE_NAME_ADMIN, ROLE_NAME_USER));
        constraintMappings.add(createConstraintMapping(CREATE_USER_API_PATH, ADMIN_CONSTRAINT_NAME, ROLE_NAME_ADMIN));

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        //как декодировать стороку с юзером:паролем https://www.base64decode.org/
        security.setAuthenticator(new BasicAuthenticator());

        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(servletContextHandler));
        return security;
    }

    private ConstraintMapping createConstraintMapping(String path, String constraintName, String... roles) {
        Constraint constraint = new Constraint();
        constraint.setName(constraintName);
        constraint.setRoles(roles);
        constraint.setAuthenticate(true);
        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec(path);
        constraintMapping.setConstraint(constraint);
        return constraintMapping;
    }
}
