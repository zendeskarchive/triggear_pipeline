package com.futuresimple.triggear

enum PipelineParameters {
    BRANCH('branch'),
    SHA('sha'),
    TAG('tag'),
    CHANGES('changes'),
    IS_PRERELEASE('is_prerelease'),
    RELEASE_TARGET('release_target')

    PipelineParameters(String requestParam) {
        this.requestParam = requestParam
    }
    private final String requestParam

    String getRequestParam() {
        return requestParam
    }
}
