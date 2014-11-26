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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|ChangeHooks
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
name|AuthException
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
name|Response
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
name|RestModifyView
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|DeleteBranch
operator|.
name|Input
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefUpdate
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteBranch
specifier|public
class|class
name|DeleteBranch
implements|implements
name|RestModifyView
argument_list|<
name|BranchResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DeleteBranch
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Input
specifier|static
class|class
name|Input
block|{   }
DECL|field|identifiedUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|referenceUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|referenceUpdated
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteBranch (Provider<IdentifiedUser> identifiedUser, GitRepositoryManager repoManager, Provider<ReviewDb> dbProvider, GitReferenceUpdated referenceUpdated, ChangeHooks hooks)
name|DeleteBranch
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|GitReferenceUpdated
name|referenceUpdated
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|)
block|{
name|this
operator|.
name|identifiedUser
operator|=
name|identifiedUser
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|referenceUpdated
operator|=
name|referenceUpdated
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (BranchResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|BranchResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|ResourceConflictException
throws|,
name|OrmException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|controlForRef
argument_list|(
name|rsrc
operator|.
name|getBranchKey
argument_list|()
argument_list|)
operator|.
name|canDelete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Cannot delete branch"
argument_list|)
throw|;
block|}
if|if
condition|(
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|changes
argument_list|()
operator|.
name|byBranchOpenAll
argument_list|(
name|rsrc
operator|.
name|getBranchKey
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"branch "
operator|+
name|rsrc
operator|.
name|getBranchKey
argument_list|()
operator|+
literal|" has open changes"
argument_list|)
throw|;
block|}
name|Repository
name|r
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|RefUpdate
operator|.
name|Result
name|result
decl_stmt|;
name|RefUpdate
name|u
decl_stmt|;
try|try
block|{
name|u
operator|=
name|r
operator|.
name|updateRef
argument_list|(
name|rsrc
operator|.
name|getRef
argument_list|()
argument_list|)
expr_stmt|;
name|u
operator|.
name|setForceUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|result
operator|=
name|u
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot delete "
operator|+
name|rsrc
operator|.
name|getBranchKey
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|NEW
case|:
case|case
name|NO_CHANGE
case|:
case|case
name|FAST_FORWARD
case|:
case|case
name|FORCED
case|:
name|referenceUpdated
operator|.
name|fire
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|u
argument_list|)
expr_stmt|;
name|hooks
operator|.
name|doRefUpdatedHook
argument_list|(
name|rsrc
operator|.
name|getBranchKey
argument_list|()
argument_list|,
name|u
argument_list|,
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
name|ResultSet
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|submoduleSubscriptions
init|=
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|bySuperProject
argument_list|(
name|rsrc
operator|.
name|getBranchKey
argument_list|()
argument_list|)
decl_stmt|;
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|delete
argument_list|(
name|submoduleSubscriptions
argument_list|)
expr_stmt|;
break|break;
case|case
name|REJECTED_CURRENT_BRANCH
case|:
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot delete "
operator|+
name|rsrc
operator|.
name|getBranchKey
argument_list|()
operator|+
literal|": "
operator|+
name|result
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"cannot delete current branch"
argument_list|)
throw|;
default|default:
name|log
operator|.
name|error
argument_list|(
literal|"Cannot delete "
operator|+
name|rsrc
operator|.
name|getBranchKey
argument_list|()
operator|+
literal|": "
operator|+
name|result
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"cannot delete branch: "
operator|+
name|result
operator|.
name|name
argument_list|()
argument_list|)
throw|;
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
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
block|}
end_class

end_unit

