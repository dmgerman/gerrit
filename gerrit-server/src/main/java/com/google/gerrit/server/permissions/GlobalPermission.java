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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_enum
DECL|enum|GlobalPermission
specifier|public
enum|enum
name|GlobalPermission
block|{
DECL|enumConstant|ACCESS_DATABASE
name|ACCESS_DATABASE
parameter_list|(
name|GlobalCapability
operator|.
name|ACCESS_DATABASE
parameter_list|)
operator|,
DECL|enumConstant|ADMINISTRATE_SERVER
constructor|ADMINISTRATE_SERVER(GlobalCapability.ADMINISTRATE_SERVER
block|)
enum|,
DECL|enumConstant|CREATE_ACCOUNT
name|CREATE_ACCOUNT
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_ACCOUNT
argument_list|)
operator|,
DECL|enumConstant|CREATE_GROUP
name|CREATE_GROUP
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_GROUP
argument_list|)
operator|,
DECL|enumConstant|CREATE_PROJECT
name|CREATE_PROJECT
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_PROJECT
argument_list|)
operator|,
DECL|enumConstant|EMAIL_REVIEWERS
name|EMAIL_REVIEWERS
argument_list|(
name|GlobalCapability
operator|.
name|EMAIL_REVIEWERS
argument_list|)
operator|,
DECL|enumConstant|FLUSH_CACHES
name|FLUSH_CACHES
argument_list|(
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|)
operator|,
DECL|enumConstant|KILL_TASK
name|KILL_TASK
argument_list|(
name|GlobalCapability
operator|.
name|KILL_TASK
argument_list|)
operator|,
DECL|enumConstant|MAINTAIN_SERVER
name|MAINTAIN_SERVER
argument_list|(
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
argument_list|)
operator|,
DECL|enumConstant|MODIFY_ACCOUNT
name|MODIFY_ACCOUNT
argument_list|(
name|GlobalCapability
operator|.
name|MODIFY_ACCOUNT
argument_list|)
operator|,
DECL|enumConstant|RUN_AS
name|RUN_AS
argument_list|(
name|GlobalCapability
operator|.
name|RUN_AS
argument_list|)
operator|,
DECL|enumConstant|RUN_GC
name|RUN_GC
argument_list|(
name|GlobalCapability
operator|.
name|RUN_GC
argument_list|)
operator|,
DECL|enumConstant|STREAM_EVENTS
name|STREAM_EVENTS
argument_list|(
name|GlobalCapability
operator|.
name|STREAM_EVENTS
argument_list|)
operator|,
DECL|enumConstant|VIEW_ALL_ACCOUNTS
name|VIEW_ALL_ACCOUNTS
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_ALL_ACCOUNTS
argument_list|)
operator|,
DECL|enumConstant|VIEW_CACHES
name|VIEW_CACHES
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_CACHES
argument_list|)
operator|,
DECL|enumConstant|VIEW_CONNECTIONS
name|VIEW_CONNECTIONS
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_CONNECTIONS
argument_list|)
operator|,
DECL|enumConstant|VIEW_PLUGINS
name|VIEW_PLUGINS
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_PLUGINS
argument_list|)
operator|,
DECL|enumConstant|VIEW_QUEUE
name|VIEW_QUEUE
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_QUEUE
argument_list|)
enum|;
end_enum

begin_decl_stmt
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|GlobalPermission (String name)
name|GlobalPermission
argument_list|(
name|String
name|name
argument_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
block|;   }
comment|/** @return name used in {@code project.config} permissions. */
DECL|method|permissionName ()
specifier|public
name|String
name|permissionName
argument_list|()
block|{
return|return
name|name
return|;
block|}
end_expr_stmt

begin_function
DECL|method|describeForException ()
specifier|public
name|String
name|describeForException
parameter_list|()
block|{
return|return
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|replace
argument_list|(
literal|'_'
argument_list|,
literal|' '
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

