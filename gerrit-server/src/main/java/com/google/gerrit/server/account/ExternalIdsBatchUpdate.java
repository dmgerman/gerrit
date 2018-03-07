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
import|import static
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
name|ExternalId
operator|.
name|toAccountExternalIds
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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

begin_comment
comment|/** This class allows to do batch updates to external IDs. */
end_comment

begin_class
DECL|class|ExternalIdsBatchUpdate
specifier|public
class|class
name|ExternalIdsBatchUpdate
block|{
DECL|field|toAdd
specifier|private
specifier|final
name|Set
argument_list|<
name|ExternalId
argument_list|>
name|toAdd
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|toDelete
specifier|private
specifier|final
name|Set
argument_list|<
name|ExternalId
argument_list|>
name|toDelete
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** Adds an external ID replacement to the batch. */
DECL|method|replace (ExternalId extIdToDelete, ExternalId extIdToAdd)
specifier|public
name|void
name|replace
parameter_list|(
name|ExternalId
name|extIdToDelete
parameter_list|,
name|ExternalId
name|extIdToAdd
parameter_list|)
block|{
name|ExternalIdsUpdate
operator|.
name|checkSameAccount
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|extIdToDelete
argument_list|,
name|extIdToAdd
argument_list|)
argument_list|)
expr_stmt|;
name|toAdd
operator|.
name|add
argument_list|(
name|extIdToAdd
argument_list|)
expr_stmt|;
name|toDelete
operator|.
name|add
argument_list|(
name|extIdToDelete
argument_list|)
expr_stmt|;
block|}
comment|/**    * Commits this batch.    *    *<p>This means external ID replacements which were prepared by invoking {@link    * #replace(ExternalId, ExternalId)} are now executed. Deletion of external IDs is done before    * adding the new external IDs. This means if an external ID is specified for deletion and an    * external ID with the same key is specified to be added, the old external ID with that key is    * deleted first and then the new external ID is added (so the external ID for that key is    * replaced).    */
DECL|method|commit (ReviewDb db)
specifier|public
name|void
name|commit
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|toDelete
operator|.
name|isEmpty
argument_list|()
operator|&&
name|toAdd
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|delete
argument_list|(
name|toAccountExternalIds
argument_list|(
name|toDelete
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|toAccountExternalIds
argument_list|(
name|toAdd
argument_list|)
argument_list|)
expr_stmt|;
name|toAdd
operator|.
name|clear
argument_list|()
expr_stmt|;
name|toDelete
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

