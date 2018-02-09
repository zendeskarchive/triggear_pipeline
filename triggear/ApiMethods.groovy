package com.futuresimple.triggear

enum ApiMethods {
    CLEAR('clear'),
    DEREGISTER('deregister'),
    MISSING('missing'),
    REGISTER('register'),
    COMMENT('comment'),
    STATUS('status'),
    DEPLOYMENT('deployment'),
    DEPLOYMENT_STATUS('deployment_status')

    ApiMethods(String methodName) {
        this.methodName = methodName
    }
    private String methodName

    String getMethodName() {
        return methodName
    }
}