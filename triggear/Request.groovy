package com.futuresimple.triggear


class Request implements Serializable{
    TriggeringEvent registrationEvent
    List<String> labels = []
    Map<PipelineParameters, String> requestedParameters = [:]
    List<String> changeRestrictions = []
    List<String> branchRestrictions = []
    List<String> fileRestrictions = []

    private Request(TriggeringEvent type){
        registrationEvent = type
    }

    private void addLabel(String label){
        labels.add(label)
    }

    private void addChangeRestriction(String pathPrefix){
        changeRestrictions.add(pathPrefix)
    }

    private void addBranchRestriction(String branch){
        branchRestrictions.add(branch)
    }

    private void addFileRestriction(String filePath){
        fileRestrictions.add(filePath)
    }

    private void addBranchAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.BRANCH, nameOverwrite)
    }

    private void addShaAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.SHA, nameOverwrite)
    }

    private void addTagAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.TAG, nameOverwrite)
    }

    private void addChangesAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.CHANGES, nameOverwrite)
    }

    private void addReleaseTargetAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.RELEASE_TARGET, nameOverwrite)
    }

    private void addIsPrereleaseAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.IS_PRERELEASE, nameOverwrite)
    }

    private void addWhoAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.WHO, nameOverwrite)
    }

    private void addPrUrlAsParameter(String nameOverwrite=null){
        requestedParameters.put(PipelineParameters.PR_URL, nameOverwrite)
    }

    static PushBuilder forPushes(){
        return new PushBuilder()
    }

    static TagBuilder forTags(){
        return new TagBuilder()
    }

    static LabelBuilder forLabels(){
        return new LabelBuilder()
    }

    static PrBuilder forPrOpened(){
        return new PrBuilder()
    }

    static ReleaseBuilder forReleases(){
        return new ReleaseBuilder()
    }

    static class PushBuilder extends Builder {
        PushBuilder(){
            this.eventType = TriggeringEvent.PUSH
        }

        PushBuilder addBranchRestriction(String branch){
            getRequest().addBranchRestriction(branch)
            return this
        }

        PushBuilder addChangeRestriction(String pathPrefix){
            getRequest().addChangeRestriction(pathPrefix)
            return this
        }

        PushBuilder addChangesAsParameter(String nameOverwrite=null){
            getRequest().addChangesAsParameter(nameOverwrite)
            return this
        }

        PushBuilder addBranchAsParameter(String nameOverwrite=null){
            getRequest().addBranchAsParameter(nameOverwrite)
            return this
        }

        PushBuilder addShaAsParameter(String nameOverwrite=null){
            getRequest().addShaAsParameter(nameOverwrite)
            return this
        }
    }

    static class TagBuilder extends Builder {
        TagBuilder(){
            this.eventType = TriggeringEvent.TAG
        }

        TagBuilder addTagAsParameter(String nameOverwrite=null){
            getRequest().addTagAsParameter(nameOverwrite)
            return this
        }

        TagBuilder addShaAsParameter(String nameOverwrite=null){
            getRequest().addShaAsParameter(nameOverwrite)
            return this
        }
    }

    static class LabelBuilder extends Builder {
        LabelBuilder(){
            this.eventType = TriggeringEvent.LABEL
        }

        LabelBuilder addLabel(String label){
            getRequest().addLabel(label)
            return this
        }

        LabelBuilder addBranchAsParameter(String nameOverwrite=null){
            getRequest().addBranchAsParameter(nameOverwrite)
            return this
        }

        LabelBuilder addShaAsParameter(String nameOverwrite=null){
            getRequest().addShaAsParameter(nameOverwrite)
            return this
        }

        LabelBuilder addWhoAsParameter(String nameOverwrite=null){
            getRequest().addWhoAsParameter(nameOverwrite)
            return this
        }

        LabelBuilder addPrUrlAsParameter(String nameOverwrite=null){
            getRequest().addPrUrlAsParameter(nameOverwrite)
            return this
        }
    }

    static class ReleaseBuilder extends Builder {
        ReleaseBuilder(){
            this.eventType = TriggeringEvent.RELEASE
        }

        ReleaseBuilder addReleaseTargetAsParameter(String nameOverwrite=null){
            getRequest().addReleaseTargetAsParameter(nameOverwrite)
            return this
        }

        ReleaseBuilder addIsPrereleaseAsParameter(String nameOverwrite=null){
            getRequest().addIsPrereleaseAsParameter(nameOverwrite)
            return this
        }

        ReleaseBuilder addTagAsParameter(String nameOverwrite=null){
            getRequest().addTagAsParameter(nameOverwrite)
            return this
        }
    }

    static class PrBuilder extends Builder {
        PrBuilder(){
            this.eventType = TriggeringEvent.PR_OPEN
        }

        PrBuilder addBranchRestriction(String branch){
            getRequest().addBranchRestriction(branch)
            return this
        }

        PrBuilder addBranchAsParameter(String nameOverwrite=null){
            getRequest().addBranchAsParameter(nameOverwrite)
            return this
        }

        PrBuilder addShaAsParameter(String nameOverwrite=null){
            getRequest().addShaAsParameter(nameOverwrite)
            return this
        }
    }

    private static class Builder implements Serializable {
        protected TriggeringEvent eventType
        protected Request request

        protected Request getRequest(){
            if(request == null){
                request = new Request(eventType)
            }
            return request
        }

        Builder addFileRestriction(String filePath){
            getRequest().addFileRestriction(filePath)
            return this
        }

        Request build(){
            return getRequest()
        }
    }
}
