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
name|client
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
name|client
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
name|effectiveGroups
operator|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
argument_list|(
name|authGroups
argument_list|)
expr_stmt|;
if|if
condition|(
name|effectiveGroups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|effectiveGroups
operator|.
name|addAll
argument_list|(
name|authConfig
operator|.
name|getRegisteredGroups
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|effectiveGroups
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|effectiveGroups
argument_list|)
expr_stmt|;
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
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|effectiveGroups
argument_list|)
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
block|}
end_class

end_unit

