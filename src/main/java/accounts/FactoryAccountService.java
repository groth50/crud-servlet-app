package accounts;

public class FactoryAccountService {
    public static final AccountServiceMock accountServiceMock = new AccountServiceMock();
    public static final AccountServiceDB accountServiceDB = new AccountServiceDB(new DBService());

    public static AccountService getAccountService() {
        return accountServiceDB;
    }
}
