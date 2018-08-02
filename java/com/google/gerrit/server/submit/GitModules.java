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
DECL|package|com.google.gerrit.server.submit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|submit
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
name|reviewdb
operator|.
name|client
operator|.
name|SubmoduleSubscription
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
name|CanonicalWebUrl
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
name|NoSuchProjectException
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
name|submit
operator|.
name|MergeOpRepoManager
operator|.
name|OpenRepo
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
name|util
operator|.
name|RequestId
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
name|util
operator|.
name|git
operator|.
name|SubmoduleSectionParser
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
name|ArrayList
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
name|Collections
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
name|errors
operator|.
name|ConfigInvalidException
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
name|BlobBasedConfig
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
name|FileMode
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
name|treewalk
operator|.
name|TreeWalk
import|;
end_import

begin_comment
comment|/**  * Loads the .gitmodules file of the specified project/branch. It can be queried which submodules  * this branch is subscribed to.  */
end_comment

begin_class
DECL|class|GitModules
specifier|public
class|class
name|GitModules
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
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Branch.NameKey project, MergeOpRepoManager m)
name|GitModules
name|create
parameter_list|(
name|Branch
operator|.
name|NameKey
name|project
parameter_list|,
name|MergeOpRepoManager
name|m
parameter_list|)
function_decl|;
block|}
DECL|field|GIT_MODULES
specifier|private
specifier|static
specifier|final
name|String
name|GIT_MODULES
init|=
literal|".gitmodules"
decl_stmt|;
DECL|field|submissionId
specifier|private
specifier|final
name|RequestId
name|submissionId
decl_stmt|;
DECL|field|branch
specifier|private
specifier|final
name|Branch
operator|.
name|NameKey
name|branch
decl_stmt|;
DECL|field|subscriptions
name|Set
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|subscriptions
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitModules ( @anonicalWebUrl @ullable String canonicalWebUrl, @Assisted Branch.NameKey branch, @Assisted MergeOpRepoManager orm)
name|GitModules
parameter_list|(
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
name|String
name|canonicalWebUrl
parameter_list|,
annotation|@
name|Assisted
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
annotation|@
name|Assisted
name|MergeOpRepoManager
name|orm
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
name|this
operator|.
name|submissionId
operator|=
name|orm
operator|.
name|getSubmissionId
argument_list|()
expr_stmt|;
name|Project
operator|.
name|NameKey
name|project
init|=
name|branch
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|logDebug
argument_list|(
literal|"Loading .gitmodules of %s for project %s"
argument_list|,
name|branch
argument_list|,
name|project
argument_list|)
expr_stmt|;
try|try
block|{
name|OpenRepo
name|or
init|=
name|orm
operator|.
name|getRepo
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|ObjectId
name|id
init|=
name|or
operator|.
name|repo
operator|.
name|resolve
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot open branch "
operator|+
name|branch
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
name|RevCommit
name|commit
init|=
name|or
operator|.
name|rw
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|or
operator|.
name|repo
argument_list|,
name|GIT_MODULES
argument_list|,
name|commit
operator|.
name|getTree
argument_list|()
argument_list|)
init|)
block|{
if|if
condition|(
name|tw
operator|==
literal|null
operator|||
operator|(
name|tw
operator|.
name|getRawMode
argument_list|(
literal|0
argument_list|)
operator|&
name|FileMode
operator|.
name|TYPE_MASK
operator|)
operator|!=
name|FileMode
operator|.
name|TYPE_FILE
condition|)
block|{
name|subscriptions
operator|=
name|Collections
operator|.
name|emptySet
argument_list|()
expr_stmt|;
name|logDebug
argument_list|(
literal|"The .gitmodules file doesn't exist in %s"
argument_list|,
name|branch
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|BlobBasedConfig
name|bbc
decl_stmt|;
try|try
block|{
name|bbc
operator|=
operator|new
name|BlobBasedConfig
argument_list|(
literal|null
argument_list|,
name|or
operator|.
name|repo
argument_list|,
name|commit
argument_list|,
name|GIT_MODULES
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not read .gitmodules of super project: "
operator|+
name|branch
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|subscriptions
operator|=
operator|new
name|SubmoduleSectionParser
argument_list|(
name|bbc
argument_list|,
name|canonicalWebUrl
argument_list|,
name|branch
argument_list|)
operator|.
name|parseAllSections
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|subscribedTo (Branch.NameKey src)
specifier|public
name|Collection
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|subscribedTo
parameter_list|(
name|Branch
operator|.
name|NameKey
name|src
parameter_list|)
block|{
name|logDebug
argument_list|(
literal|"Checking for a subscription of %s for %s"
argument_list|,
name|src
argument_list|,
name|branch
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SubmoduleSubscription
name|s
range|:
name|subscriptions
control|)
block|{
if|if
condition|(
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|equals
argument_list|(
name|src
argument_list|)
condition|)
block|{
name|logDebug
argument_list|(
literal|"Found %s"
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
DECL|method|logDebug (String msg, @Nullable Object arg)
specifier|private
name|void
name|logDebug
parameter_list|(
name|String
name|msg
parameter_list|,
annotation|@
name|Nullable
name|Object
name|arg
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
name|submissionId
operator|+
name|msg
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
DECL|method|logDebug (String msg, @Nullable Object arg1, @Nullable Object arg2)
specifier|private
name|void
name|logDebug
parameter_list|(
name|String
name|msg
parameter_list|,
annotation|@
name|Nullable
name|Object
name|arg1
parameter_list|,
annotation|@
name|Nullable
name|Object
name|arg2
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
name|submissionId
operator|+
name|msg
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

