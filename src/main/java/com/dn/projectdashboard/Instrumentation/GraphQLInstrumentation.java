package com.dn.projectdashboard.Instrumentation;

import com.dn.projectdashboard.Service.SessionService;
import graphql.ExecutionResult;
import graphql.GraphQLContext;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.SimplePerformantInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters;
import graphql.language.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class GraphQLInstrumentation extends SimplePerformantInstrumentation {
    private SessionService sessionService;

    private final String[] publicOperations = {"login", "register", "validateToken"};
    private final String[] validationOperations = {"tokenValidity"};

    @Override
    public InstrumentationContext<ExecutionResult> beginExecuteOperation(InstrumentationExecuteOperationParameters parameters, InstrumentationState state) {
        GraphQLContext context = parameters.getExecutionContext().getGraphQLContext();
        Document document = parameters.getExecutionContext().getDocument();
        List<String> fields = new ArrayList<>();

        for (Definition<?> definition : document.getDefinitions()) {
            if (definition instanceof OperationDefinition) {
                OperationDefinition operation = (OperationDefinition) definition;
                fields.addAll(extractFields(operation.getSelectionSet(), ""));
            }
        }

        if (fields.isEmpty()) return SimpleInstrumentationContext.noOp();


        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        Boolean containsValidationOfAccessToken = null;
        boolean containsValidationOfRefreshToken = false;

        for (String field : fields) {
            if (isPublicOperation(field)) containsValidationOfAccessToken = true;
            if (field.contains("validateToken")) containsValidationOfRefreshToken = true;
        }

        if (containsValidationOfAccessToken == null) return SimpleInstrumentationContext.noOp();
        if (!containsValidationOfAccessToken) return super.beginExecuteOperation(parameters, state);
        if (!containsValidationOfRefreshToken && !containsValidationOfAccessToken) return super.beginExecuteOperation(parameters, state);
        if (request == null) return SimpleInstrumentationContext.noOp();

        String authHeader = request.getHeader("Authorization");

        // If needed, parse and manually authenticate the JWT here
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return SimpleInstrumentationContext.noOp();

        String token = authHeader.substring(7);

        if (containsValidationOfRefreshToken) {
            String tokenFromCookie = getTokenFromCookie(request.getCookies());
            Boolean isValidSession = sessionService.isValidSession(tokenFromCookie);
            if (isValidSession == null || !isValidSession)
                return SimpleInstrumentationContext.noOp();
            System.out.println("Adding Refresh Token");
            context.put("refreshToken", tokenFromCookie);
            context.put("token", token);
            return super.beginExecuteOperation(parameters, state);
        }

        // Extract token from headers
        Boolean isValidSession = sessionService.isValidSession(token);

        if (token == null || isValidSession == null || !isValidSession) return SimpleInstrumentationContext.noOp();

        // Add user info to context
        Integer userId = sessionService.getUserIdFromToken(token);
        String username = sessionService.getUsernameFromToken(token);


        context.put("token", token);
        context.put("userId", userId);
        context.put("username", username);

        return super.beginExecuteOperation(parameters, state);
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

    private String getTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("DNPD_AUTH_TOKEN")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private List<String> extractFields(SelectionSet selectionSet, String prefix) {
        if (selectionSet == null) return null;

        List<String> fields = new ArrayList<>(0);

        for (Selection<?> selection : selectionSet.getSelections()) {
            if (selection instanceof Field) {
                Field field = (Field) selection;
                String fieldName = prefix + field.getName();
                fields.add(fieldName);
            }
        }
        return fields;
    }
}
