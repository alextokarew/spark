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

package org.apache.spark.executor

import org.apache.spark.SparkEnv
import org.apache.spark.resource.ResourceProfile
import org.apache.spark.rpc.RpcEnv


private[spark] class YTsaurusCoarseGrainedExecutorBackend(
  rpcEnv: RpcEnv,
  driverUrl: String,
  executorId: String,
  bindAddress: String,
  hostname: String,
  cores: Int,
  env: SparkEnv,
  resourcesFile: Option[String],
  resourceProfile: ResourceProfile
) extends CoarseGrainedExecutorBackend(
  rpcEnv,
  driverUrl,
  executorId,
  bindAddress,
  hostname,
  cores,
  env,
  resourcesFile,
  resourceProfile) {

}

object YTsaurusCoarseGrainedExecutorBackend {
  def main(args: Array[String]): Unit = {
    val createFn: (RpcEnv, CoarseGrainedExecutorBackend.Arguments, SparkEnv, ResourceProfile) =>
      CoarseGrainedExecutorBackend = {
      case (rpcEnv, arguments, env, resourceProfile) =>
        val ytTaskJobIndex = System.getenv("YT_TASK_JOB_INDEX")
        new YTsaurusCoarseGrainedExecutorBackend(rpcEnv, arguments.driverUrl,
          ytTaskJobIndex, arguments.bindAddress, arguments.hostname, arguments.cores,
           env, arguments.resourcesFileOpt, resourceProfile)
    }
    val backendArgs = CoarseGrainedExecutorBackend.parseArguments(args,
      this.getClass.getCanonicalName.stripSuffix("$"))
    CoarseGrainedExecutorBackend.run(backendArgs, createFn)
    System.exit(0)
  }
}