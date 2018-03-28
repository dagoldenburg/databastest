package DB.Model;

public class Transaction {

    private double amount;
    private String to;
    private String from;
    private int transactionId;

    public Transaction(double amount, String to, String from, int transactionId) {
        this.amount = amount;
        this.to = to;
        this.from = from;
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", transactionId=" + transactionId +
                '}';
    }
}
