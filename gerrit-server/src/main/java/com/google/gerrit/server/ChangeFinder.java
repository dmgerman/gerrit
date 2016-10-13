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
name|common
operator|.
name|primitives
operator|.
name|Ints
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
name|change
operator|.
name|ChangeTriplet
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
name|project
operator|.
name|ChangeControl
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
name|project
operator|.
name|NoSuchChangeException
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|Singleton
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

begin_class
annotation|@
name|Singleton
DECL|class|ChangeFinder
specifier|public
class|class
name|ChangeFinder
block|{
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeFinder (Provider<InternalChangeQuery> queryProvider)
name|ChangeFinder
parameter_list|(
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|)
block|{
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
block|}
comment|/**    * Find changes matching the given identifier.    *    * @param id change identifier, either a numeric ID, a Change-Id, or    *     project~branch~id triplet.    * @param user user to wrap in controls.    * @return possibly-empty list of controls for all matching changes,    *     corresponding to the given user; may or may not be visible.    * @throws OrmException if an error occurred querying the database.    */
DECL|method|find (String id, CurrentUser user)
specifier|public
name|List
argument_list|<
name|ChangeControl
argument_list|>
name|find
parameter_list|(
name|String
name|id
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|OrmException
block|{
comment|// Use the index to search for changes, but don't return any stored fields,
comment|// to force rereading in case the index is stale.
name|InternalChangeQuery
name|query
init|=
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|noFields
argument_list|()
decl_stmt|;
comment|// Try legacy id
if|if
condition|(
operator|!
name|id
operator|.
name|isEmpty
argument_list|()
operator|&&
name|id
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|!=
literal|'0'
condition|)
block|{
name|Integer
name|n
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
condition|)
block|{
return|return
name|asChangeControls
argument_list|(
name|query
operator|.
name|byLegacyChangeId
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|n
argument_list|)
argument_list|)
argument_list|,
name|user
argument_list|)
return|;
block|}
block|}
comment|// Try isolated changeId
if|if
condition|(
operator|!
name|id
operator|.
name|contains
argument_list|(
literal|"~"
argument_list|)
condition|)
block|{
return|return
name|asChangeControls
argument_list|(
name|query
operator|.
name|byKeyPrefix
argument_list|(
name|id
argument_list|)
argument_list|,
name|user
argument_list|)
return|;
block|}
comment|// Try change triplet
name|Optional
argument_list|<
name|ChangeTriplet
argument_list|>
name|triplet
init|=
name|ChangeTriplet
operator|.
name|parse
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|triplet
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|asChangeControls
argument_list|(
name|query
operator|.
name|byBranchKey
argument_list|(
name|triplet
operator|.
name|get
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|,
name|triplet
operator|.
name|get
argument_list|()
operator|.
name|id
argument_list|()
argument_list|)
argument_list|,
name|user
argument_list|)
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
DECL|method|findOne (Change.Id id, CurrentUser user)
specifier|public
name|ChangeControl
name|findOne
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchChangeException
block|{
name|List
argument_list|<
name|ChangeControl
argument_list|>
name|ctls
init|=
name|find
argument_list|(
name|id
argument_list|,
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|ctls
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|id
argument_list|)
throw|;
block|}
return|return
name|ctls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|find (Change.Id id, CurrentUser user)
specifier|public
name|List
argument_list|<
name|ChangeControl
argument_list|>
name|find
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|OrmException
block|{
comment|// Use the index to search for changes, but don't return any stored fields,
comment|// to force rereading in case the index is stale.
name|InternalChangeQuery
name|query
init|=
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|noFields
argument_list|()
decl_stmt|;
return|return
name|asChangeControls
argument_list|(
name|query
operator|.
name|byLegacyChangeId
argument_list|(
name|id
argument_list|)
argument_list|,
name|user
argument_list|)
return|;
block|}
DECL|method|asChangeControls (List<ChangeData> cds, CurrentUser user)
specifier|private
name|List
argument_list|<
name|ChangeControl
argument_list|>
name|asChangeControls
parameter_list|(
name|List
argument_list|<
name|ChangeData
argument_list|>
name|cds
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|ChangeControl
argument_list|>
name|ctls
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|cds
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|cds
control|)
block|{
name|ctls
operator|.
name|add
argument_list|(
name|cd
operator|.
name|changeControl
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ctls
return|;
block|}
block|}
end_class

end_unit

