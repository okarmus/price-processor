package org.okarmus.currency.repository;

import org.okarmus.currency.domain.Currency;
import org.springframework.data.repository.Repository;


/**
 * Created by mateusz on 10.11.16.
 */
public interface CurrencyRepository extends Repository<Currency, String> {
    Currency findByCurrency(String currency);
}
