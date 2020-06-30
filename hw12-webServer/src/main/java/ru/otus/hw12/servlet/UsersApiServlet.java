package ru.otus.hw12.servlet;

import com.google.gson.Gson;
import ru.otus.hw12.core.model.AddressDataSet;
import ru.otus.hw12.core.model.PhoneDataSet;
import ru.otus.hw12.core.model.User;
import ru.otus.hw12.core.service.DBServiceUser;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;


public class UsersApiServlet extends HttpServlet {
    private static final int ID_PATH_PARAM_POSITION = 1;
    private static final String ADMIN_PAGE = "/admin";
    private static final String NAME = "userName";
    private static final String LOGIN = "userLogin";
    private static final String PASSWORD = "userPassword";
    private static final String ADDRESS = "userAddress";
    private static final String PHONES = "userPhones";
    private static final String PHONES_SEPARATOR = ",";
    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var newUser = new User();
        newUser.setName(req.getParameter(NAME));
        newUser.setLogin(req.getParameter(LOGIN));
        newUser.setPassword(req.getParameter(PASSWORD));
        var addressString = req.getParameter(ADDRESS);
        if (addressString != null && !addressString.isEmpty()) {
            newUser.setAddress(new AddressDataSet(UUID.randomUUID().toString(), addressString));
        }
        var phonesString = req.getParameter(PHONES);
        if (addressString != null && !addressString.isEmpty()) {
            newUser.setPhones(Arrays.stream(phonesString.split(PHONES_SEPARATOR))
                    .map(x -> new PhoneDataSet(UUID.randomUUID().toString(), x)).collect(Collectors.toSet()));
        }
        dbServiceUser.saveUser(newUser);
        resp.sendRedirect(ADMIN_PAGE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = dbServiceUser.getUser(extractIdFromRequest(request)).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }

}
