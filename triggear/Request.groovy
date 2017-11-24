package com.futuresimple.triggear

class Request {
    TriggeringEvent registrationEvent
    List<String> labels = []
    List<PipelineParameters> requestedParameters = []
    List<String> changeRestrictions = []
    List<String> branchRestrictions = []

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

    private void addBranchAsParameter(){
        requestedParameters.add(PipelineParameters.BRANCH)
    }

    private void addShaAsParameter(){
        requestedParameters.add(PipelineParameters.SHA)
    }

    private void addTagAsParameter(){
        requestedParameters.add(PipelineParameters.TAG)
    }

    static PushBuilder forPushes(){
        return new PushBuilder()
    }

    static TagBuilder forTags(){
        return new TagBuilder()
    }

    static LabelBuilder forLabels(String... labels){
        return new LabelBuilder(labels)
    }

    static PrBuilder forPrOpened(){
        return new PrBuilder()
    }

    static class PushBuilder implements Builder {
        PushBuilder(){
            eventType = TriggeringEvent.PUSH
        }

        PushBuilder addBranchRestriction(String branch){
            request.addBranchRestriction(branch)
            return this
        }

        PushBuilder addChangeRestriction(String pathPrefix){
            request.addChangeRestriction(pathPrefix)
            return this
        }
    }

    static class TagBuilder implements Builder {
        TagBuilder(){
            eventType = TriggeringEvent.TAG
        }

        TagBuilder addTagAsParameter(){
            request.addTagAsParameter()
            return this
        }

        TagBuilder addBranchRestriction(String branch){
            request.addBranchRestriction(branch)
            return this
        }
    }

    static class LabelBuilder implements Builder {
        LabelBuilder(String... labels){
            eventType = TriggeringEvent.LABEL
            request.labels = labels
        }

        LabelBuilder addLabel(String label){
            request.addLabel(label)
            return this
        }
    }

    static class PrBuilder implements Builder {
        PrBuilder(){
            eventType = TriggeringEvent.PR_OPEN
        }
    }

    private trait Builder {
        TriggeringEvent eventType
        Request request

        Request getRequest(){
            if(request == null){
                request = new Request(eventType)
            }
            return request
        }

        Builder addBranchAsParameter(){
            getRequest().addBranchAsParameter()
            return this
        }

        Builder addShaAsParameter(){
            getRequest().addShaAsParameter()
            return this
        }

        Request build(){
            return request
        }
    }
}
