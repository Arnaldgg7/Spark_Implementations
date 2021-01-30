Spark Lab 02 session 

In .gitlab-ci.yml file, in the variables section 
```
  variables:
    CLUSTER_USER: "bdma**"
    CLUSTER_PASSWORD: "*****"
    CLUSTER_PORT: *****  
```
    

- CLUSTER_USER: replace bdma** with your bdma account. 

- CLUSTER_PASSWORD: replace ***** with your bdma account password. 

- CLUSTER_PORT: replace ***** with the port mapping to port 22 (received in email upon cluster deployment).

If you want to compile directly in the cluster, you should uncomment the following lines on pom.xml:
```
<!--<fork>true</fork>
    <executable>/home/bdma**/jdk1.8.0_144/bin/javac</executable>-->
```