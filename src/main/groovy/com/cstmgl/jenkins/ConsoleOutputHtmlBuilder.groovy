package com.cstmgl.jenkins


import groovy.xml.MarkupBuilder

import java.time.LocalDateTime
import java.time.ZoneOffset

class ConsoleOutputHtmlBuilder {

    org.slf4j.Logger logger
    List<ConsoleRow> list

    ConsoleOutputHtmlBuilder(org.slf4j.Logger logger) {
        this.logger = logger
        this.list = new ArrayList<>()[]
    }

    String buildReport(String outputDir, String env, String filename) {
        list = processOutput(filename)

        LocalDateTime date = LocalDateTime.now()

        GString file = "${outputDir}/${env}_${date.toEpochSecond(ZoneOffset.UTC)}.html"
        logger.info("Rendering Output HTML: ${file}")

        FileWriter xmlWriter = new FileWriter(file)
        MarkupBuilder xmlMarkup = new MarkupBuilder(xmlWriter)
        xmlWriter.write("<!DOCTYPE html>\n")
        xmlMarkup.html() {
            head() {
                meta(charset: "utf-8")
                title("Smoke Tests - ${env}")
                style('''
                    .isa_header, .isa_info, .isa_success, .isa_warning, .isa_error {
                    margin: 10px 0px;
                    padding:12px;

                    }
                    .isa_header {
                        color: #00529B;
                        background-color: #BDE5F8;
                    }
                    .isa_info {
                        color: #adadad;
                        background-color: #d3d3d3;
                    }
                    .isa_success {
                        color: #4F8A10;
                        background-color: #DFF2BF;
                    }
                    .isa_warning {
                        color: #9F6000;
                        background-color: #FEEFB3;
                    }
                    .isa_error {
                        color: #D8000C;
                        background-color: #FFD2D2;
                    }
                ''')
            }
            body(id: 'main') {
                h1() {
                    a(date.format("yyyy-MM-dd HH:mm") + " ${env}")
                }
                table(id: 'reportTable') {
                    thead() {
                        tr(class: 'isa_header') {
                            th('Type')
                            th('Message')
                        }
                    }
                    tbody() {
                        list.each {
                            row ->
                                tr(class: row.rowReportClass) {
                                    td(row.rowStatus)
                                    td(row.message)
                                }
                        }
                    }
                }
            }
        }
        xmlWriter.flush()
        xmlWriter.close()
        return file.toString()
    }

    String getMessage(int index, String pattern, String msg) {
        return msg.substring(index + pattern.length())
    }

    List<ConsoleRow> processOutput(String filename, boolean sort = true, boolean ignoreDebug = true) {
        List<ConsoleRow> outputRows = []
        new File(filename).withReader('UTF-8') { BufferedReader reader ->
            String line
            while ((line = reader.readLine()) != null) {
                int indexError = line.indexOf(ConsoleRow.ERROR_PATTERN)
                int indexWarn = line.indexOf(ConsoleRow.WARN_PATTERN)
                int indexInfo = line.indexOf(ConsoleRow.INFO_PATTERN)
                int indexDebug = line.indexOf(ConsoleRow.DEBUG_PATTERN)
                if (indexError >= 0) {
                    outputRows.add(ConsoleRow.ERROR(getMessage(indexError, ConsoleRow.ERROR_PATTERN, line)))
                } else if (indexWarn >= 0) {
                    outputRows.add(ConsoleRow.WARN(getMessage(indexWarn, ConsoleRow.WARN_PATTERN, line)))
                } else if (indexInfo >= 0) {
                    outputRows.add(ConsoleRow.INFO(getMessage(indexInfo, ConsoleRow.INFO_PATTERN, line)))
                } else if (!ignoreDebug && indexDebug >= 0) {
                    outputRows.add(ConsoleRow.DEBUG(getMessage(indexDebug, ConsoleRow.DEBUG_PATTERN, line)))
                }
            }
        }

        return sort ? outputRows.sort() : outputRows
    }

}
