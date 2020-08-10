package hw16;

import hw16.core.service.DBServiceUser;
import hw16.core.service.handlers.DeleteUserDataRequestHandler;
import hw16.core.service.handlers.GetAllUsersDataRequestHandler;
import hw16.core.service.handlers.SaveUserDataRequestHandler;
import hw16.messagesystem.HandlersStore;
import hw16.messagesystem.HandlersStoreImpl;
import hw16.messagesystem.MessageSystem;
import hw16.messagesystem.MessageSystemImpl;
import hw16.messagesystem.client.CallbackRegistry;
import hw16.messagesystem.client.CallbackRegistryImpl;
import hw16.messagesystem.client.MsClient;
import hw16.messagesystem.client.MsClientImpl;
import hw16.messagesystem.message.MessageType;
import hw16.services.front.FrontendService;
import hw16.services.front.FrontendServiceImpl;
import hw16.services.front.handlers.SimpleUserDataResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ApplConfig implements WebSocketMessageBrokerConfigurer {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    private final DBServiceUser dbServiceUser;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/users-websocket").withSockJS();
    }

    @Bean
    public MessageSystem messageSystem() {
        MessageSystem messageSystem = new MessageSystemImpl();
        messageSystem.start();
        return messageSystem;
    }

    @Bean
    public FrontendService frontendService(MessageSystem messageSystem) {
        CallbackRegistry callbackRegistry = new CallbackRegistryImpl();
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.ALL_USERS_DATA, new GetAllUsersDataRequestHandler(dbServiceUser));
        requestHandlerDatabaseStore.addHandler(MessageType.DELETE_USER_DATA, new DeleteUserDataRequestHandler(dbServiceUser));
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_USER_DATA, new SaveUserDataRequestHandler(dbServiceUser));

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.ALL_USERS_DATA, new SimpleUserDataResponseHandler(callbackRegistry));
        requestHandlerFrontendStore.addHandler(MessageType.DELETE_USER_DATA, new SimpleUserDataResponseHandler(callbackRegistry));
        requestHandlerFrontendStore.addHandler(MessageType.SAVE_USER_DATA, new SimpleUserDataResponseHandler(callbackRegistry));

        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem, requestHandlerFrontendStore, callbackRegistry);
        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        messageSystem.addClient(frontendMsClient);

        return frontendService;
    }
}
