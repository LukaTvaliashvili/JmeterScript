
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Exact_Sim_Search_Test {

    private static final String jmeter_folder = "C:/Users/HP/Desktop/apache-jmeter-5.3";

    private static final String index_name = "test10k";

    private static final String structure = "C1=CC=CC=C1";

    private static File jmeterHome = new File(jmeter_folder);

    private static String slash = System.getProperty("file.separator");

    private static File jmeterProperties = new File(jmeterHome.getPath() + slash + "bin" + slash + "jmeter.properties");

    private static StandardJMeterEngine jmeter = new StandardJMeterEngine();


    static {
        //JMeter initialization (properties, log levels, locale, etc)
        JMeterUtils.setJMeterHome(jmeterHome.getPath());
        JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
        JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
        JMeterUtils.initLocale();
    }


    public static void main(String[] args) {

        similarity_search_test(1000, structure, "C:/Users/HP/Desktop/sim.jstl", "C:/Users/HP/Desktop/sim.jmx");

        //exact_search_test(1000, structure, "C:/Users/HP/Desktop/exact.jstl", "C:/Users/HP/Desktop/exact.jmx");

    }


    public static void exact_search_test(int number_of_users, String exact_structure,
            String where_to_export_jstl_results_file,
            String where_to_export_jmx_config_file) {

        if (jmeterHome.exists()) {

            if (jmeterProperties.exists()) {

                HeaderManager exact_search_header = getHeaderManager();

                HTTPSampler exact_search_request = new HTTPSampler();
                exact_search_request.setDomain("localhost");
                exact_search_request.setPort(8080);
                exact_search_request.setPath("/search");
                exact_search_request.setMethod("POST");
                exact_search_request.setName("ExactSearch");
                exact_search_request.setHeaderManager(exact_search_header);
                exact_search_request.addNonEncodedArgument("", "{\n" +
                        "    \"library_ids\":[\"" + index_name + "\"],\n" +
                        "    \"query_structure\": \"" + exact_structure + "\",\n" +
                        "    \"type\":\"exact\",\n" +
                        "    \"offset\":0,\n" +
                        "    \"limit\":20,\n" +
                        "    \"hydrogen_visible\":true\n" +
                        "}", "");
                exact_search_request.setPostBodyRaw(true);
                exact_search_request.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
                exact_search_request.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());


                LoopController exact_search_loopController = getLoopController();

                // Thread Group
                ThreadGroup threadGroup1 = getThreadGroup(number_of_users, exact_search_loopController);

                // Test Plan
                TestPlan testPlan = new TestPlan("Exact Search");
                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments)new ArgumentsPanel().createTestElement());

                //Highest node in hirerachy
                HashTree testPlanTree = new HashTree();

                // Construct Test Plan from previously initialized elements
                testPlanTree.add(testPlan);
                HashTree threadGroupHashTree1 = testPlanTree.add(testPlan, threadGroup1);
                threadGroupHashTree1.add(exact_search_request, exact_search_header);

                // save generated test plan to JMeter's .jmx file format
                output_files_config(where_to_export_jstl_results_file, where_to_export_jmx_config_file, testPlanTree);

                // Run Test Plan
                jmeter.configure(testPlanTree);
                jmeter.run();

                System.out.println("Test completed. See " + where_to_export_jstl_results_file + " file for results");
                System.out.println("JMeter .jmx script is available at " + where_to_export_jmx_config_file);
                System.exit(0);

            }
        }

        System.err.println("jmeter.home property is not set or pointing to incorrect location");
        System.exit(1);
    }

    private static LoopController getLoopController() {
        LoopController exact_search_loopController = new LoopController();
        exact_search_loopController.setLoops(1);
        exact_search_loopController.setFirst(true);
        exact_search_loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        exact_search_loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        exact_search_loopController.initialize();
        return exact_search_loopController;
    }

    public static void similarity_search_test(int number_of_users, String sim_structure,
            String where_to_export_jstl_results_file,
            String where_to_export_jmx_config_file) {
        if (jmeterHome.exists()) {

            if (jmeterProperties.exists()) {
                HeaderManager similarity_search_header = getHeaderManager();

                HTTPSampler similarity_search_request = new HTTPSampler();
                similarity_search_request.setDomain("localhost");
                similarity_search_request.setPort(8080);
                similarity_search_request.setPath("/search");
                similarity_search_request.setMethod("POST");
                similarity_search_request.setName("SimilaritySearch");
                similarity_search_request.setHeaderManager(similarity_search_header);
                similarity_search_request.addNonEncodedArgument("", "{\n" +
                        "    \"library_ids\":[\"" + index_name + "\"],\n" +
                        "    \"query_structure\": \"" + sim_structure + "\",\n" +
                        "    \"type\":\"sim\",\n" +
                        "    \"offset\":0,\n" +
                        "    \"limit\":20,\n" +
                        "    \"hydrogen_visible\":true\n" +
                        "}", "");
                similarity_search_request.setPostBodyRaw(true);
                similarity_search_request.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
                similarity_search_request.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());

                //Loop controller is must in jmeter java code.
                LoopController similarity_search_loopController = getLoopController();

                // Thread Group
                ThreadGroup threadGroup = getThreadGroup(number_of_users, similarity_search_loopController);

                // Test Plan
                TestPlan testPlan = new TestPlan("Sim Search");
                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments)new ArgumentsPanel().createTestElement());

                //Highest node in hirerachy
                HashTree testPlanTree = new HashTree();

                // Construct Test Plan from previously initialized elements
                testPlanTree.add(testPlan);
                HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
                threadGroupHashTree.add(similarity_search_request, similarity_search_header);


                output_files_config(where_to_export_jstl_results_file, where_to_export_jmx_config_file, testPlanTree);


                // Run Test Plan
                jmeter.configure(testPlanTree);
                jmeter.run();

                System.out.println("Test completed. See " + where_to_export_jstl_results_file + " file for results");
                System.out.println("JMeter .jmx script is available at " + where_to_export_jmx_config_file);
                System.exit(0);

            }
        }

        System.err.println("jmeter.home property is not set or pointing to incorrect location");
        System.exit(1);
    }

    private static void output_files_config(String where_to_export_jstl_results_file,
            String where_to_export_jmx_config_file,
            HashTree testPlanTree) {
        // save generated test plan to JMeter's .jmx file format
        try {
            SaveService.saveTree(testPlanTree, new FileOutputStream(where_to_export_jmx_config_file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //add Summarizer output to get test progress in stdout like:
        // summary =      2 in   1.3s =    1.5/s Avg:   631 Min:   290 Max:   973 Err:     0 (0.00%)
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }


        // Store execution results into a .jtl file
        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(where_to_export_jstl_results_file);
        testPlanTree.add(testPlanTree.getArray()[0], logger);
    }

    private static ThreadGroup getThreadGroup(int number_of_users, LoopController similarity_search_loopController) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Thread Group 1");
        threadGroup.setNumThreads(number_of_users);
        threadGroup.setRampUp(1);
        threadGroup.setSamplerController(similarity_search_loopController);
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
        return threadGroup;
    }

    private static HeaderManager getHeaderManager() {
        HeaderManager exact_search_header = new HeaderManager();
        exact_search_header.add(new Header("Content-Type", "application/json"));
        exact_search_header.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        exact_search_header.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        return exact_search_header;
    }
}

