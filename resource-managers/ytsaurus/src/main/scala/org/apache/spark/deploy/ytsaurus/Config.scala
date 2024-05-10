/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.deploy.ytsaurus

import org.apache.spark.internal.config.ConfigBuilder

object Config {
  val GLOBAL_CONFIG_PATH = ConfigBuilder("spark.ytsaurus.config.global.path")
    .doc("Path to global Spark configuration for the whole YTsaurus cluster")
    .version("3.2.2")
    .stringConf
    .createWithDefault("//home/spark/conf/global")

  val RELEASE_CONFIG_PATH = ConfigBuilder("spark.ytsaurus.config.releases.path")
    .doc("Root path for SPYT releases configuration")
    .version("3.2.2")
    .stringConf
    .createWithDefault("//home/spark/conf/releases")

  val RELEASE_SPYT_PATH = ConfigBuilder("spark.ytsaurus.spyt.releases.path")
    .doc("Root path for SPYT releases configuration")
    .version("3.2.2")
    .stringConf
    .createWithDefault("//home/spark/spyt/releases")

  val SPARK_DISTRIBUTIVES_PATH = ConfigBuilder("spark.ytsaurus.distributives.path")
    .doc("Root path for Spark distributives")
    .version("3.2.2")
    .stringConf
    .createWithDefault("//home/spark/distrib")

  val LAUNCH_CONF_FILE = ConfigBuilder("spark.ytsaurus.config.launch.file")
    .doc("SPYT release configuration file name")
    .version("3.2.2")
    .stringConf
    .createWithDefault("spark-launch-conf")

  val SPYT_VERSION = ConfigBuilder("spark.ytsaurus.spyt.version")
    .doc("SPYT version to use on cluster")
    .version("3.2.2")
    .stringConf
    .createOptional

  val MAX_DRIVER_FAILURES = ConfigBuilder("spark.ytsaurus.driver.maxFailures")
    .doc("Maximum driver task failures before operation failure")
    .version("3.2.2")
    .intConf
    .createWithDefault(5)

  val MAX_EXECUTOR_FAILURES = ConfigBuilder("spark.ytsaurus.executor.maxFailures")
    .doc("Maximum executor task failures before operation failure")
    .version("3.2.2")
    .intConf
    .createWithDefault(10)

  val EXECUTOR_OPERATION_SHUTDOWN_DELAY =
    ConfigBuilder("spark.ytsaurus.executor.operation.shutdown.delay")
    .doc("Time for executors to shutdown themselves before terminating " +
      "the executor operation, milliseconds")
    .version("3.2.2")
    .longConf
    .createWithDefault(10000)

  val YTSAURUS_POOL = ConfigBuilder("spark.ytsaurus.pool")
    .doc("YTsaurus scheduler pool to execute this job")
    .version("3.2.2")
    .stringConf
    .createOptional

  val YTSAURUS_IS_PYTHON = ConfigBuilder("spark.ytsaurus.isPython")
    .internal()
    .version("3.2.2")
    .booleanConf
    .createWithDefault(false)

  val YTSAURUS_PYTHON_VERSION = ConfigBuilder("spark.ytsaurus.python.version")
    .internal()
    .version("3.2.2")
    .stringConf
    .createOptional

  val DRIVER_OPERATION_ID = "spark.ytsaurus.driver.operation.id"
  val EXECUTOR_OPERATION_ID = "spark.ytsaurus.executor.operation.id"
  val SPARK_PRIMARY_RESOURCE = "spark.ytsaurus.primary.resource"
}
