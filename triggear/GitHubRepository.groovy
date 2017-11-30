package com.futuresimple.triggear

interface GitHubRepository {
    String getOrganizationName()
    String getRepositoryUrl()
    String getRepositoryName()
    String getRepositoryFullName()
}
