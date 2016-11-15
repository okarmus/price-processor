package org.okarmus.aggregate

import org.okarmus.aggregate.step.AggregateDataStepConfiguration
import org.okarmus.transaction.domain.Transaction
import org.okarmus.transaction.repository.TransactionRepository
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.mockito.Mockito.when
import static org.springframework.batch.core.BatchStatus.COMPLETED

/**
 * Created by mateusz on 14.11.16.
 */
@ActiveProfiles("batchtest")
@TestPropertySource(properties = ["matchingPath = aggregate/matchingTest.csv", "resultPath = resultTest.csv", "ignoredCount = false", "currency = PLN", "header = sample header"])
@ContextConfiguration(classes = [AggregateJobTestConfiguration.class, AggregateDataStepConfiguration.class])
class AggregateJobIntegrationTest extends Specification {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    private TransactionRepository transactionRepository

    @Value('${resultPath}')
    private String resultFilePath;

    private List<Transaction> transactionsForMatchingId1 = [new Transaction(id: 1, totalValue: 4500.00, quantity: 2, matchingId: 1), new Transaction(id: 2, totalValue: 525.00, quantity: 1, matchingId: 1), new Transaction(id: 3, totalValue: 2000.00, quantity: 1, matchingId: 1),]
    private List<Transaction> transactionsForMatchingId2 = [new Transaction(id: 4, totalValue: 7875.00, quantity: 2, matchingId: 2)]
    private List<String> expectedResult = ["sample header", "1,11000.0,3666.67,PLN,false", "2,15750.0,7875.0,PLN,false"]

    def "should aggregate data and save them in file"() {
        given:
            mockRepository()
        when:
            JobExecution jobExecution = jobLauncherTestUtils.launchJob()
        then:
            jobExecution.getStatus() == COMPLETED
            def resultFile = new File(resultFilePath)
            resultFile.readLines() == expectedResult
        cleanup:
            resultFile.delete()
    }

    def mockRepository() {
        when(transactionRepository.findByMatchingId(1)).thenReturn(transactionsForMatchingId1)
        when(transactionRepository.findByMatchingId(2)).thenReturn(transactionsForMatchingId2)
    }
}
