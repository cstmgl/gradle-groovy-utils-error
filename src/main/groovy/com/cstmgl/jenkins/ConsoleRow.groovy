package com.cstmgl.jenkins

class ConsoleRow implements Comparable {

    static final String ERROR_PATTERN = 'ERROR:'
    static final String WARN_PATTERN = 'WARN:'
    static final String INFO_PATTERN = 'INFO:'
    static final String DEBUG_PATTERN = 'DEBUG:'

    final Integer priority
    final String rowStatus
    final String message
    final String rowReportClass

    ConsoleRow(Integer priority, String status, String msg, String rowC) {
        this.priority = priority
        this.rowStatus = status
        this.message = msg
        this.rowReportClass = rowC
    }

    @Override
    int compareTo(Object o) {
        return this.priority <=> ((ConsoleRow) o).priority
    }

    static ConsoleRow ERROR(String msg) {
        return new ConsoleRow(1, 'ERROR', msg, 'isa_error')
    }

    static ConsoleRow WARN(String msg) {
        return new ConsoleRow(2, 'WARN', msg, 'isa_warning')
    }

    static ConsoleRow INFO(String msg) {
        return new ConsoleRow(3, 'INFO', msg, 'isa_info')
    }

    static ConsoleRow DEBUG(String msg) {
        return new ConsoleRow(4, 'DEBUG', msg, 'isa_debug')
    }

}
