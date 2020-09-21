 ## Setup vital parameters
        
    On line 31 full path to the Jmeter folder needs to be defined 
    On line 32 index, used during the upload, needs to be defined
    On line 35 the structure that we will be searching for is defined. 
    On lines 45 and 46, destination for .jtl file(stores results) and .jmx(has configurations for the given test script) needs to be defined. 
    .jtl file will be used for the test result analysis.
    
 ## Similarity Search  
    
    similarity_search_test() is called on line 60 to simulate similarity search by multiple users.
    The first parameter defines the number of users who will sent HTTP requests for searching a particular structure using the similarity search. 
    The second parameter, ramp_up_period, defines the time JMeter should take to start the total number of threads. 
    For instance, if we have 300 threads(users) and 300 seconds as ramp up period, every user will send a new request after each second (300 / 300 = 1). 
    The third parameter, is the substructure used for search, which should already be stored in variable structure on line 35. 
    The results will be saved at the location (path) stored in jtl_result_folder variable with the name sim.jtl. 
    Whereas, the configuration file will be available at the location (path) stored in the variable jmx_config_folder with the name sim.jmx. 
    It will be useful to name both files according to the number of users, ramp-up period, and loop count so it is easier to distinguish during analysis. 
    
 ## Naming convention
    Let`s come up with a naming convention which will be universal among our peers.
    
    searchtype_numberOfUsers_rampUpPeriod_loopCount.jtl for results and .jmx for configuration file. 
    
    For instance, Sim_300_300_1.jtl will be generated as a result of running Sim_300_300_1.jmx test script. 
    For finding the treshold, after which the application does not behave as expected, we decided it is better not to loop over. 
    That`s why the number of loops is hardcoded and set to 1 on line 184. 
    If you decide that looping over is a viable option please go to line 184 and alter it.  
 
 ## Exact Search 
    is called on line 62. The parameters for exact_search_test() function are identical to the ones of similarity_search_test(). 
    Please follow the above-mentioned naming convetion for this search.

 
 ## Substructure search
    Due to relatively complex logic involved, we created a preconfigured file which is stored in resources folder in Jmeter script written in Java. 
    Prior to running tests, one should open sub_config.jmx file in Jmeter and vary the number of threads and ramp-up period as needed. 
  
 ## WARNING
    We need to clear run results using CTRL+E shortcut or by clicking Clear All button, which is the sixth icon after start. 
    Otherwise, the Aggregate report or any other listener will append the current test results to the previously stored results. 
    

    
    