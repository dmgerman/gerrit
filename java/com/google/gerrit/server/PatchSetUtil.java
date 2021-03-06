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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|Nullable
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
name|LabelFunction
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
name|LabelType
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
name|entities
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
name|entities
operator|.
name|PatchSet
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
name|entities
operator|.
name|PatchSetApproval
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
name|entities
operator|.
name|Project
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|git
operator|.
name|GitRepositoryManager
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
name|notedb
operator|.
name|ChangeNotes
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
name|notedb
operator|.
name|ChangeUpdate
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
name|ProjectCache
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
name|ProjectState
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_comment
comment|/** Utilities for manipulating patch sets. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PatchSetUtil
specifier|public
class|class
name|PatchSetUtil
block|{
DECL|field|approvalsUtilProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ApprovalsUtil
argument_list|>
name|approvalsUtilProvider
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Inject
DECL|method|PatchSetUtil ( Provider<ApprovalsUtil> approvalsUtilProvider, ProjectCache projectCache, GitRepositoryManager repoManager)
name|PatchSetUtil
parameter_list|(
name|Provider
argument_list|<
name|ApprovalsUtil
argument_list|>
name|approvalsUtilProvider
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|this
operator|.
name|approvalsUtilProvider
operator|=
name|approvalsUtilProvider
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
block|}
DECL|method|current (ChangeNotes notes)
specifier|public
name|PatchSet
name|current
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|get
argument_list|(
name|notes
argument_list|,
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|get (ChangeNotes notes, PatchSet.Id psId)
specifier|public
name|PatchSet
name|get
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getPatchSets
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
return|;
block|}
DECL|method|byChange (ChangeNotes notes)
specifier|public
name|ImmutableCollection
argument_list|<
name|PatchSet
argument_list|>
name|byChange
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getPatchSets
argument_list|()
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|byChangeAsMap (ChangeNotes notes)
specifier|public
name|ImmutableMap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|byChangeAsMap
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getPatchSets
argument_list|()
return|;
block|}
DECL|method|getAsMap ( ChangeNotes notes, Set<PatchSet.Id> patchSetIds)
specifier|public
name|ImmutableMap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|getAsMap
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|Set
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|patchSetIds
parameter_list|)
block|{
return|return
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|Maps
operator|.
name|filterKeys
argument_list|(
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getPatchSets
argument_list|()
argument_list|,
name|patchSetIds
operator|::
name|contains
argument_list|)
argument_list|)
return|;
block|}
DECL|method|insert ( RevWalk rw, ChangeUpdate update, PatchSet.Id psId, ObjectId commit, List<String> groups, @Nullable String pushCertificate, @Nullable String description)
specifier|public
name|PatchSet
name|insert
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|ObjectId
name|commit
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|,
annotation|@
name|Nullable
name|String
name|pushCertificate
parameter_list|,
annotation|@
name|Nullable
name|String
name|description
parameter_list|)
throws|throws
name|IOException
block|{
name|requireNonNull
argument_list|(
name|groups
argument_list|,
literal|"groups may not be null"
argument_list|)
expr_stmt|;
name|ensurePatchSetMatches
argument_list|(
name|psId
argument_list|,
name|update
argument_list|)
expr_stmt|;
name|update
operator|.
name|setCommit
argument_list|(
name|rw
argument_list|,
name|commit
argument_list|,
name|pushCertificate
argument_list|)
expr_stmt|;
name|update
operator|.
name|setPsDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|update
operator|.
name|setGroups
argument_list|(
name|groups
argument_list|)
expr_stmt|;
return|return
name|PatchSet
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
name|psId
argument_list|)
operator|.
name|commitId
argument_list|(
name|commit
argument_list|)
operator|.
name|uploader
argument_list|(
name|update
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|createdOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
name|update
operator|.
name|getWhen
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
operator|.
name|groups
argument_list|(
name|groups
argument_list|)
operator|.
name|pushCertificate
argument_list|(
name|Optional
operator|.
name|ofNullable
argument_list|(
name|pushCertificate
argument_list|)
argument_list|)
operator|.
name|description
argument_list|(
name|Optional
operator|.
name|ofNullable
argument_list|(
name|description
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|ensurePatchSetMatches (PatchSet.Id psId, ChangeUpdate update)
specifier|private
specifier|static
name|void
name|ensurePatchSetMatches
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|)
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
name|update
operator|.
name|getId
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|psId
operator|.
name|changeId
argument_list|()
operator|.
name|equals
argument_list|(
name|changeId
argument_list|)
argument_list|,
literal|"cannot modify patch set %s on update for change %s"
argument_list|,
name|psId
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
if|if
condition|(
name|update
operator|.
name|getPatchSetId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|checkArgument
argument_list|(
name|update
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|psId
argument_list|)
argument_list|,
literal|"cannot modify patch set %s on update for %s"
argument_list|,
name|psId
argument_list|,
name|update
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|update
operator|.
name|setPatchSetId
argument_list|(
name|psId
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Check if the current patch set of the change is locked. */
DECL|method|checkPatchSetNotLocked (ChangeNotes notes)
specifier|public
name|void
name|checkPatchSetNotLocked
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceConflictException
block|{
if|if
condition|(
name|isPatchSetLocked
argument_list|(
name|notes
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The current patch set of change %s is locked"
argument_list|,
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/** Is the current patch set locked against state changes? */
DECL|method|isPatchSetLocked (ChangeNotes notes)
specifier|public
name|boolean
name|isPatchSetLocked
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|IOException
block|{
name|Change
name|change
init|=
name|notes
operator|.
name|getChange
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|.
name|isMerged
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|)
decl_stmt|;
name|requireNonNull
argument_list|(
name|projectState
argument_list|,
parameter_list|()
lambda|->
name|String
operator|.
name|format
argument_list|(
literal|"Failed to load project %s"
argument_list|,
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ApprovalsUtil
name|approvalsUtil
init|=
name|approvalsUtilProvider
operator|.
name|get
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|ap
range|:
name|approvalsUtil
operator|.
name|byPatchSet
argument_list|(
name|notes
argument_list|,
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
control|)
block|{
name|LabelType
name|type
init|=
name|projectState
operator|.
name|getLabelTypes
argument_list|(
name|notes
argument_list|)
operator|.
name|byLabel
argument_list|(
name|ap
operator|.
name|label
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
name|ap
operator|.
name|value
argument_list|()
operator|==
literal|1
operator|&&
name|type
operator|.
name|getFunction
argument_list|()
operator|==
name|LabelFunction
operator|.
name|PATCH_SET_LOCK
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** Returns the commit for the given project at the given patchset revision */
DECL|method|getRevCommit (Project.NameKey project, PatchSet patchSet)
specifier|public
name|RevCommit
name|getRevCommit
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|RevCommit
name|src
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|patchSet
operator|.
name|commitId
argument_list|()
argument_list|)
decl_stmt|;
name|rw
operator|.
name|parseBody
argument_list|(
name|src
argument_list|)
expr_stmt|;
return|return
name|src
return|;
block|}
block|}
block|}
end_class

end_unit

