package dao;

//import com.sun.deploy.util.SessionState; ???????????? не грузится
import exception.DBException;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() {// throws SQLException - зачем throws SQLe????????
        List<BankClient> allBankClients = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {

            stmt.execute("SELECT * FROM bank_client");
            ResultSet result = stmt.getResultSet();
            while (result.next()) {
                long idClient = result.getLong(1);
                String nameClient = result.getString(2);
                String passwordClient = result.getString(3);
                long moneyClient = result.getLong(4);
                allBankClients.add(new BankClient(idClient, nameClient, passwordClient, moneyClient));
            }
        result.close();
       // stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

        return allBankClients;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        BankClient bankClient = getClientByName(name); //присвоить переменной клиента,найденного по ИМЕНИ
        if (bankClient.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        //if (validateClient(name, password)) {
        BankClient bankClient = getClientByName(name);
        long updatedMoney = bankClient.getMoney() + transactValue;
        String requestSQL = "UPDATE bank_client SET money = ? WHERE name = ?";
        PreparedStatement preStmt = connection.prepareStatement(requestSQL);

        preStmt.setLong(1, updatedMoney);
        preStmt.setString(2, bankClient.getName());
        preStmt.executeUpdate();
        preStmt.close();

    }



    public BankClient getClientById(long id) /*throws SQLException*/ {
   try {
        Statement stmt = connection.createStatement();
        stmt.execute("SELECT * FROM bank_clien WHERE id ='" + id + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        long idClient = result.getLong(1);
        String nameClient = result.getString(2);
        String passwordClient = result.getString(3);
        Long moneyClient = result.getLong(4);
        BankClient bankClient = new BankClient(idClient, nameClient, passwordClient, moneyClient);
        result.close();
        stmt.close();

        return bankClient;
    } catch (SQLException e) {
        return null;
    }
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        BankClient bankClient = getClientByName(name); //
        if (bankClient.getMoney() >= expectedSum) {
            return true;
        } else {
            return false;
        }
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("SELECT * FROM bank_client WHERE name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) /*throws SQLException*/ { // Добавил throws SQLException или НЕТ???
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("SELECT * FROM bank_client WHERE name='" + name + "'");
            ResultSet result = stmt.getResultSet();
            result.next();

            Long idClient = null;

            idClient = result.getLong(1);
            String nameClient = result.getString(2);
            String passwordClient = result.getString(3);
            long moneyClient = result.getLong(4);
            BankClient bankClient = new BankClient(idClient, nameClient, passwordClient, moneyClient);

            result.close();
            stmt.close();

            return bankClient;


        } catch (SQLException e) {
            return null;
        }

    }

 //   public void addClient(BankClient client) /*throws DBException*/ {

/*

        if (getClientByName(client.getName()) == null) {
            String requestSQL = "INSERT INTO bank_client (name, password, money) VALUES (?, ?, ?)";
            PreparedStatement preStmt;
            try {

                preStmt = connection.prepareStatement(requestSQL);

                preStmt.setString(1, client.getName());
                preStmt.setString(2, client.getPassword());
                preStmt.setLong(3, client.getMoney());

                preStmt.executeUpdate();
                preStmt.close();
            } catch (SQLException e) {
              //  e.printStackTrace();
            }
        }
    }
     */
/////////////////////////////////////////////////////////////////

    public void addClient(BankClient client) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO bank_client (name, password, money) VALUES (?, ?, ?)")) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPassword());
            stmt.setLong(3, client.getMoney());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
///////////////////////////////////////////////////////////


    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE if NOT EXISTS bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE if EXISTS bank_client");
        stmt.close();
    }


    // add deleteClient to BankClientDAO
    public void deleteClient(String name) /*throws SQLException*/ {// void or boolean
        BankClient bankClient = getClientByName(name); // а проверка имени клиента??? в Сервисе?
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE * FROM bank_clien WHERE name='" + name + "'");
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

/*
    // add sendMoneyToClient to BankClientDAO
    public boolean sendMoneyToClient(BankClient sender, String name, long value) throws SQLException {
        if (validateClient(sender.getName(), sender.getPassword())) {
            if (isClientHasSum(sender.getName(), value)) {
                BankClient receiver = getClientByName(name);

                updateClientsMoney(sender.getName(), sender.getPassword(), -value);
                updateClientsMoney(receiver.getName(), receiver.getPassword(), value);



            } else {
                return false;// когда нет денег...
            }
        } else {
            return false; // клиент не прошёл проверку
        }
        return false;



    }

*/


}
