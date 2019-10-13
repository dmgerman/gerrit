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
name|com
operator|.
name|sun
operator|.
name|management
operator|.
name|UnixOperatingSystemMXBean
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"restriction"
argument_list|)
DECL|class|OperatingSystemMXBeanUnixNative
class|class
name|OperatingSystemMXBeanUnixNative
implements|implements
name|OperatingSystemMXBeanInterface
block|{
DECL|field|sys
specifier|private
specifier|final
name|UnixOperatingSystemMXBean
name|sys
decl_stmt|;
DECL|method|OperatingSystemMXBeanUnixNative (UnixOperatingSystemMXBean sys)
name|OperatingSystemMXBeanUnixNative
parameter_list|(
name|UnixOperatingSystemMXBean
name|sys
parameter_list|)
block|{
name|this
operator|.
name|sys
operator|=
name|sys
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
return|return
name|sys
operator|.
name|getProcessCpuTime
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOpenFileDescriptorCount ()
specifier|public
name|long
name|getOpenFileDescriptorCount
parameter_list|()
block|{
return|return
name|sys
operator|.
name|getOpenFileDescriptorCount
argument_list|()
return|;
block|}
block|}
end_class

end_unit

