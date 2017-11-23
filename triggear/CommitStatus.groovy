package com.futuresimple.triggear

enum CommitStatus {
    SUCCESS('success'),
    FAILURE('failure'),
    ERROR('error'),
    PENDING('pending')

    CommitStatus(String gitHubStateName) {
        this.gitHubStateName = gitHubStateName
    }
    private String gitHubStateName

    String getGitHubStateName() {
        return gitHubStateName
    }
}
