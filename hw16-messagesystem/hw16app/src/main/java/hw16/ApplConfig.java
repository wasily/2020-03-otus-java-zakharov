package hw16;

import hw16.core.service.DBServiceUser;
import hw16.core.service.handlers.DeleteUserDataRequestHandler;
import hw16.core.service.handlers.GetAllUsersDataRequestHandler;
import hw16.core.service.handlers.SaveUserDataRequestHandler;
import hw16.front.FrontendService;
import hw16.front.FrontendServiceImpl;
import hw16.front.handlers.SimpleUserDataResponseHandler;
import lombok.Setter;
import messagesystem.HandlersStore;
import messagesystem.HandlersStoreImpl;
import messagesystem.MessageSystem;
import messagesystem.MessageSystemImpl;
import messagesystem.client.CallbackRegistry;
import messagesystem.client.CallbackRegistryImpl;
import messagesystem.client.MsClient;
import messagesystem.client.MsClientImpl;
import messagesystem.message.MessageType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@ConfigurationProperties(prefix = "clients")
@EnableWebSocketMessageBroker
public class ApplConfig implements WebSocketMessageBrokerConfigurer {
    @Setter
    private String frontendServiceClientName;
    @Setter
    private String databaseServiceClientName;

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
    public FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient, databaseServiceClientName);
    }

    @Bean(destroyMethod = "dispose")
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
        MsClient databaseMsClient = new MsClientImpl(databaseServiceClientName, messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean("frontendMsClient")
    public MsClient frontendMsClient(MessageSystem messageSystem, HandlersStore requestHandlerFrontendStore, CallbackRegistry callbackRegistry) {
        MsClient frontendMsClient = new MsClientImpl(frontendServiceClientName, messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }
}
