begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|common
operator|.
name|data
operator|.
name|IncludedInDetail
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
name|BadRequestException
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|IncorrectObjectTypeException
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
name|errors
operator|.
name|MissingObjectException
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
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|IncludedIn
class|class
name|IncludedIn
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Inject
DECL|method|IncludedIn (ReviewDb db, GitRepositoryManager repoManager)
name|IncludedIn
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|IncludedInInfo
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|ChangeControl
name|ctl
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|PatchSet
name|ps
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|ctl
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|Repository
name|r
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|r
argument_list|)
decl_stmt|;
try|try
block|{
name|rw
operator|.
name|setRetainBody
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|RevCommit
name|rev
decl_stmt|;
try|try
block|{
name|rev
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IncorrectObjectTypeException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|MissingObjectException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
operator|new
name|IncludedInInfo
argument_list|(
name|IncludedInResolver
operator|.
name|resolve
argument_list|(
name|r
argument_list|,
name|rw
argument_list|,
name|rev
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|IncludedInInfo
specifier|static
class|class
name|IncludedInInfo
block|{
DECL|field|kind
name|String
name|kind
init|=
literal|"gerritcodereview#includedininfo"
decl_stmt|;
DECL|field|branches
name|Collection
argument_list|<
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|field|tags
name|Collection
argument_list|<
name|String
argument_list|>
name|tags
decl_stmt|;
DECL|method|IncludedInInfo (IncludedInDetail in)
name|IncludedInInfo
parameter_list|(
name|IncludedInDetail
name|in
parameter_list|)
block|{
name|branches
operator|=
name|in
operator|.
name|getBranches
argument_list|()
expr_stmt|;
name|tags
operator|=
name|in
operator|.
name|getTags
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

