package hw16.messagesystem;

import hw16.core.service.DBServiceUser;
import hw16.core.service.handlers.DeleteUserDataRequestHandler;
import hw16.core.service.handlers.GetAllUsersDataRequestHandler;
import hw16.core.service.handlers.SaveUserDataRequestHandler;
import hw16.messagesystem.client.CallbackRegistry;
import hw16.messagesystem.client.CallbackRegistryImpl;
import hw16.messagesystem.client.MsClient;
import hw16.messagesystem.client.MsClientImpl;
import hw16.messagesystem.message.MessageType;
import hw16.services.front.handlers.SimpleUserDataResponseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSystemConfiguration {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public static String getDatabaseServiceClientName() {
        return DATABASE_SERVICE_CLIENT_NAME;
    }

    @Bean
    public MessageSystem messageSystem() {
        MessageSystem messageSystem = new MessageSystemImpl();
        messageSystem.start();
        return messageSystem;
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("requestHandlerDatabaseStore")
    public HandlersStore requestHandlerDatabaseStore(DBServiceUser dbServiceUser) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersDataRequestHandler(dbServiceUser));
        requestHandlerDatabaseStore.addHandler(MessageType.DELETE_USER_DATA, new DeleteUserDataRequestHandler(dbServiceUser));
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_USER_DATA, new SaveUserDataRequestHandler(dbServiceUser));
        return requestHandlerDatabaseStore;
    }

    @Bean("requestHandlerFrontendStore")
    public HandlersStore requestHandlerFrontendStore(CallbackRegistry callbackRegistry) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.ALL_USERS_DATA, new SimpleUserDataResponseHandler(callbackRegistry));
        requestHandlerFrontendStore.addHandler(MessageType.DELETE_USER_DATA, new SimpleUserDataResponseHandler(callbackRegistry));
        requestHandlerFrontendStore.addHandler(MessageType.SAVE_USER_DATA, new SimpleUserDataResponseHandler(callbackRegistry));
        return requestHandlerFrontendStore;
    }

    @Bean("databaseMsClient")
    public MsClient databaseMsClient(MessageSystem messageSystem, HandlersStore requestHandlerDatabaseStore, CallbackRegistry callbackRegistry) {
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean("frontendMsClient")
    public MsClient frontendMsClient(MessageSystem messageSystem, HandlersStore requestHandlerFrontendStore, CallbackRegistry callbackRegistry) {
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }


}
