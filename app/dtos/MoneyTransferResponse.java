package dtos;

public class MoneyTransferResponse {

    public MoneyTransferResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    private boolean successful;
    private String message;

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }
}
