package accounts;

import database.DBService;

/**
 * Class used for return instance {@link AccountService}
 *
 * @autor Alex
 */
public class FactoryAccountService {

    /** Database instance for {@link AccountService} interface */
    public static final AccountServiceDB accountServiceDB = new AccountServiceDB(new DBService());

    /**
     * Factory method, return instance
     * for {@link AccountService} interface
     *
     * @return instance for AccountService interface
     */
    public static AccountService getAccountService() {
        return accountServiceDB;
    }
}
