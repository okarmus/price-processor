package org.okarmus.transaction.processor;

import org.okarmus.currency.service.ExchangeService;
import org.okarmus.transaction.domain.Data;
import org.okarmus.transaction.domain.Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by mateusz on 10.11.16.
 */
@Service
public class DataProcessor implements ItemProcessor<Data, Transaction> {

    @Value("${currency}")
    private String dstCurrency;

    private final ExchangeService exchangeService;

    @Autowired
    public DataProcessor(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @Override
    public Transaction process(Data data) throws Exception {
        BigDecimal total = exchangeCurrency(data);
        return new Transaction(data.getId(), total, data.getQuantity(), data.getMatchingId());
    }

    public BigDecimal exchangeCurrency(Data data) {
        String srcCurrency = data.getCurrency();
        BigDecimal amount = data.getPrice();

        return exchangeService.exchange(srcCurrency, dstCurrency, amount);
    }
}
