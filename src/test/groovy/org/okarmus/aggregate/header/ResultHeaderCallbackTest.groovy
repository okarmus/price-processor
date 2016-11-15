package org.okarmus.aggregate.header

import spock.lang.Specification

/**
 * Created by mateusz on 14.11.16.
 */

class ResultHeaderCallbackTest extends Specification {

    String sampleHeader = "this is a sample header"
    ResultHeaderCallback underTest = new ResultHeaderCallback(header: sampleHeader)
    Writer writer = Mock()

    def "header should be writen to mock"() {
        when:
            underTest.writeHeader(writer)
        then:
            1 * writer.write(sampleHeader)
    }
}
