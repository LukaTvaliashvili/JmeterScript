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
import org.apache.jmeter.protocol.http.proxy.HttpRequestHdr;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
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


public class Main {

    public static void main(String[] args) {
        File jmeterHome = new File("/home/lt/Softwares/Jmeter/apache-jmeter-5.3");
        String slash = System.getProperty("file.separator");

        if (jmeterHome.exists()) {
            File jmeterProperties = new File(jmeterHome.getPath() + slash + "bin" + slash + "jmeter.properties");
            if (jmeterProperties.exists()) {
                //JMeter Engine
                StandardJMeterEngine jmeter = new StandardJMeterEngine();

                //JMeter initialization (properties, log levels, locale, etc)
                JMeterUtils.setJMeterHome(jmeterHome.getPath());
                JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
                JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
                JMeterUtils.initLocale();

                // JMeter Test Plan, basically JOrphan HashTree
                HashTree testPlanTree = new HashTree();


                //HTTP Header

                // "Content-Type", "application/json");


                HeaderManager headerManager = new HeaderManager();
                headerManager.add(new Header("Content-Type", "application/json"));
                headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
                headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());



//                HTTPSamplerProxy exactSearch = new HTTPSamplerProxy();
//                exactSearch.setDomain("localhost");
//                exactSearch.setPort(8080);
//                exactSearch.setPath("/search");
//                exactSearch.setMethod("POST");
//                exactSearch.setName("Set name if needed");

                HTTPSampler exactSearch = new HTTPSampler();
                exactSearch.setDomain("localhost");
                exactSearch.setPort(8080);
                exactSearch.setPath("/search");
                exactSearch.setMethod("POST");
                exactSearch.setName("Set name");
//                exactSearch.setHeaderManager(headerManager);

//                HTTPArgument httpArgument = new HTTPArgument();

//                exactSearch.addNonEncodedArgument("", "{\n" +
//                        "    \"library_ids\":[\"index1\"],\n" +
//                        "    \"query_structure\": \"C1=CC=CC=C1\",\n" +
//                        "    \"type\":\"exact\",\n" +
//                        "    \"offset\":0,\n" +
//                        "    \"limit\":20,\n" +
//                        "    \"hydrogen_visible\":true\n" +
//                        "}", "");
//
//                exactSearch.setPostBodyRaw(true);
//
//                exactSearch.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
//                exactSearch.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());


                // Second HTTP Sampler for similarity search
//                HTTPSamplerProxy simSearch = new HTTPSamplerProxy();
//                simSearch.setDomain("localhost");
//                simSearch.setPort(8080);
//                simSearch.setPath("/search");
//                simSearch.setMethod("POST");
//                simSearch.setName("Set name if needed");
//                simSearch.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
//                simSearch.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());


                // Second HTTP Sampler for sub search
//                HTTPSamplerProxy subSearch = new HTTPSamplerProxy();
//                subSearch.setDomain("localhost");
//                subSearch.setPort(8080);
//                subSearch.setPath("/search");
//                subSearch.setMethod("POST");
//                subSearch.setName("Set name if needed");

//                subSearch.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
//                subSearch.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());


                // Loop Controller
                LoopController loopController = new LoopController();
                loopController.setLoops(1);
                loopController.setFirst(true);
                //loopController.setContinueForever(false);
                loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
                loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
                loopController.initialize();

                // Thread Group
                ThreadGroup threadGroup = new ThreadGroup();
                threadGroup.setName("Example Thread Group");
                threadGroup.setNumThreads(5);
                threadGroup.setRampUp(5);
                threadGroup.setSamplerController(loopController);
                threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
                threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

                // Test Plan
                TestPlan testPlan = new TestPlan("Exact Search");
                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

                // Construct Test Plan from previously initialized elements
                testPlanTree.add(testPlan);
                HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
                threadGroupHashTree.add(exactSearch, headerManager);
//                threadGroupHashTree.add(examplecomSampler);

                // save generated test plan to JMeter's .jmx file format
                try {
                    SaveService.saveTree(testPlanTree, new FileOutputStream(jmeterHome + slash + "Exact5.jmx"));
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
                String logFile = jmeterHome + slash + "Exact5.jtl";
                ResultCollector logger = new ResultCollector(summer);
                logger.setFilename(logFile);
                testPlanTree.add(testPlanTree.getArray()[0], logger);

                // Run Test Plan
                jmeter.configure(testPlanTree);
                jmeter.run();

                System.out.println("Test completed. See " + jmeterHome + slash + "Exact5.jtl file for results");
                System.out.println("JMeter .jmx script is available at " + jmeterHome + slash + "Exact5.jmx");
                System.exit(0);

            }
        }

        System.err.println("jmeter.home property is not set or pointing to incorrect location");
        System.exit(1);




    }

}
