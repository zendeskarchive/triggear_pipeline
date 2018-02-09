package com.futuresimple.triggear

import groovy.json.JsonOutput

class TriggearClient implements Serializable {
    private Object context
    private GitHubRepository repository

    /**
     * Create a Triggear object for specific repository
     *
     * @param context Pipeline context (a.k.a. steps) - simply pass `this` from pipeline
     * @param repositoryFullName full name of GitHub repository, e.g. "futuresimple/triggear"
     */
    TriggearClient(context, GitHubRepository repository) {
        this.context = context
        this.repository = repository
    }

    /**
     * Add a GitHub status to commit identified by SHA
     *
     * @param sha SHA of the commit to add status to
     * @param state State that should be shown on comment
     * @param description Short description of status
     * @param statusName Name of status to add. JOB_NAME taken by default
     * @param statusUrl URL that this status should direct to. Current BUILD_URL by default
     */
    void addCommitStatus(String sha,
                         CommitStatus state,
                         String description,
                         String statusName = '',
                         String statusUrl = '') {
        statusName = statusName != '' ? statusName : context.env.JOB_NAME
        statusUrl = statusUrl != '' ? statusUrl : context.env.BUILD_URL
        sendRequestToTriggearService(ApiMethods.STATUS,
            [
                sha        : sha,
                repository : repository.getRepositoryFullName(),
                state      : state.getGitHubStateName(),
                description: description,
                url        : statusUrl,
                context    : statusName
            ]
        )
    }

    /**
     * Add a GitHub status to commit identified by SHA based on result of called Closure. If Closure raises exception
     * status is set to failure. It's set to success otherwise.
     *
     * @param sha SHA of the commit to add status to
     * @param statusName Name of status to add
     * @param targetUrl URL that this status should direct to
     * @param description Short description of status
     * @param body Closure to call and check for errors
     */
    void reportStatus(String sha, String statusName, String targetUrl, String description = '', Closure body){
        Boolean passed = false
        try{
            body()
            passed = true
        } finally {
            addCommitStatus(
                sha,
                passed ? CommitStatus.SUCCESS : CommitStatus.FAILURE,
                description != '' ? description : "executed for SHA ${sha[0..6]}",
                statusName,
                targetUrl
            )
        }
    }

    /**
     * Add a GitHub comment to commit identified by SHA
     *
     * @param sha SHA of the commit to add comment to
     * @param body Comment content
     */
    void addComment(String sha,
                    String body){
        sendRequestToTriggearService(ApiMethods.COMMENT,
        [
            sha: sha,
            repository: repository.getRepositoryFullName(),
            jobName: context.env.JOB_NAME,
            body: body
        ])
    }

    /**
     * Get all jobs for specific registration type that were missed at least once when trying to call them.
     * Example: new TriggearClient.getMissing(Request.forPushes().build())
     *
     * @param context Pipeline context (a.k.a. steps) - simply pass `this` from pipeline
     * @param request: Request object. Only eventType specification is required.
     * @return String of strings like job_name#number_of_missed joined with ','.
     */
    static String getMissing(context, Request request){
        return new TriggearClient(context, null).sendRequestToTriggearService(
            ApiMethods.MISSING, [:], 'GET', "/${request.registrationEvent.getEventName()}"
        )
    }

    static String deregister(context, String jobName, Request request){
        return new TriggearClient(context, null).sendRequestToTriggearService(ApiMethods.DEREGISTER,
            [
                jenkins_url : context.env.JENKINS_URL,
                eventType   : request.registrationEvent.getEventName(),
                jobName     : jobName,
                caller      : "${context.env.BUILD_TAG}"
            ]
        )
    }

    static String clearMissedCount(context, String jobName, Request request){
        return new TriggearClient(context, null).sendRequestToTriggearService(ApiMethods.CLEAR,
                [
                    jenkins_url: context.env.JENKINS_URL,
                    eventType: request.registrationEvent.getEventName(),
                    jobName: jobName
                ]
        )
    }

    void addDeployment(String ref,
                       String environment,
                       String description){
        sendRequestToTriggearService(ApiMethods.DEPLOYMENT,
            [
                ref: ref,
                repo: repository.getRepositoryFullName(),
                environment: environment,
                description: description
            ])
    }

    void addDeploymentStatus(String ref,
                             String environment,
                             String description,
                             String targetUrl,
                             DeploymentState state){
        sendRequestToTriggearService(ApiMethods.DEPLOYMENT_STATUS,
            [
                ref: ref,
                repo: repository.getRepositoryFullName(),
                environment: environment,
                description: description,
                state: state.gitHubStateName,
                targetUrl: targetUrl
            ])
    }

    void register(Request request) {
        sendRequestToTriggearService(ApiMethods.REGISTER,
            [
                jenkins_url         : context.env.JENKINS_URL,
                eventType           : request.registrationEvent.getEventName(),
                repository          : repository.getRepositoryFullName(),
                jobName             : context.env.JOB_NAME,
                labels              : request.labels,
                requested_params    : request.requestedParameters.collect { PipelineParameters param, String nameOverride ->
                    nameOverride == null ? param.getRequestParam() : "${param.getRequestParam()}:${nameOverride}"
                },
                branch_restrictions : request.branchRestrictions,
                change_restrictions : request.changeRestrictions,
                file_restrictions   : request.fileRestrictions
            ]
        )
    }

    private String sendRequestToTriggearService(ApiMethods methodName,
                                                Map<String, Object> payload = [:],
                                                String httpMethod = 'POST',
                                                String requestVariable = ''){
        String responseText = ''
        try {
            context.withCredentials([context.string(credentialsId: 'triggear_token', variable: 'triggear_token')]) {
                URLConnection post = new URL("${context.env.TRIGGEAR_URL}${methodName.getMethodName()}${requestVariable}").openConnection()
                context.println("${methodName} call to Triggear service (payload: " + payload + ")")
                post.setRequestMethod(httpMethod)
                post.setDoOutput(true)
                post.setRequestProperty("Content-Type", "application/json")
                post.setRequestProperty("Authorization", "Token ${context.triggear_token}")
                if(payload != [:]) {
                    String payloadAsString = JsonOutput.toJson(payload)
                    post.getOutputStream().write(payloadAsString.getBytes("UTF-8"))
                }
                int postResponseCode = post.getResponseCode()
                if (postResponseCode == 200) {
                    responseText = post.getInputStream().getText()
                    context.println(responseText)
                    return responseText
                } else {
                    context.println("Calling Triggears ${methodName} failed with code " + postResponseCode.toString())
                    return responseText
                }
            }
        } catch(e) {
            context.println("Calling Triggears ${methodName} failed! " + e)
            return responseText
        }
        return responseText
    }
}
