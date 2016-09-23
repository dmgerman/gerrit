begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|ArrayListMultimap
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
name|ImmutableCollection
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
name|ImmutableMap
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
name|ImmutableSet
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
name|ListMultimap
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
name|Multimap
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
name|Branch
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
comment|/**  * A set of changes grouped together to be submitted atomically.  *<p>  * MergeSuperSet constructs ChangeSets to accumulate intermediate  * results toward the ChangeSet it returns when done.  *<p>  * This class is not thread safe.  */
end_comment

begin_class
DECL|class|ChangeSet
specifier|public
class|class
name|ChangeSet
block|{
DECL|field|changeData
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|changeData
decl_stmt|;
comment|/**    * Additional changes not included in changeData because their    * connection to the original change is not visible to the    * current user.  That is, this map includes both    * - changes that are not visible to the current user, and    * - changes whose only relationship to the set is via a change    *   that is not visible to the current user    */
DECL|field|nonVisibleChanges
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|nonVisibleChanges
decl_stmt|;
DECL|method|index ( Iterable<ChangeData> changes, Collection<Change.Id> exclude)
specifier|private
specifier|static
name|ImmutableMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|index
parameter_list|(
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|,
name|Collection
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|exclude
parameter_list|)
block|{
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|ret
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changes
control|)
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|cd
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ret
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
operator|&&
operator|!
name|exclude
operator|.
name|contains
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|ret
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|ret
argument_list|)
return|;
block|}
DECL|method|ChangeSet ( Iterable<ChangeData> changes, Iterable<ChangeData> hiddenChanges)
specifier|public
name|ChangeSet
parameter_list|(
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|,
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|hiddenChanges
parameter_list|)
block|{
name|changeData
operator|=
name|index
argument_list|(
name|changes
argument_list|,
name|ImmutableList
operator|.
expr|<
name|Change
operator|.
name|Id
operator|>
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|nonVisibleChanges
operator|=
name|index
argument_list|(
name|hiddenChanges
argument_list|,
name|changeData
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ChangeSet (ChangeData change, boolean visible)
specifier|public
name|ChangeSet
parameter_list|(
name|ChangeData
name|change
parameter_list|,
name|boolean
name|visible
parameter_list|)
block|{
name|this
argument_list|(
name|visible
condition|?
name|ImmutableList
operator|.
name|of
argument_list|(
name|change
argument_list|)
else|:
name|ImmutableList
operator|.
expr|<
name|ChangeData
operator|>
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|change
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ids ()
specifier|public
name|ImmutableSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|ids
parameter_list|()
block|{
return|return
name|changeData
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|changesById ()
specifier|public
name|ImmutableMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|changesById
parameter_list|()
block|{
return|return
name|changeData
return|;
block|}
DECL|method|changesByBranch ()
specifier|public
name|Multimap
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|ChangeData
argument_list|>
name|changesByBranch
parameter_list|()
throws|throws
name|OrmException
block|{
name|ListMultimap
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|ChangeData
argument_list|>
name|ret
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changeData
operator|.
name|values
argument_list|()
control|)
block|{
name|ret
operator|.
name|put
argument_list|(
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|,
name|cd
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
DECL|method|changes ()
specifier|public
name|ImmutableCollection
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|()
block|{
return|return
name|changeData
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|nonVisibleIds ()
specifier|public
name|ImmutableSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|nonVisibleIds
parameter_list|()
block|{
return|return
name|nonVisibleChanges
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|nonVisibleChanges ()
specifier|public
name|ImmutableList
argument_list|<
name|ChangeData
argument_list|>
name|nonVisibleChanges
parameter_list|()
block|{
return|return
name|nonVisibleChanges
operator|.
name|values
argument_list|()
operator|.
name|asList
argument_list|()
return|;
block|}
DECL|method|furtherHiddenChanges ()
specifier|public
name|boolean
name|furtherHiddenChanges
parameter_list|()
block|{
return|return
operator|!
name|nonVisibleChanges
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|changeData
operator|.
name|size
argument_list|()
operator|+
name|nonVisibleChanges
operator|.
name|size
argument_list|()
return|;
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
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
name|ids
argument_list|()
operator|+
name|nonVisibleIds
argument_list|()
return|;
block|}
block|}
end_class

end_unit

