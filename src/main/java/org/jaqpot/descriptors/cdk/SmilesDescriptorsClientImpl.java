package org.jaqpot.descriptors.cdk;

import org.jaqpot.descriptors.dto.dataset.Dataset;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.IDescriptor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.*;
import javax.swing.tree.TreeNode;

public class SmilesDescriptorsClientImpl implements SmilesDescriptorsClient {
    @Override
    public Boolean isSmilesDocument(Byte[] smilesFile) {
        return null;
    }

    @Override
    public Dataset generateDatasetBySmiles(String[] wantedCategories, List<String> smilesFile, String sourceName, String sourceURI) throws CDKException {
        HashMap<String, DefaultMutableTreeNode> categories;
        List<IDescriptor> selectedDescriptors = new ArrayList<IDescriptor>();
        categories = CDKDescUtils.instantiateCategories();
        Dataset dataEntries = null;
        Set<String> wantedUniqueCategories = new HashSet<>();

        for (String category: wantedCategories)
        {
            if (category.equals("all"))
                Collections.addAll(wantedUniqueCategories, "topological","hybrid","geometrical","constitutional","electronic");
            else
                wantedUniqueCategories.add(category);
        }

        for (String category:wantedUniqueCategories) {
            int selectedDescsCount = 0;
            if (categories.containsKey(category+"Descriptor")) {
                
                System.out.println("WantedDesc: "+categories.get(category+"Descriptor").getLeafCount());
                Enumeration<? extends TreeNode> defaultMutableTreeNodeEnumeration = categories.get(category+"Descriptor").depthFirstEnumeration();
                while (defaultMutableTreeNodeEnumeration.hasMoreElements()) {
                    DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) defaultMutableTreeNodeEnumeration.nextElement();
                    if(defaultMutableTreeNode.isLeaf()) {
                        DescriptorTreeLeaf aLeaf = (DescriptorTreeLeaf) (defaultMutableTreeNode.getUserObject());
                        selectedDescriptors.add(aLeaf.getInstance());
                        selectedDescsCount++;
                    }
                }
                System.out.println("SelectedDesc: "+ selectedDescsCount);  
            }
        }
        Collections.sort(selectedDescriptors, CDKDescUtils.getDescriptorComparator());

        DescriptorCalculator descriptorCalculator = new DescriptorCalculator(selectedDescriptors);
        try {
            dataEntries = descriptorCalculator.calculateMoleculeDescriptors(smilesFile, sourceName, sourceURI);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataEntries;
    }
}
