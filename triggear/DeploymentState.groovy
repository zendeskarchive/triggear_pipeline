package com.futuresimple.triggear

enum DeploymentState {
    SUCCESS('success'),
    FAILURE('failure'),
    ERROR('error'),
    PENDING('pending')

    DeploymentState(String gitHubStateName) {
        this.gitHubStateName = gitHubStateName
    }
    private String gitHubStateName

    String getGitHubStateName() {
        return gitHubStateName
    }
}