package com.futuresimple.triggear

enum PipelineParameters {
    BRANCH('branch'),
    SHA('sha'),
    TAG('tag')

    PipelineParameters(String requestParam) {
        this.requestParam = requestParam
    }
    private final String requestParam

    String getRequestParam() {
        return requestParam
    }
}
