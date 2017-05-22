package models;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Currency currency;

    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
