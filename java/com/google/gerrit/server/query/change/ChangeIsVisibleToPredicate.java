begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
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
name|change
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
name|exceptions
operator|.
name|StorageException
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
name|index
operator|.
name|query
operator|.
name|IsVisibleToPredicate
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
name|AnonymousUser
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
name|CurrentUser
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
name|IndexUtils
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|RepositoryNotFoundException
import|;
end_import

begin_class
DECL|class|ChangeIsVisibleToPredicate
specifier|public
class|class
name|ChangeIsVisibleToPredicate
extends|extends
name|IsVisibleToPredicate
argument_list|<
name|ChangeData
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
DECL|field|notesFactory
specifier|protected
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
decl_stmt|;
DECL|field|user
specifier|protected
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|permissionBackend
specifier|protected
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|projectCache
specifier|protected
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|anonymousUserProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|AnonymousUser
argument_list|>
name|anonymousUserProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeIsVisibleToPredicate ( ChangeNotes.Factory notesFactory, CurrentUser user, PermissionBackend permissionBackend, ProjectCache projectCache, Provider<AnonymousUser> anonymousUserProvider)
specifier|public
name|ChangeIsVisibleToPredicate
parameter_list|(
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|Provider
argument_list|<
name|AnonymousUser
argument_list|>
name|anonymousUserProvider
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_VISIBLETO
argument_list|,
name|IndexUtils
operator|.
name|describe
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|notesFactory
operator|=
name|notesFactory
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|anonymousUserProvider
operator|=
name|anonymousUserProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
if|if
condition|(
name|cd
operator|.
name|fastIsVisibleTo
argument_list|(
name|user
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Change
name|change
init|=
name|cd
operator|.
name|change
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ChangeNotes
name|notes
init|=
name|notesFactory
operator|.
name|createFromIndexedChange
argument_list|(
name|change
argument_list|)
decl_stmt|;
try|try
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|cd
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Filter out change %s of non-existing project %s"
argument_list|,
name|cd
argument_list|,
name|cd
operator|.
name|project
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|projectState
operator|.
name|statePermitsRead
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Filter out change %s of non-reabable project %s"
argument_list|,
name|cd
argument_list|,
name|cd
operator|.
name|project
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
literal|"unable to read project state"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|PermissionBackend
operator|.
name|WithUser
name|withUser
init|=
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|?
name|permissionBackend
operator|.
name|absentUser
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
else|:
name|permissionBackend
operator|.
name|user
argument_list|(
name|anonymousUserProvider
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|withUser
operator|.
name|indexedChange
argument_list|(
name|cd
argument_list|,
name|notes
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|instanceof
name|RepositoryNotFoundException
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Filter out change %s because the corresponding repository %s was not found"
argument_list|,
name|cd
argument_list|,
name|cd
operator|.
name|project
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
throw|throw
operator|new
name|StorageException
argument_list|(
literal|"unable to check permissions on change "
operator|+
name|cd
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Filter out non-visisble change: %s"
argument_list|,
name|cd
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|cd
operator|.
name|cacheVisibleTo
argument_list|(
name|user
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

