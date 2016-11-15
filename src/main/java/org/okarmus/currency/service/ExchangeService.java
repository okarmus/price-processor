package org.okarmus.currency.service;

import org.okarmus.currency.domain.Currency;
import org.okarmus.currency.exception.CurrencyNotFoundException;
import org.okarmus.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Optional.*;

/**
 * Created by mateusz on 11.11.16.
 */
@Service
public class ExchangeService {

    private static final int PRECISION = 2;
    private static final String errorMessage = "Currency %s not found";

    @Autowired
    private CurrencyRepository currencyRepository;

    public BigDecimal exchange(String src, String dst, BigDecimal amount) {
       BigDecimal srcRate = findByCurrency(src);
       BigDecimal dstRate = findByCurrency(dst);

        return amount.multiply(srcRate).divide(dstRate, PRECISION, HALF_UP);
    }

    private BigDecimal findByCurrency(String currency) {
        return ofNullable(currencyRepository.findByCurrency(currency))
                                         .map(Currency::getRatio)
                                         .orElseThrow(() -> new CurrencyNotFoundException(String.format(errorMessage, currency)));
    }
}
