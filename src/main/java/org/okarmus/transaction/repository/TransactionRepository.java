package org.okarmus.transaction.repository;

import org.okarmus.transaction.domain.Transaction;
import org.springframework.data.repository.Repository;


import java.util.List;

/**
 * Created by mateusz on 10.11.16.
 */
public interface TransactionRepository extends Repository<Transaction, Integer> {
    List<Transaction> findByMatchingId(int matchingId);
}
