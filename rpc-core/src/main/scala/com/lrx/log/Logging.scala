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

package org.apache.spark.internal

import java.util.logging.Level

import org.slf4j.{Logger, LoggerFactory}
import com.lrx.util.Utils

/**
  * Utility trait for classes that want to log data. Creates a SLF4J logger for the class and allows
  * logging messages at different levels using methods that only evaluate parameters lazily if the
  * log level is enabled.
  */
trait Logging {

	// Make the log field transient so that objects with Logging can
	// be serialized and used on another machine
	@transient private var log_ : Logger = null

	// Method to get the logger name for this object
	protected def logName = {
		// Ignore trailing $'s in the class names for Scala objects
		this.getClass.getName.stripSuffix("$")
	}

	// Method to get or create the logger for this object
	protected def log: Logger = {
		if (log_ == null) {
			log_ = LoggerFactory.getLogger(logName)
		}
		log_
	}

	// Log methods that take only a String
	protected def logInfo(msg: => String) {
		if (log.isInfoEnabled) log.info(msg)
	}

	protected def logDebug(msg: => String) {
		if (log.isDebugEnabled) log.debug(msg)
	}

	protected def logTrace(msg: => String) {
		if (log.isTraceEnabled) log.trace(msg)
	}

	protected def logWarning(msg: => String) {
		if (log.isWarnEnabled) log.warn(msg)
	}

	protected def logError(msg: => String) {
		if (log.isErrorEnabled) log.error(msg)
	}

	// Log methods that take Throwables (Exceptions/Errors) too
	protected def logInfo(msg: => String, throwable: Throwable) {
		if (log.isInfoEnabled) log.info(msg, throwable)
	}

	protected def logDebug(msg: => String, throwable: Throwable) {
		if (log.isDebugEnabled) log.debug(msg, throwable)
	}

	protected def logTrace(msg: => String, throwable: Throwable) {
		if (log.isTraceEnabled) log.trace(msg, throwable)
	}

	protected def logWarning(msg: => String, throwable: Throwable) {
		if (log.isWarnEnabled) log.warn(msg, throwable)
	}

	protected def logError(msg: => String, throwable: Throwable) {
		if (log.isErrorEnabled) log.error(msg, throwable)
	}

	protected def isTraceEnabled(): Boolean = {
		log.isTraceEnabled
	}
}

private[spark] object Logging {
	@volatile private var initialized = false
	@volatile private var defaultRootLevel: Level = null
	@volatile private var defaultSparkLog4jConfig = false
	@volatile private[spark] var sparkShellThresholdLevel: Level = null

	val initLock = new Object()// We use reflection here to handle the case where users remove the
	//		// slf4j-to-jul bridge order to route their logs to JUL.
	//		val bridgeClass = Utils.classForName("org.slf4j.bridge.SLF4JBridgeHandler")
	//		bridgeClass.getMethod("removeHandlersForRootLogger").invoke(null)
	//		val installed = bridgeClass.getMethod("isInstalled").invoke(null).asInstanceOf[Boolean]
	//		if (!installed) {
	//			bridgeClass.getMethod("install").invoke(null)
	//		}
	try {
//
	} catch {
		case e: ClassNotFoundException => // can't log anything yet so just fail silently
	}

	/**
	  * Marks the logging system as not initialized. This does a best effort at resetting the
	  * logging system to its initial state so that the next class to use logging triggers
	  * initialization again.
	  */

}
