package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    private BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  //      PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>());
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
//---------------------------------------------------------------------------------------------
/*
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8"); //

        BankClientService bankClientService = new BankClientService();


        Map<String, Object> messageForm = new HashMap<>();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        long money = Long.parseLong(req.getParameter("money"));

        BankClient newClient = new BankClient(name, password, money);

    //    if (new BankClientService().getClientByName(name) == null) {

            if (bankClientService.addClient(newClient)){
            messageForm.put("message", "Add client successful");}
        else {
            messageForm.put("message", "Client not add");
        }

        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", messageForm));
    //    PageGenerator.getInstance().getPage("resultPage.html", messageForm);
        resp.setStatus(HttpServletResponse.SC_OK);

    }
*/

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        Map<String, Object> messages = new HashMap<>();

        // form data
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        long money = Long.parseLong(req.getParameter("money"));
        // new client
        BankClient client = new BankClient(name, pass, money);

        if (bankClientService.addClient(client)) {
            messages.put("message", "Add client successful");
        } else {
            messages.put("message", "Client not add");
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", messages));

    }











}