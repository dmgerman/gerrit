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
DECL|package|com.google.gerrit.server.rpc.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rpc
operator|.
name|account
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
name|server
operator|.
name|IdentifiedUser
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
name|account
operator|.
name|GroupCache
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
name|rpc
operator|.
name|Handler
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
name|ArrayList
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
name|Comparator
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
name|Set
import|;
end_import

begin_class
DECL|class|MyGroupsFactory
class|class
name|MyGroupsFactory
extends|extends
name|Handler
argument_list|<
name|List
argument_list|<
name|AccountGroup
argument_list|>
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ()
name|MyGroupsFactory
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|MyGroupsFactory (final GroupCache groupCache, final IdentifiedUser user)
name|MyGroupsFactory
parameter_list|(
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|effective
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
specifier|final
name|int
name|cnt
init|=
name|effective
operator|.
name|size
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|groupList
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroup
argument_list|>
argument_list|(
name|cnt
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
range|:
name|effective
control|)
block|{
name|groupList
operator|.
name|add
argument_list|(
name|groupCache
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|groupList
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AccountGroup
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|AccountGroup
name|a
parameter_list|,
name|AccountGroup
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|groupList
return|;
block|}
block|}
end_class

end_unit

