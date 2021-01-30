# Spark_Implementations
Two relevant implementations in Spark. First, a KNN Model to predict the type of a tumor, and secondly a computation process to retrieve the country with the highest Happiness Score from 3 different data sets.

The second process has been optimized to the full by means of pushing down Projections and Selections, as well as minimizigin as much as possible the change of Stages, so that we achieve higher throughput overall and minimize at the same time data transfers over the network (main bottlneck in distributed systems).

These 2 Spark Jobs have been devised to be executed in a cluster environment such as HDFS as a distributed file system as source to leverage all potential from Spark by means of Parallelization.
