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

package org.apache.spark.scheduler.cluster.ytsaurus

import org.apache.spark.SparkContext
import org.apache.spark.deploy.ytsaurus.YTsaurusUtils
import org.apache.spark.internal.Logging
import org.apache.spark.scheduler.{ExternalClusterManager, SchedulerBackend, TaskScheduler, TaskSchedulerImpl}

private[spark] class YTsaurusClusterManager extends ExternalClusterManager with Logging {

  override def canCreate(masterURL: String): Boolean = {
    masterURL.startsWith(YTsaurusUtils.URL_PREFIX)
  }

  override def createTaskScheduler(sc: SparkContext, masterURL: String): TaskScheduler = {
    new TaskSchedulerImpl(sc)
  }

  override def createSchedulerBackend(
    sc: SparkContext,
    masterURL: String,
    scheduler: TaskScheduler): SchedulerBackend = {
    logInfo("Creating YTsaurus scheduler backend")
    var ytProxy = YTsaurusUtils.parseMasterUrl(masterURL)
    val deployMode = sc.conf.get("spark.submit.deployMode")
    var networkName = sc.conf.getOption("spark.hadoop.yt.proxyNetworkName")
    if (deployMode == "cluster") {
      ytProxy = sc.conf.get("spark.hadoop.yt.clusterProxy", ytProxy)
      networkName = None
    }
    if (sc.conf.contains("spark.hadoop.yt.clusterProxy")) {
      sc.conf.set("spark.hadoop.yt.proxy", sc.conf.get("spark.hadoop.yt.clusterProxy"))
    }

    val operationManager = YTsaurusOperationManager.create(ytProxy, sc.conf, networkName)

    new YTsaurusSchedulerBackend(scheduler.asInstanceOf[TaskSchedulerImpl], sc, operationManager)
  }

  override def initialize(scheduler: TaskScheduler, backend: SchedulerBackend): Unit = {
    logInfo("Initializing YTsaurus scheduler backend")
    scheduler.asInstanceOf[TaskSchedulerImpl].initialize(backend)
    backend.asInstanceOf[YTsaurusSchedulerBackend].initialize()
  }
}
