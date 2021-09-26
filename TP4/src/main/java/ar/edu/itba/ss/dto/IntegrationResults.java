package ar.edu.itba.ss.dto;

import ar.edu.itba.ss.models.Frame;

import java.util.List;

public class IntegrationResults {

    private String  integration;
    private List<Frame> results;

    public String getIntegration() {
        return integration;
    }
    public void setIntegration(String integration) {
        this.integration = integration;
    }
    public IntegrationResults withIntegration(String integration) {
        setIntegration(integration);
        return this;
    }

    public List<Frame> getResults() {
        return results;
    }
    public void setResults(List<Frame> results) {
        this.results = results;
    }
    public IntegrationResults withResults(List<Frame> results) {
        setResults(results);
        return this;
    }
}
