begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Server wide capabilities. Represented as {@link Permission} objects. */
end_comment

begin_class
DECL|class|GlobalCapability
specifier|public
class|class
name|GlobalCapability
block|{
comment|/**    * Denotes the server's administrators.    *<p>    * This is similar to UNIX root, or Windows SYSTEM account. Any user that    * has this capability can perform almost any other action, or can grant    * themselves the power to perform any other action on the site. Most of    * the other capabilities and permissions fall-back to the predicate    * "OR user has capablity ADMINISTRATE_SERVER".    */
DECL|field|ADMINISTRATE_SERVER
specifier|public
specifier|static
specifier|final
name|String
name|ADMINISTRATE_SERVER
init|=
literal|"administrateServer"
decl_stmt|;
comment|/** Can create any account on the server. */
DECL|field|CREATE_ACCOUNT
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_ACCOUNT
init|=
literal|"createAccount"
decl_stmt|;
comment|/** Can create any group on the server. */
DECL|field|CREATE_GROUP
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_GROUP
init|=
literal|"createGroup"
decl_stmt|;
comment|/** Can create any project on the server. */
DECL|field|CREATE_PROJECT
specifier|public
specifier|static
specifier|final
name|String
name|CREATE_PROJECT
init|=
literal|"createProject"
decl_stmt|;
comment|/** Can flush any cache except the active web_sessions cache. */
DECL|field|FLUSH_CACHES
specifier|public
specifier|static
specifier|final
name|String
name|FLUSH_CACHES
init|=
literal|"flushCaches"
decl_stmt|;
comment|/** Can terminate any task using the kill command. */
DECL|field|KILL_TASK
specifier|public
specifier|static
specifier|final
name|String
name|KILL_TASK
init|=
literal|"killTask"
decl_stmt|;
comment|/** Queue a user can access to submit their tasks to. */
DECL|field|PRIORITY
specifier|public
specifier|static
specifier|final
name|String
name|PRIORITY
init|=
literal|"priority"
decl_stmt|;
comment|/** Maximum result limit per executed query. */
DECL|field|QUERY_LIMIT
specifier|public
specifier|static
specifier|final
name|String
name|QUERY_LIMIT
init|=
literal|"queryLimit"
decl_stmt|;
comment|/** Forcefully restart replication to any configured destination. */
DECL|field|START_REPLICATION
specifier|public
specifier|static
specifier|final
name|String
name|START_REPLICATION
init|=
literal|"startReplication"
decl_stmt|;
comment|/** Can view the server's current cache states. */
DECL|field|VIEW_CACHES
specifier|public
specifier|static
specifier|final
name|String
name|VIEW_CACHES
init|=
literal|"viewCaches"
decl_stmt|;
comment|/** Can view open connections to the server's SSH port. */
DECL|field|VIEW_CONNECTIONS
specifier|public
specifier|static
specifier|final
name|String
name|VIEW_CONNECTIONS
init|=
literal|"viewConnections"
decl_stmt|;
comment|/** Can view all pending tasks in the queue (not just the filtered set). */
DECL|field|VIEW_QUEUE
specifier|public
specifier|static
specifier|final
name|String
name|VIEW_QUEUE
init|=
literal|"viewQueue"
decl_stmt|;
DECL|field|NAMES_LC
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|NAMES_LC
decl_stmt|;
static|static
block|{
name|NAMES_LC
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|ADMINISTRATE_SERVER
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|CREATE_ACCOUNT
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|CREATE_GROUP
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|CREATE_PROJECT
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|FLUSH_CACHES
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|KILL_TASK
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|PRIORITY
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|QUERY_LIMIT
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|START_REPLICATION
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|VIEW_CACHES
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|VIEW_CONNECTIONS
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|NAMES_LC
operator|.
name|add
argument_list|(
name|VIEW_QUEUE
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** @return true if the name is recognized as a capability name. */
DECL|method|isCapability (String varName)
specifier|public
specifier|static
name|boolean
name|isCapability
parameter_list|(
name|String
name|varName
parameter_list|)
block|{
return|return
name|NAMES_LC
operator|.
name|contains
argument_list|(
name|varName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
comment|/** @return true if the capability should have a range attached. */
DECL|method|hasRange (String varName)
specifier|public
specifier|static
name|boolean
name|hasRange
parameter_list|(
name|String
name|varName
parameter_list|)
block|{
return|return
name|QUERY_LIMIT
operator|.
name|equalsIgnoreCase
argument_list|(
name|varName
argument_list|)
return|;
block|}
comment|/** @return the valid range for the capability if it has one, otherwise null. */
DECL|method|getRange (String varName)
specifier|public
specifier|static
name|PermissionRange
operator|.
name|WithDefaults
name|getRange
parameter_list|(
name|String
name|varName
parameter_list|)
block|{
if|if
condition|(
name|QUERY_LIMIT
operator|.
name|equalsIgnoreCase
argument_list|(
name|varName
argument_list|)
condition|)
block|{
return|return
operator|new
name|PermissionRange
operator|.
name|WithDefaults
argument_list|(
name|varName
argument_list|,
literal|0
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
literal|0
argument_list|,
literal|500
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|GlobalCapability ()
specifier|private
name|GlobalCapability
parameter_list|()
block|{
comment|// Utility class, do not create instances.
block|}
block|}
end_class

end_unit

