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
DECL|package|com.google.gerrit.server.query.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|group
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|Iterables
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
name|flogger
operator|.
name|FluentLogger
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
name|index
operator|.
name|IndexConfig
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
name|index
operator|.
name|query
operator|.
name|InternalQuery
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|Account
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
name|server
operator|.
name|group
operator|.
name|InternalGroup
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
name|index
operator|.
name|group
operator|.
name|GroupIndexCollection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * Query wrapper for the group index.  *  *<p>Instances are one-time-use. Other singleton classes should inject a Provider rather than  * holding on to a single instance.  */
end_comment

begin_class
DECL|class|InternalGroupQuery
specifier|public
class|class
name|InternalGroupQuery
extends|extends
name|InternalQuery
argument_list|<
name|InternalGroup
argument_list|,
name|InternalGroupQuery
argument_list|>
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
annotation|@
name|Inject
DECL|method|InternalGroupQuery ( GroupQueryProcessor queryProcessor, GroupIndexCollection indexes, IndexConfig indexConfig)
name|InternalGroupQuery
parameter_list|(
name|GroupQueryProcessor
name|queryProcessor
parameter_list|,
name|GroupIndexCollection
name|indexes
parameter_list|,
name|IndexConfig
name|indexConfig
parameter_list|)
block|{
name|super
argument_list|(
name|queryProcessor
argument_list|,
name|indexes
argument_list|,
name|indexConfig
argument_list|)
expr_stmt|;
block|}
DECL|method|byName (AccountGroup.NameKey groupName)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|byName
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|groupName
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|getOnlyGroup
argument_list|(
name|GroupPredicates
operator|.
name|name
argument_list|(
name|groupName
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
literal|"group name '"
operator|+
name|groupName
operator|+
literal|"'"
argument_list|)
return|;
block|}
DECL|method|byId (AccountGroup.Id groupId)
specifier|public
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|byId
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|getOnlyGroup
argument_list|(
name|GroupPredicates
operator|.
name|id
argument_list|(
name|groupId
argument_list|)
argument_list|,
literal|"group id '"
operator|+
name|groupId
operator|+
literal|"'"
argument_list|)
return|;
block|}
DECL|method|byMember (Account.Id memberId)
specifier|public
name|List
argument_list|<
name|InternalGroup
argument_list|>
name|byMember
parameter_list|(
name|Account
operator|.
name|Id
name|memberId
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|GroupPredicates
operator|.
name|member
argument_list|(
name|memberId
argument_list|)
argument_list|)
return|;
block|}
DECL|method|bySubgroup (AccountGroup.UUID subgroupId)
specifier|public
name|List
argument_list|<
name|InternalGroup
argument_list|>
name|bySubgroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|subgroupId
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|GroupPredicates
operator|.
name|subgroup
argument_list|(
name|subgroupId
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getOnlyGroup ( Predicate<InternalGroup> predicate, String groupDescription)
specifier|private
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|getOnlyGroup
parameter_list|(
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|predicate
parameter_list|,
name|String
name|groupDescription
parameter_list|)
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|InternalGroup
argument_list|>
name|groups
init|=
name|query
argument_list|(
name|predicate
argument_list|)
decl_stmt|;
if|if
condition|(
name|groups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
if|if
condition|(
name|groups
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|groups
argument_list|)
argument_list|)
return|;
block|}
name|ImmutableList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupUuids
init|=
name|groups
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|InternalGroup
operator|::
name|getGroupUUID
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Ambiguous %s for groups %s."
argument_list|,
name|groupDescription
argument_list|,
name|groupUuids
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

