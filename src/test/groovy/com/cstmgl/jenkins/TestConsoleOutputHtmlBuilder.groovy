package com.cstmgl.jenkins


import spock.lang.Specification

class TestConsoleOutputHtmlBuilder extends Specification {

    def "processFile works"() {
        setup:
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger('')
        def console = new ConsoleOutputHtmlBuilder(logger)
        def consoleFile = this.getClass().getResource('/console.test').getPath()

        when:
        def result = console.processOutput(consoleFile, false)

        then:
        result.size() == 3 // we ignore debugs
    }

    def "processFile also debug works"() {
        setup:
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger('')
        def console = new ConsoleOutputHtmlBuilder(logger)
        def consoleFile = this.getClass().getResource('/console.test').getPath()

        when:
        def result = console.processOutput(consoleFile, false, false)

        then:
        result.size() == 4
    }

    def "processFile without sort works"() {
        setup:
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger('')
        def console = new ConsoleOutputHtmlBuilder(logger)
        def consoleFile = this.getClass().getResource('/console.test').getPath()

        when:
        List<ConsoleRow> result = console.processOutput(consoleFile, false)

        then:
        result[0].rowStatus == 'INFO'
        result[1].rowStatus == 'WARN'
        result[2].rowStatus == 'ERROR'
    }

    def "processFile with sort works"() {
        setup:
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger('')
        def console = new ConsoleOutputHtmlBuilder(logger)
        def consoleFile = this.getClass().getResource('/console.test').getPath()

        when:
        List<ConsoleRow> result = console.processOutput(consoleFile)

        then:
        result[0].rowStatus == 'ERROR'
        result[1].rowStatus == 'WARN'
        result[2].rowStatus == 'INFO'
    }

    def "buildReport works"() {
        setup:
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger('')
        def console = new ConsoleOutputHtmlBuilder(logger)
        def consoleFile = this.getClass().getResource('/console.test').getPath()

        when:
        String result = console.buildReport('build', 'TEST_DB', consoleFile)
        File f = new File(result)

        then:
        f.exists() && f.isFile() && !f.isDirectory()
    }

}
