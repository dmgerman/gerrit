begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|UsedAt
import|;
end_import

begin_class
DECL|class|JdbcUtil
specifier|public
class|class
name|JdbcUtil
block|{
DECL|method|hostname (String hostname)
specifier|public
specifier|static
name|String
name|hostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
if|if
condition|(
name|hostname
operator|==
literal|null
operator|||
name|hostname
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|hostname
operator|=
literal|"localhost"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hostname
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
operator|&&
operator|!
name|hostname
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
condition|)
block|{
name|hostname
operator|=
literal|"["
operator|+
name|hostname
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|hostname
return|;
block|}
comment|// TODO(dborowitz): Still used by plugins post-ReviewDb?
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|PLUGINS_ALL
argument_list|)
DECL|method|port (String port)
specifier|public
specifier|static
name|String
name|port
parameter_list|(
name|String
name|port
parameter_list|)
block|{
if|if
condition|(
name|port
operator|!=
literal|null
operator|&&
operator|!
name|port
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|":"
operator|+
name|port
return|;
block|}
return|return
literal|""
return|;
block|}
block|}
end_class

end_unit

