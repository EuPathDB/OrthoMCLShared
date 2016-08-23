package org.orthomcl.shared.model.ontology;

import org.kohsuke.args4j.Option;

/**
 *  Process arguments passed from command line for OntologyMerger.java
 *
 *  @author Jie Zheng
 */
public class OwlClassGeneratorOptions {
    @Option(name="-path", usage ="the directory contains input file and also used for saved output file", required = false)
        private String path = "/var/www/PlasmoDB/plasmo.jbrestel/gus_home/lib/wdk/ontology/";

    @Option(name="-inputFilename", usage="input tab-delimited file that will be used to convert to OWL format file", required = false)
    private String inputFilename = "individuals.txt";

    @Option(name="-labelPos", usage="number of column in the tab delimited file that contains term labels ", required = false)
    private String labelPos = "1";

    @Option(name="-parentPos", usage="number of column in the tab delimited file that contains term parent IRIs", required = false)
    private String parentPos = "2";

    @Option(name="-annotPos", usage="number of column and following colums in the tab delimited file that contain annotation of terms", required = false)
    private String annotPos = "4";

    @Option(name="-idBase", usage="Ontology term URI base", required = false)
    private String idBase = "http://purl.obolibrary.org/obo/";

    @Option(name="-domainName", usage="used to create IRI for newly added terms"
    		+ "", required = false)
    private String domainName = "EUPATH";

    @Option(name="-startId", usage="unique ID assigned to the newly added terms when startId >0, otherwise, the term label will be used", required = false)
    private String startId = "-1";

    @Option(name="-ontoIRIstr", usage="IRI of ontology converted from the tab-delimited file", required = false)
    private String ontoIRIstr = "http://purl.obolibrary.org/obo/eupath/individuals.owl";

    @Option(name="-outputFilename", usage="The filename of output OWL file", required = false)
    private String outputFilename = "individuals.owl";

    public String getPath () {
    	return this.path;
    }

    public String getInputFilename () {
    	return this.inputFilename;
    }

    public String getIdBase () {
    	return this.idBase;
    }

    public String getDomainName () {
    	return this.domainName;
    }

    public int getLabelPos () {
    	int pos = Integer.parseInt(this.labelPos) -1;
    	return pos;
    }

    public int getParentPos () {
    	int pos = Integer.parseInt(this.parentPos) -1;
    	return pos;
    }

    public int getAnnotPos () {
    	int pos = Integer.parseInt(this.annotPos) -1;
    	return pos;
    }

    public int getStartId () {
    	return Integer.parseInt(this.startId);
    }

    public String getOutputFilename () {
    	return this.outputFilename;
    }

    public String getOntoIRIstr () {
    	return this.ontoIRIstr;
    }
}
