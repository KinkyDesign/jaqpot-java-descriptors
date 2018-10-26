package org.jaqpot.descriptors.dto.jpdi;


import org.jaqpot.descriptors.dto.dataset.Dataset;

public class DescriptorResponse {

    Dataset responseDataset;

    public Dataset getResponseDataset() {
        return responseDataset;
    }

    public void setResponseDataset(Dataset responseDataset) {
        this.responseDataset = responseDataset;
    }
}
