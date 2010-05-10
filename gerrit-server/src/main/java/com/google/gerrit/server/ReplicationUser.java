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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|reviewdb
operator|.
name|AccountGroup
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
name|server
operator|.
name|config
operator|.
name|AuthConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|ReplicationUser
specifier|public
class|class
name|ReplicationUser
extends|extends
name|CurrentUser
block|{
comment|/** Magic set of groups enabling read of any project and reference. */
DECL|field|EVERYTHING_VISIBLE
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|EVERYTHING_VISIBLE
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (@ssisted Set<AccountGroup.Id> authGroups)
name|ReplicationUser
name|create
parameter_list|(
annotation|@
name|Assisted
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|authGroups
parameter_list|)
function_decl|;
block|}
DECL|field|effectiveGroups
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|effectiveGroups
decl_stmt|;
annotation|@
name|Inject
DECL|method|ReplicationUser (AuthConfig authConfig, @Assisted Set<AccountGroup.Id> authGroups)
specifier|protected
name|ReplicationUser
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|,
annotation|@
name|Assisted
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|authGroups
parameter_list|)
block|{
name|super
argument_list|(
name|AccessPath
operator|.
name|REPLICATION
argument_list|,
name|authConfig
argument_list|)
expr_stmt|;
if|if
condition|(
name|authGroups
operator|==
name|EVERYTHING_VISIBLE
condition|)
block|{
name|effectiveGroups
operator|=
name|EVERYTHING_VISIBLE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|authGroups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Only include the registered groups if no specific groups
comment|// were provided. This allows an administrator to configure
comment|// a replication user with a narrower view of the system than
comment|// all other users, such as when replicating from an internal
comment|// company server to a public open source distribution site.
comment|//
name|effectiveGroups
operator|=
name|authConfig
operator|.
name|getRegisteredGroups
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|effectiveGroups
operator|=
name|copy
argument_list|(
name|authGroups
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copy (Set<AccountGroup.Id> groups)
specifier|private
specifier|static
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|copy
parameter_list|(
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groups
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|(
name|groups
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getEffectiveGroups ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getEffectiveGroups
parameter_list|()
block|{
return|return
name|effectiveGroups
return|;
block|}
annotation|@
name|Override
DECL|method|getStarredChanges ()
specifier|public
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|getStarredChanges
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|isEverythingVisible ()
specifier|public
name|boolean
name|isEverythingVisible
parameter_list|()
block|{
return|return
name|getEffectiveGroups
argument_list|()
operator|==
name|EVERYTHING_VISIBLE
return|;
block|}
block|}
end_class

end_unit

