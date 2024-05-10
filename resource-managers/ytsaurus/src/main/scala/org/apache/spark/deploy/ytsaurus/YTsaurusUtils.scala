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

import tech.ytsaurus.client.rpc.YTsaurusClientAuth

object YTsaurusUtils {

  val URL_PREFIX = "ytsaurus://"

  def parseMasterUrl(masterURL: String): String = {
    masterURL.substring(URL_PREFIX.length)
  }

  def userAndToken(): (String, String) = {
    val user = sys.env.get("YT_SECURE_VAULT_YT_USER").orNull
    val token = sys.env.get("YT_SECURE_VAULT_YT_TOKEN").orNull
    if (user == null || token == null) {
      val auth = YTsaurusClientAuth.loadUserAndTokenFromEnvironment()
      (auth.getUser.orElseThrow(), auth.getToken.orElseThrow())
    } else {
      (user, token)
    }
  }
}
