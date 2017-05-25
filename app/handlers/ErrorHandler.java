package handlers;

import play.http.HttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler implements HttpErrorHandler {
    @Override
    public CompletionStage<Result> onClientError(Http.RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(
                Results.status(statusCode, "A client error occurred. Please try again later.")
        );
    }

    @Override
    public CompletionStage<Result> onServerError(Http.RequestHeader request, Throwable exception) {
        return CompletableFuture.completedFuture(
                Results.internalServerError("A server error occurred. Please try again later.")
        );
    }
}
