package com.phoenixplaydev.musicapp.exceptions;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Component
public class GraphQLExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        GraphQLError error = GraphqlErrorBuilder.newError()
                .message(exception.getMessage())
                .extensions(new HashMap<>() {{
                    put("StackTrace", exception.getStackTrace());
                }})
                .location(handlerParameters.getSourceLocation())
                .build();

        DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult
                .newResult()
                .error(error)
                .build();

        return CompletableFuture.completedFuture(result);
    }

}
