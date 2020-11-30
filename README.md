 ## Setup parameters for script
     On line 31 full path to the Jmeter folder needs to be defined 
     On line 32 index, used during the upload, needs to be defined
     On line 35 the structure that we will be searching for is defined. 
     On line 40 the metric for similarity needs to be defined, tanimoto, tversky, euclid-cub.
     minVal, maxVal on lines 44, 46 are vital for similarity search in general
     whereas, alpha and beta parameters (lines 48, 50) are needed for euclid-cub only.
    
     On lines 60 and 62, destination for .jtl file(stores results) and .jmx(has configurations for the given test script) needs to be defined. 
     .jtl file will be used for the test result analysis.
    
 ## Setup parameters for bingoService and ElasticSearch
     In application.properties it is important to set the search.cache.size to the number of threads in JMeter. 
     So, if we use 300 threads(users), search.cache.size=300 is expected. 
     search.buffer.size=120
     
     Also, search.max_open_scroll_context should be twice the size of number of threads.
     So for 300 users we need to do the following:
     
     curl -x "" -X PUT localhost:9200/_cluster/settings -H 'Content-Type: application/json' -d'{
         "persistent" : {
             "search.max_open_scroll_context": 600
         },
         "transient": {
             "search.max_open_scroll_context": 600
         }
     }'
     
     Before running the script, it is vital to increase the RAM size from pom.xml file. 
     The given is the formula for determining what size of RAM one should dedicate to bingoService is the following:
     memory = 10kb * search.buffer.size * search.cache.size + 2Gb
    
     <plugin>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-maven-plugin</artifactId>
             <configuration>
                 <jvmArguments>-Xmx2048m -XX:NativeMemoryTracking=summary --enable-preview</jvmArguments>
            </configuration>
        </plugin> 
        
     2048 Mbs is the default value for bingoService, but it needs to be replaced with the value calculated above in Mbs.
     
     After this, one can launch a test case. 
     
     Once it is completed, stop bingoService, and clear the scroll context using the following command:
     
     curl --request DELETE \
       --url http://localhost:9200/_search/scroll/_all
     
     
    
 ## Exact Search 
     exact_search_test() is called on line 77. The parameters for exact_search_test() function are identical to the ones of similarity_search_test(). 
     Please follow the above-mentioned naming convetion for this search.
     The first parameter defines the number of users who will sent HTTP requests for searching a particular structure using the similarity search. 
     The second parameter, ramp_up_period, defines the time JMeter should take to start the total number of threads. 
     For instance, if we have 300 threads(users) and 300 seconds as ramp up period, every user will send a new request after each second (300 / 300 = 1). 
     The third parameter, is the substructure used for search, which should already be stored in variable structure on line 35. 
     The results will be saved at the location (path) stored in jtl_result_folder variable with the name sim.jtl. 
     Whereas, the configuration file will be available at the location (path) stored in the variable jmx_config_folder with the name sim.jmx. 
     It will be useful to name both files according to the number of users, ramp-up period, and loop count so it is easier to distinguish during analysis. 
 
 ## Substructure search
     sub_search_test() follows the the same semantics as the exact search, when it comes to using this script. 

 ## Similarity Search  
     similarity_search_test() is called on line 80 to simulate similarity search by multiple users.
     Apart from usual parameters, such as number of threads, ramp-up period, structure, and destination path for results
     and test case, the following parameters minVal, maxVal, alpha and beta needs to be considered. 
     minVal, maxVal defined on lines 44, 46 are vital for all metrics of similarity: tanimoto, tversky, euclid-cub.
     In addition, when tversky is selected, alpha and beta, values are a must (lines 48, 50)

 ## Disclaimer
     For finding the treshold, after which the application does not behave as expected, we decided it is better not to loop over. 
     That`s why the number of loops is hardcoded and set to 1 on line 254. 
     If you decide that looping over is a viable option please go to line 254 and alter it.  
 
     

    
    