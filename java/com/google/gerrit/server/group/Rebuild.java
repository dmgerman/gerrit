begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|MoreObjects
operator|.
name|firstNonNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
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
name|GlobalCapability
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
name|annotations
operator|.
name|RequiresCapability
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
name|BinaryResult
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
name|MethodNotAllowedException
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
name|RestApiException
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|config
operator|.
name|AllUsersName
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
name|group
operator|.
name|Rebuild
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
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|db
operator|.
name|GroupBundle
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
name|group
operator|.
name|db
operator|.
name|GroupRebuilder
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
name|GroupsMigration
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
name|update
operator|.
name|RefUpdateUtil
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
name|OrmDuplicateKeyException
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
name|List
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
name|Repository
import|;
end_import

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
annotation|@
name|Singleton
DECL|class|Rebuild
specifier|public
class|class
name|Rebuild
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|force
specifier|public
name|Boolean
name|force
decl_stmt|;
block|}
DECL|field|allUsers
specifier|private
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|bundleFactory
specifier|private
specifier|final
name|GroupBundle
operator|.
name|Factory
name|bundleFactory
decl_stmt|;
DECL|field|rebuilder
specifier|private
specifier|final
name|GroupRebuilder
name|rebuilder
decl_stmt|;
DECL|field|migration
specifier|private
specifier|final
name|GroupsMigration
name|migration
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
annotation|@
name|Inject
DECL|method|Rebuild ( AllUsersName allUsers, GitRepositoryManager repoManager, GroupBundle.Factory bundleFactory, GroupRebuilder rebuilder, GroupsMigration migration, Provider<ReviewDb> db)
name|Rebuild
parameter_list|(
name|AllUsersName
name|allUsers
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|GroupBundle
operator|.
name|Factory
name|bundleFactory
parameter_list|,
name|GroupRebuilder
name|rebuilder
parameter_list|,
name|GroupsMigration
name|migration
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|)
block|{
name|this
operator|.
name|allUsers
operator|=
name|allUsers
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|bundleFactory
operator|=
name|bundleFactory
expr_stmt|;
name|this
operator|.
name|rebuilder
operator|=
name|rebuilder
expr_stmt|;
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource rsrc, Input input)
specifier|public
name|BinaryResult
name|apply
parameter_list|(
name|GroupResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|ConfigInvalidException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|boolean
name|force
init|=
name|firstNonNull
argument_list|(
name|input
operator|.
name|force
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|migration
operator|.
name|writeToNoteDb
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"NoteDb writes must be enabled"
argument_list|)
throw|;
block|}
if|if
condition|(
name|migration
operator|.
name|readFromNoteDb
argument_list|()
operator|&&
name|force
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"NoteDb reads must not be enabled when force=true"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|rsrc
operator|.
name|isInternalGroup
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"Not an internal group"
argument_list|)
throw|;
block|}
name|AccountGroup
operator|.
name|UUID
name|uuid
init|=
name|rsrc
operator|.
name|getGroup
argument_list|()
operator|.
name|getGroupUUID
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsers
argument_list|)
init|)
block|{
if|if
condition|(
name|force
condition|)
block|{
name|RefUpdateUtil
operator|.
name|deleteChecked
argument_list|(
name|repo
argument_list|,
name|RefNames
operator|.
name|refsGroups
argument_list|(
name|uuid
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|GroupBundle
name|reviewDbBundle
init|=
name|bundleFactory
operator|.
name|fromReviewDb
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|asInternalGroup
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|rebuilder
operator|.
name|rebuild
argument_list|(
name|repo
argument_list|,
name|reviewDbBundle
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmDuplicateKeyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Group already exists in NoteDb"
argument_list|)
throw|;
block|}
name|GroupBundle
name|noteDbBundle
init|=
name|bundleFactory
operator|.
name|fromNoteDb
argument_list|(
name|repo
argument_list|,
name|uuid
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|diffs
init|=
name|GroupBundle
operator|.
name|compare
argument_list|(
name|reviewDbBundle
argument_list|,
name|noteDbBundle
argument_list|)
decl_stmt|;
if|if
condition|(
name|diffs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|BinaryResult
operator|.
name|create
argument_list|(
literal|"No differences between ReviewDb and NoteDb"
argument_list|)
return|;
block|}
return|return
name|BinaryResult
operator|.
name|create
argument_list|(
name|diffs
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|"\n"
argument_list|,
literal|"Differences between ReviewDb and NoteDb:\n"
argument_list|,
literal|"\n"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

