package com.dn.projectdashboard.Interceptor;

import com.dn.projectdashboard.Service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GraphQLInterceptor implements WebGraphQlInterceptor {
    private SessionService sessionService;

    private final String[] publicOperations = {"login", "register"};

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String operationName = request.getDocument();

        // Check if operation is public
        if (isPublicOperation(operationName)) {
            return chain.next(request);
        }

        // Extract token from headers
        String token = request.getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || !sessionService.isValidSession(token)) {
            if (token == null) {
                System.out.println("token is null");
            }
            return Mono.error(new RuntimeException("Authentication required"));
        }

        // Add user info to context
        Integer userId = sessionService.getUserIdFromToken(token);
        String username = sessionService.getUsernameFromToken(token);

        String finalToken = token;

        request.configureExecutionInput((executionInput, builder) ->
                builder.graphQLContext(context -> {
                    context.put("userId", userId);
                    context.put("username", username);
                    context.put("token", finalToken);
                }).build()
        );

        return chain.next(request);
    }

    private boolean isPublicOperation(String document) {
        if (document == null) return false;

        for (String operation : publicOperations) {
            if (document.contains(operation)) {
                return true;
            }
        }
        return false;
    }
}