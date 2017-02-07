begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|sshd
operator|.
name|CommandMetaData
operator|.
name|Mode
operator|.
name|MASTER_OR_SLAVE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefDatabase
operator|.
name|ALL
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|account
operator|.
name|AccountResolver
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
name|git
operator|.
name|SearchingChangeCacheImpl
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
name|TagCache
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
name|VisibleRefFilter
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
name|project
operator|.
name|ProjectControl
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
name|sshd
operator|.
name|CommandMetaData
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
name|sshd
operator|.
name|SshCommand
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
name|Map
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
name|Ref
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"ls-user-refs"
argument_list|,
name|description
operator|=
literal|"List refs visible to a specific user"
argument_list|,
name|runsAt
operator|=
name|MASTER_OR_SLAVE
argument_list|)
DECL|class|LsUserRefs
specifier|public
class|class
name|LsUserRefs
extends|extends
name|SshCommand
block|{
DECL|field|accountResolver
annotation|@
name|Inject
specifier|private
name|AccountResolver
name|accountResolver
decl_stmt|;
DECL|field|userFactory
annotation|@
name|Inject
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|db
annotation|@
name|Inject
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|tagCache
annotation|@
name|Inject
specifier|private
name|TagCache
name|tagCache
decl_stmt|;
DECL|field|changeNotesFactory
annotation|@
name|Inject
specifier|private
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
decl_stmt|;
DECL|field|changeCache
annotation|@
name|Inject
annotation|@
name|Nullable
specifier|private
name|SearchingChangeCacheImpl
name|changeCache
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--project"
argument_list|,
name|aliases
operator|=
block|{
literal|"-p"
block|}
argument_list|,
name|metaVar
operator|=
literal|"PROJECT"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"project for which the refs should be listed"
argument_list|)
DECL|field|projectControl
specifier|private
name|ProjectControl
name|projectControl
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--user"
argument_list|,
name|aliases
operator|=
block|{
literal|"-u"
block|}
argument_list|,
name|metaVar
operator|=
literal|"USER"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"user for which the groups should be listed"
argument_list|)
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--only-refs-heads"
argument_list|,
name|usage
operator|=
literal|"list only refs under refs/heads"
argument_list|)
DECL|field|onlyRefsHeads
specifier|private
name|boolean
name|onlyRefsHeads
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|Failure
block|{
name|Account
name|userAccount
decl_stmt|;
try|try
block|{
name|userAccount
operator|=
name|accountResolver
operator|.
name|find
argument_list|(
name|db
argument_list|,
name|userName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
name|die
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|userAccount
operator|==
literal|null
condition|)
block|{
name|stdout
operator|.
name|print
argument_list|(
literal|"No single user could be found when searching for: "
operator|+
name|userName
operator|+
literal|'\n'
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return;
block|}
name|IdentifiedUser
name|user
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|userAccount
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|ProjectControl
name|userProjectControl
init|=
name|projectControl
operator|.
name|forUser
argument_list|(
name|user
argument_list|)
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
name|userProjectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|)
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refsMap
init|=
operator|new
name|VisibleRefFilter
argument_list|(
name|tagCache
argument_list|,
name|changeNotesFactory
argument_list|,
name|changeCache
argument_list|,
name|repo
argument_list|,
name|userProjectControl
argument_list|,
name|db
argument_list|,
literal|true
argument_list|)
operator|.
name|filter
argument_list|(
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|ALL
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|ref
range|:
name|refsMap
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|onlyRefsHeads
operator|||
name|ref
operator|.
name|startsWith
argument_list|(
name|RefNames
operator|.
name|REFS_HEADS
argument_list|)
condition|)
block|{
name|stdout
operator|.
name|println
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
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
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: Error reading refs: '"
operator|+
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|die
argument_list|(
literal|"'"
operator|+
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
operator|+
literal|"': not a git archive"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|die
argument_list|(
literal|"Error opening: '"
operator|+
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

