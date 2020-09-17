
import org.apache.jmeter.assertions.BeanShellAssertion;
import org.apache.jmeter.assertions.JSONPathAssertion;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.extractor.json.jsonpath.JSONPostProcessor;
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


public class SubstructureSearch {
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


                HeaderManager headerManager1 = new HeaderManager();
                headerManager1.add(new Header("Content-Type", "application/json"));
                headerManager1.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
                headerManager1.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());


                HTTPSampler subSearch1 = new HTTPSampler();
                subSearch1.setDomain("localhost");
                subSearch1.setPort(8080);
                subSearch1.setPath("/search");
                subSearch1.setMethod("POST");
                subSearch1.setName("Set name");


                subSearch1.addNonEncodedArgument("", "{\n" +
                        "    \"library_ids\":[\"index1\"],\n" +
                        "    \"query_structure\": \"C1=CC=CC=C1\",\n" +
                        "    \"type\":\"exact\",\n" +
                        "    \"offset\":0,\n" +
                        "    \"limit\":20,\n" +
                        "    \"hydrogen_visible\":true\n" +
                        "}", "");

                subSearch1.setPostBodyRaw(true);

                subSearch1.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
                subSearch1.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());


                LoopController loopController1 = new LoopController();
                loopController1.setLoops(1);
                loopController1.setFirst(true);
                //loopController1.setContinueForever(false);
                loopController1.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
                loopController1.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
                loopController1.initialize();


                JSONPostProcessor jsonPostProcessor1 = new JSONPostProcessor();
                jsonPostProcessor1.setRefNames("search_id");
                jsonPostProcessor1.setJsonPathExpressions("$.search_id");

                // Thread Group
                ThreadGroup threadGroup1 = new ThreadGroup();
                threadGroup1.setName("Thread Group 1");
                threadGroup1.setNumThreads(5);
                threadGroup1.setRampUp(5);
                threadGroup1.setSamplerController(loopController1);
                threadGroup1.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
                threadGroup1.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());


                HeaderManager headerManager2 = new HeaderManager();
                headerManager2.add(new Header("Content-Type", "application/json"));
                headerManager2.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
                headerManager2.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());


                HTTPSampler subSearch2 = new HTTPSampler();
                subSearch2.setDomain("localhost");
                subSearch2.setPort(8080);
                subSearch2.setPath("/search/${__property(search_id)");
                subSearch2.setMethod("GET");
                subSearch2.setName("Set name");


                subSearch2.addNonEncodedArgument("",
                        "{\n" +
                                "\"limit\":20,\n" +
                                "}",
                        "");

                subSearch2.setPostBodyRaw(true);

                subSearch2.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
                subSearch2.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());


                LoopController loopController2 = new LoopController();
                loopController2.setLoops(1);
                loopController2.setFirst(true);
                //loopController2.setContinueForever(false);
                loopController2.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
                loopController2.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
                loopController2.initialize();


                ThreadGroup threadGroup2 = new ThreadGroup();
                threadGroup2.setName("Thread Group 2");
                threadGroup2.setNumThreads(5);
                threadGroup2.setRampUp(5);
                threadGroup2.setSamplerController(loopController2);
                threadGroup2.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
                threadGroup2.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());


                JSONPostProcessor jsonPostProcessor2 = new JSONPostProcessor();
                jsonPostProcessor2.setRefNames("search_id");
                jsonPostProcessor2.setJsonPathExpressions("$.search_id");



                BeanShellAssertion beanShellAssertion = new BeanShellAssertion();
                beanShellAssertion.setScript("${__setProperty(search_id, ${search_id})};\n");


                // Test Plan
                TestPlan testPlan = new TestPlan("Sub Search");
                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());


                // Construct Test Plan from previously initialized elements
                testPlanTree.add(testPlan);
                HashTree threadGroupHashTree1 = testPlanTree.add(testPlan, threadGroup1);
                threadGroupHashTree1.add(subSearch1, headerManager1);
                threadGroupHashTree1.add(jsonPostProcessor1);

                HashTree threadGroupHashTree2 = testPlanTree.add(testPlan, threadGroup2);
                threadGroupHashTree2.add(subSearch2, headerManager2);
                threadGroupHashTree2.add(jsonPostProcessor2);

                testPlanTree.add(beanShellAssertion);

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
