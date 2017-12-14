package com.futuresimple.triggear

enum ApiMethods {
    CLEAR('clear'),
    DEREGISTER('deregister'),
    MISSING('missing'),
    REGISTER('register'),
    COMMENT('comment'),
    STATUS('status')

    ApiMethods(String methodName) {
        this.methodName = methodName
    }
    private String methodName

    String getMethodName() {
        return methodName
    }
}