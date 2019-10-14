begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.metrics.proc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
operator|.
name|proc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|management
operator|.
name|UnixOperatingSystemMXBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|OperatingSystemMXBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"restriction"
argument_list|)
DECL|class|OperatingSystemMXBeanFactory
class|class
name|OperatingSystemMXBeanFactory
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|method|create ()
specifier|static
name|OperatingSystemMXBeanInterface
name|create
parameter_list|()
block|{
name|OperatingSystemMXBean
name|sys
init|=
name|ManagementFactory
operator|.
name|getOperatingSystemMXBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|sys
operator|instanceof
name|UnixOperatingSystemMXBean
condition|)
block|{
return|return
operator|new
name|OperatingSystemMXBeanUnixNative
argument_list|(
operator|(
name|UnixOperatingSystemMXBean
operator|)
name|sys
argument_list|)
return|;
block|}
for|for
control|(
name|String
name|name
range|:
name|Arrays
operator|.
name|asList
argument_list|(
literal|"com.sun.management.UnixOperatingSystemMXBean"
argument_list|,
literal|"com.ibm.lang.management.UnixOperatingSystemMXBean"
argument_list|)
control|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|impl
init|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|impl
operator|.
name|isInstance
argument_list|(
name|sys
argument_list|)
condition|)
block|{
return|return
operator|new
name|OperatingSystemMXBeanReflectionBased
argument_list|(
name|sys
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ReflectiveOperationException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"No implementation for %s"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"No implementation of UnixOperatingSystemMXBean found"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

