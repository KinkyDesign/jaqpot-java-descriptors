package org.jaqpot.descriptors.resource;

import org.jaqpot.descriptors.cdk.SmilesDescriptorsClientImpl;
import org.jaqpot.descriptors.dto.dataset.DataEntry;
import org.jaqpot.descriptors.dto.dataset.Dataset;
import org.jaqpot.descriptors.dto.dataset.FeatureInfo;
import org.jaqpot.descriptors.dto.jpdi.DescriptorRequest;
import org.jaqpot.descriptors.dto.jpdi.DescriptorResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("smiles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SmilesDescriptorLegacy {
    private static final Logger LOG = Logger.getLogger(SmilesDescriptorLegacy.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("calculate")
    public Response calculate(DescriptorRequest descriptorRequest) {
        try {
            ArrayList<String> _categories = new ArrayList<>();
            _categories.add("all");

            Map<String, Object> parameters = descriptorRequest.getParameters() != null ? descriptorRequest.getParameters() : new HashMap<>();
            ArrayList<String> wantedCategoriesArray = (ArrayList<String>) parameters.getOrDefault("categories", _categories);

            String[] wantedCategories = wantedCategoriesArray.toArray(new String[0]);

            List<String> smiles  = new ArrayList<>();
            String sourceName = null;
            String sourceURI = null;

            //Todo handle multiple featureURIs as input
            //Get smiles from feature
            if (!descriptorRequest.getDataset().getFeatures().isEmpty()) {
                FeatureInfo featureInfo1 = descriptorRequest.getDataset().getFeatures().iterator().next();
                for (DataEntry dataEntry : descriptorRequest.getDataset().getDataEntry())
                    smiles.add((String) dataEntry.getValues().get(featureInfo1.getURI()));
                sourceName = featureInfo1.getName();
                sourceURI = featureInfo1.getURI();
            }//or from EntryId
            else {
                for (DataEntry dataEntry : descriptorRequest.getDataset().getDataEntry())
                    smiles.add(dataEntry.getEntryId().getName());
                sourceName = descriptorRequest.getDataset().getDatasetURI();
            }

            SmilesDescriptorsClientImpl smilesDescriptorsClient = new SmilesDescriptorsClientImpl();

            Dataset calculations = smilesDescriptorsClient.generateDatasetBySmiles(wantedCategories, smiles,sourceName,sourceURI);

            //Calculate smiles features
            calculations.getDataEntry().forEach(de -> {
                calculations.getDataEntry().stream()
                        .filter(e -> !e.equals(de))
                        .forEach(e -> {
                            de.getValues().keySet().retainAll(e.getValues().keySet());
                        });
                if (!calculations.getDataEntry().isEmpty()) {
                    calculations.setFeatures(calculations.getFeatures()
                        .stream()
                        .filter(f -> calculations.getDataEntry()
                            .get(0)
                            .getValues()
                            .keySet()
                            .contains(f.getURI())
                        )
                        .collect(Collectors.toSet()));
                }
            });

            Set<Dataset.DescriptorCategory> descriptorCategories = new TreeSet<>();
            descriptorCategories.add(Dataset.DescriptorCategory.CDK);
            calculations.setDescriptors(descriptorCategories);

            DescriptorResponse descriptorResponse = new DescriptorResponse();
            descriptorResponse.setResponseDataset(calculations);
            return Response.ok(descriptorResponse).build();
        } catch (Exception ex) {
        LOG.log(Level.SEVERE, null, ex);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
