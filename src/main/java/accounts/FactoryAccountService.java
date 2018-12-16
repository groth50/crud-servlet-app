package accounts;

import database.DBService;

public class FactoryAccountService {
    public static final AccountServiceDB accountServiceDB = new AccountServiceDB(new DBService());

    public static AccountService getAccountService() {
        return accountServiceDB;
    }
}
