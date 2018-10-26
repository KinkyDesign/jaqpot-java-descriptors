package org.jaqpot.descriptors.dto.jpdi;


import org.jaqpot.descriptors.dto.dataset.Dataset;

import java.util.Map;

public class DescriptorRequest {
    private Dataset dataset;
    private Map<String, Object> parameters;

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
