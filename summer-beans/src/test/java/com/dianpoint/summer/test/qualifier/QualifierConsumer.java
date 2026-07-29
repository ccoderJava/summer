package com.dianpoint.summer.test.qualifier;

import com.dianpoint.summer.beans.factory.annotation.Autowired;
import com.dianpoint.summer.beans.factory.annotation.Qualifier;

public class QualifierConsumer {

    @Autowired
    @Qualifier("databaseService")
    private DatabaseService dbService;

    @Autowired
    private UserService userService;

    public DatabaseService getDbService() {
        return dbService;
    }

    public UserService getUserService() {
        return userService;
    }
}
