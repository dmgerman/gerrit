begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.ioutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
import|;
end_import

begin_class
DECL|class|HostPlatform
specifier|public
specifier|final
class|class
name|HostPlatform
block|{
DECL|field|win32
specifier|private
specifier|static
specifier|final
name|boolean
name|win32
init|=
name|compute
argument_list|(
literal|"windows"
argument_list|)
decl_stmt|;
DECL|field|mac
specifier|private
specifier|static
specifier|final
name|boolean
name|mac
init|=
name|compute
argument_list|(
literal|"mac"
argument_list|)
decl_stmt|;
comment|/** @return true if this JVM is running on a Windows platform. */
DECL|method|isWin32 ()
specifier|public
specifier|static
name|boolean
name|isWin32
parameter_list|()
block|{
return|return
name|win32
return|;
block|}
DECL|method|isMac ()
specifier|public
specifier|static
name|boolean
name|isMac
parameter_list|()
block|{
return|return
name|mac
return|;
block|}
DECL|method|compute (String platform)
specifier|private
specifier|static
name|boolean
name|compute
parameter_list|(
name|String
name|platform
parameter_list|)
block|{
specifier|final
name|String
name|osDotName
init|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|String
argument_list|>
call|)
argument_list|()
operator|->
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|osDotName
operator|!=
literal|null
operator|&&
name|osDotName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
name|platform
argument_list|)
return|;
block|}
DECL|method|HostPlatform ()
specifier|private
name|HostPlatform
parameter_list|()
block|{}
block|}
end_class

end_unit

