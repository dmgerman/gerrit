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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|Maps
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
name|data
operator|.
name|GroupDescription
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
name|data
operator|.
name|GroupInfo
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
name|data
operator|.
name|GroupInfoCache
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
name|Map
import|;
end_import

begin_comment
comment|/** Efficiently builds a {@link GroupInfoCache}. */
end_comment

begin_class
DECL|class|GroupInfoCacheFactory
specifier|public
class|class
name|GroupInfoCacheFactory
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|GroupInfoCacheFactory
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|out
specifier|private
specifier|final
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|GroupDescription
operator|.
name|Basic
argument_list|>
name|out
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupInfoCacheFactory (GroupBackend groupBackend)
name|GroupInfoCacheFactory
parameter_list|(
name|GroupBackend
name|groupBackend
parameter_list|)
block|{
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
block|}
comment|/**    * Indicate a group will be needed later on.    *    * @param uuid identity that will be needed in the future; may be null.    */
DECL|method|want (final AccountGroup.UUID uuid)
specifier|public
name|void
name|want
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
if|if
condition|(
name|uuid
operator|!=
literal|null
operator|&&
operator|!
name|out
operator|.
name|containsKey
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
name|out
operator|.
name|put
argument_list|(
name|uuid
argument_list|,
name|groupBackend
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Indicate one or more groups will be needed later on. */
DECL|method|want (final Iterable<AccountGroup.UUID> uuids)
specifier|public
name|void
name|want
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|uuids
parameter_list|)
block|{
for|for
control|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|uuid
range|:
name|uuids
control|)
block|{
name|want
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|get (final AccountGroup.UUID uuid)
specifier|public
name|GroupDescription
operator|.
name|Basic
name|get
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|want
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
return|;
block|}
comment|/**    * Create an GroupInfoCache with the currently loaded AccountGroup entities.    * */
DECL|method|create ()
specifier|public
name|GroupInfoCache
name|create
parameter_list|()
block|{
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|r
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|out
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupDescription
operator|.
name|Basic
name|a
range|:
name|out
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|GroupInfo
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GroupInfoCache
argument_list|(
name|r
argument_list|)
return|;
block|}
block|}
end_class

end_unit

