# Spark_Implementations
Two relevant implementations in Spark. First, a KNN Model to predict the type of a tumor, and secondly a computation process to retrieve the country with the highest Happiness Score from 3 different data sets.

The first Spark implementation is a KNN predictive model with K=1, aimed at finding the type of tumor (benign or malign) based on several features of the tumor itself.

This implementation is based on 1 single Spark Job focused on optimize and distribute properly the workload in a distributed envionment such as HDFS. The overall goal is to know if we are good enough at predicting cancer tumors by factoring in the existing characteristics measured in each tumor and using a 1NN Predictive Model, by means of the Euclidean Distance as Statistical Test.

The final output (made of trainResult_testResult types of tumors, which is accessible in the folter MapReduce3.out) provides us with the Confusion Matrix values.

Therefore, we were able to predict the type of the tumor 94.4% of the times ((94 B_B + 57 M_M)/160).

The second Spark implementation is based on determining the country with the highest Happiness Score. The data is related to the World Happiness Report
dataset from Kaggle (https://www.kaggle.com/unsdsn/world-happiness). It consists of a set CSV files (one per year) that rank 155 countries by their
happiness levels according to metrics about economic production, social support, etc.

Such Spark implementation has been optimized to the full by means of pushing down Projections and Selections, as well as minimizing as much as possible the change of Stages, so that we achieve higher throughput overall and minimize at the same time data transfers over the network (main bottlneck in distributed systems). The output is provided in the console when executing the code.

These 2 Spark Jobs have been devised to be executed in a cluster environment such as HDFS as a distributed file system as source to leverage all potential from Spark by means of parallelization and distribution.
