begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|hash
operator|.
name|Hasher
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
operator|.
name|Hashing
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|TimeUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_comment
comment|/** Unique identifier for an end-user request, used in logs and similar. */
end_comment

begin_class
DECL|class|RequestId
specifier|public
class|class
name|RequestId
block|{
DECL|field|MACHINE_ID
specifier|private
specifier|static
specifier|final
name|String
name|MACHINE_ID
decl_stmt|;
static|static
block|{
name|String
name|id
decl_stmt|;
try|try
block|{
name|id
operator|=
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getHostAddress
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|e
parameter_list|)
block|{
name|id
operator|=
literal|"unknown"
expr_stmt|;
block|}
name|MACHINE_ID
operator|=
name|id
expr_stmt|;
block|}
DECL|method|forChange (Change c)
specifier|public
specifier|static
name|RequestId
name|forChange
parameter_list|(
name|Change
name|c
parameter_list|)
block|{
return|return
operator|new
name|RequestId
argument_list|(
name|c
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|forProject (Project.NameKey p)
specifier|public
specifier|static
name|RequestId
name|forProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
block|{
return|return
operator|new
name|RequestId
argument_list|(
name|p
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|field|str
specifier|private
specifier|final
name|String
name|str
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
comment|// Use Hashing.sha1 for compatibility.
DECL|method|RequestId (String resourceId)
specifier|private
name|RequestId
parameter_list|(
name|String
name|resourceId
parameter_list|)
block|{
name|Hasher
name|h
init|=
name|Hashing
operator|.
name|sha1
argument_list|()
operator|.
name|newHasher
argument_list|()
decl_stmt|;
name|h
operator|.
name|putLong
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|putUnencodedChars
argument_list|(
name|MACHINE_ID
argument_list|)
expr_stmt|;
name|str
operator|=
literal|"["
operator|+
name|resourceId
operator|+
literal|"-"
operator|+
name|TimeUtil
operator|.
name|nowTs
argument_list|()
operator|.
name|getTime
argument_list|()
operator|+
literal|"-"
operator|+
name|h
operator|.
name|hash
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|8
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|str
return|;
block|}
DECL|method|toStringForStorage ()
specifier|public
name|String
name|toStringForStorage
parameter_list|()
block|{
return|return
name|str
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

