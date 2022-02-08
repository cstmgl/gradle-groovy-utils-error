package com.cstmgl.jenkins


import spock.lang.Specification

class ConsoleRowTest extends Specification {
    def "compareTo returns 0"() {
        setup:
        def err = ConsoleRow.ERROR('msgE')

        when:
        def result = err <=> err

        then:
        result == 0
    }

    def "err > warn"() {
        setup:
        def err = ConsoleRow.ERROR('msgE')
        def warn = ConsoleRow.WARN('msgW')

        when:
        def result = err <=> warn

        then:
        result < 0
    }

    def "warn > info"() {
        setup:
        def info = ConsoleRow.INFO('msgI')
        def warn = ConsoleRow.WARN('msgW')

        when:
        def result = info <=> warn

        then:
        result > 0
    }

    def "list is sorted"() {
        setup:
        def info = ConsoleRow.INFO('msgI')
        def warn = ConsoleRow.WARN('msgW')
        def err = ConsoleRow.ERROR('msgE')
        List<ConsoleRow> list = []
        list.push(info)
        list.push(warn)
        list.push(err)

        when:
        list.sort()

        then:
        list.get(0).rowStatus == 'ERROR'
    }

}