
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


public class Jmeter_search_tests {

    private static final String jmeter_folder = "C:/Users/HP/Desktop/apache-jmeter-5.3";

    private static final String index_name = "test10k";

    private static final String structure = "C1=CC=CC=C1";

    private static File jmeterHome = new File(jmeter_folder);

    private static String slash = System.getProperty("file.separator");

    private static File jmeterProperties = new File(jmeterHome.getPath() + slash + "bin" + slash + "jmeter.properties");

    private static StandardJMeterEngine jmeter = new StandardJMeterEngine();

    private static String jtl_result_folder = "C:/Users/HP/Desktop";

    private static String jmx_config_folder = "C:/Users/HP/Desktop";

    static {
        //JMeter initialization (properties, log levels, locale, etc)
        JMeterUtils.setJMeterHome(jmeterHome.getPath());
        JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
        JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
        JMeterUtils.initLocale();
    }


    public static void main(String[] args) throws IOException {

        similarity_search_test(300, 300, structure, jtl_result_folder + "/sim.jtl", jmx_config_folder + "/sim.jmx");

        exact_search_test(1000, 1000, structure, jtl_result_folder + "/exact.jtl", jmx_config_folder + "/exact.jmx");

        sub_search_test_with_external_jmx(-1, structure, jtl_result_folder + "/sub.jtl",
                "src/main/resources/sub_config.jmx");
    }


    public static void exact_search_test(int number_of_users, int ramp_up_period, String exact_structure,
                                         String where_to_export_jstl_results_file,
                                         String where_to_export_jmx_config_file) {

        if (jmeterHome.exists()) {

            if (jmeterProperties.exists()) {

                HeaderManager exact_search_header = getHeaderManager();

                HTTPSampler exact_search_request = getHttpSampler(exact_structure, "ExactSearch", "exact");


                LoopController exact_search_loopController = getLoopController();

                // Thread Group
                ThreadGroup threadGroup1 = getThreadGroup(number_of_users, ramp_up_period, exact_search_loopController);

                // Test Plan
                TestPlan testPlan = new TestPlan("Exact Search");
                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

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

    public static void similarity_search_test(int number_of_users, int ramp_up_period, String sim_structure,
                                              String where_to_export_jstl_results_file,
                                              String where_to_export_jmx_config_file) {
        if (jmeterHome.exists()) {

            if (jmeterProperties.exists()) {
                HeaderManager similarity_search_header = getHeaderManager();

                HTTPSampler similarity_search_request = getHttpSampler(sim_structure, "SimilaritySearch", "sim");

                //Loop controller is must in jmeter java code.
                LoopController similarity_search_loopController = getLoopController();

                // Thread Group
                ThreadGroup threadGroup = getThreadGroup(number_of_users, ramp_up_period, similarity_search_loopController);

                // Test Plan
                TestPlan testPlan = new TestPlan("Sim Search");
                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

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

    public static void sub_search_test_with_external_jmx(int number_of_users, String sub_structure,
                                                         String where_to_export_jtl_results_file,
                                                         String jmx_config_file) throws IOException {

        // Load existing .jmx Test Plan
        HashTree testPlanTree = SaveService.loadTree(new File(jmx_config_file));

        output_files_config(where_to_export_jtl_results_file, null, testPlanTree);
        // Run JMeter Test
        jmeter.configure(testPlanTree);
        jmeter.run();
    }


    private static LoopController getLoopController() {
        LoopController search_loopController = new LoopController();
        search_loopController.setLoops(1);
        search_loopController.setFirst(true);
        search_loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        search_loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        search_loopController.initialize();
        return search_loopController;
    }

    private static HTTPSampler getHttpSampler(String structure, String name, String searchType) {
        HTTPSampler sub_search_request = new HTTPSampler();
        sub_search_request.setDomain("localhost");
        sub_search_request.setPort(8080);
        sub_search_request.setPath("/search");
        sub_search_request.setMethod("POST");
        sub_search_request.setName(name);
        sub_search_request.setHeaderManager(getHeaderManager());
        sub_search_request.addNonEncodedArgument("", "{\n" +
                "    \"library_ids\":[\"" + index_name + "\"],\n" +
                "    \"query_structure\": \"" + structure + "\",\n" +
                "    \"type\":\"" + searchType + "\",\n" +
                "    \"offset\":0,\n" +
                "    \"limit\":20,\n" +
                "    \"hydrogen_visible\":true\n" +
                "}", "");
        sub_search_request.setPostBodyRaw(true);
        sub_search_request.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
        sub_search_request.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        return sub_search_request;
    }

    private static void output_files_config(String where_to_export_jstl_results_file,
                                            String where_to_export_jmx_config_file,
                                            HashTree testPlanTree) {
        // save generated test plan to JMeter's .jmx file format
        if (where_to_export_jmx_config_file != null) {
            try {
                SaveService.saveTree(testPlanTree, new FileOutputStream(where_to_export_jmx_config_file));
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private static ThreadGroup getThreadGroup(int number_of_users, int ramp_up_period, LoopController similarity_search_loopController) {
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Thread Group 1");
        threadGroup.setNumThreads(number_of_users);
        threadGroup.setRampUp(ramp_up_period);
        threadGroup.setSamplerController(similarity_search_loopController);
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
        return threadGroup;
    }

    private static HeaderManager getHeaderManager() {
        HeaderManager search_header = new HeaderManager();
        search_header.add(new Header("Content-Type", "application/json"));
        search_header.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        search_header.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        return search_header;
    }
}

