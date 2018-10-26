package org.jaqpot.descriptors.cdk;

import org.jaqpot.descriptors.dto.dataset.Dataset;
import org.jaqpot.descriptors.dto.dataset.FeatureInfo;
import org.openscience.cdk.exception.CDKException;

import java.util.List;

public interface SmilesDescriptorsClient {

    Boolean isSmilesDocument(Byte[] smilesFile);

    Dataset generateDatasetBySmiles(String[] wantedCategories, List<String> smilesFile, String sourceName, String sourceURI) throws CDKException;

}

