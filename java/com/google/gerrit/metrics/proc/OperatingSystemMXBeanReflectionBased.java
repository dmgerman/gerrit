begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_class
DECL|class|OperatingSystemMXBeanReflectionBased
class|class
name|OperatingSystemMXBeanReflectionBased
implements|implements
name|OperatingSystemMXBeanInterface
block|{
DECL|field|sys
specifier|private
specifier|final
name|OperatingSystemMXBean
name|sys
decl_stmt|;
DECL|field|getProcessCpuTime
specifier|private
specifier|final
name|Method
name|getProcessCpuTime
decl_stmt|;
DECL|field|getOpenFileDescriptorCount
specifier|private
specifier|final
name|Method
name|getOpenFileDescriptorCount
decl_stmt|;
DECL|method|OperatingSystemMXBeanReflectionBased (OperatingSystemMXBean sys)
name|OperatingSystemMXBeanReflectionBased
parameter_list|(
name|OperatingSystemMXBean
name|sys
parameter_list|)
throws|throws
name|ReflectiveOperationException
block|{
name|this
operator|.
name|sys
operator|=
name|sys
expr_stmt|;
name|getProcessCpuTime
operator|=
name|sys
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getProcessCpuTime"
argument_list|)
expr_stmt|;
name|getProcessCpuTime
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getOpenFileDescriptorCount
operator|=
name|sys
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getOpenFileDescriptorCount"
argument_list|)
expr_stmt|;
name|getOpenFileDescriptorCount
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProcessCpuTime ()
specifier|public
name|long
name|getProcessCpuTime
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|long
operator|)
name|getProcessCpuTime
operator|.
name|invoke
argument_list|(
name|sys
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ReflectiveOperationException
name|e
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getOpenFileDescriptorCount ()
specifier|public
name|long
name|getOpenFileDescriptorCount
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|long
operator|)
name|getOpenFileDescriptorCount
operator|.
name|invoke
argument_list|(
name|sys
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ReflectiveOperationException
name|e
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
block|}
end_class

end_unit

