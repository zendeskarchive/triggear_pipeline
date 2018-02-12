package com.futuresimple.triggear

enum TriggeringEvent {
    LABEL('labeled'),
    PUSH('push'),
    TAG('tagged'),
    PR_OPEN('opened'),
    RELEASE('release')

    TriggeringEvent(String eventName) {
        this.eventName = eventName
    }
    private final String eventName

    String getEventName() {
        return eventName
    }
}
