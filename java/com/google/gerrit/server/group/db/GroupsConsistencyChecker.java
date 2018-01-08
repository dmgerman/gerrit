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
DECL|package|com.google.gerrit.server.group.db
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
operator|.
name|db
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
name|extensions
operator|.
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInfo
operator|.
name|ConsistencyProblemInfo
operator|.
name|error
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInfo
operator|.
name|ConsistencyProblemInfo
operator|.
name|warning
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
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInfo
operator|.
name|ConsistencyProblemInfo
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
name|server
operator|.
name|account
operator|.
name|AccountState
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
name|Accounts
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
name|GroupBackend
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
name|InternalGroup
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
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

begin_comment
comment|/**  * Checks individual groups for oddities, such as cycles, non-existent subgroups, etc. Only works if  * we are writing to NoteDb.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GroupsConsistencyChecker
specifier|public
class|class
name|GroupsConsistencyChecker
block|{
DECL|field|allUsersName
specifier|private
specifier|final
name|AllUsersName
name|allUsersName
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|accounts
specifier|private
specifier|final
name|Accounts
name|accounts
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|globalChecker
specifier|private
specifier|final
name|GroupsNoteDbConsistencyChecker
name|globalChecker
decl_stmt|;
DECL|field|groupsMigration
specifier|private
specifier|final
name|GroupsMigration
name|groupsMigration
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsConsistencyChecker ( AllUsersName allUsersName, GroupBackend groupBackend, Accounts accounts, GitRepositoryManager repositoryManager, GroupsNoteDbConsistencyChecker globalChecker, GroupsMigration groupsMigration)
name|GroupsConsistencyChecker
parameter_list|(
name|AllUsersName
name|allUsersName
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|Accounts
name|accounts
parameter_list|,
name|GitRepositoryManager
name|repositoryManager
parameter_list|,
name|GroupsNoteDbConsistencyChecker
name|globalChecker
parameter_list|,
name|GroupsMigration
name|groupsMigration
parameter_list|)
block|{
name|this
operator|.
name|allUsersName
operator|=
name|allUsersName
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repositoryManager
expr_stmt|;
name|this
operator|.
name|globalChecker
operator|=
name|globalChecker
expr_stmt|;
name|this
operator|.
name|groupsMigration
operator|=
name|groupsMigration
expr_stmt|;
block|}
comment|/** Checks that all internal group references exist, and that no groups have cycles. */
DECL|method|check ()
specifier|public
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|check
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|groupsMigration
operator|.
name|writeToNoteDb
argument_list|()
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|GroupsNoteDbConsistencyChecker
operator|.
name|Result
name|result
init|=
name|globalChecker
operator|.
name|check
argument_list|(
name|repo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|problems
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|result
operator|.
name|problems
return|;
block|}
for|for
control|(
name|InternalGroup
name|g
range|:
name|result
operator|.
name|uuidToGroupMap
operator|.
name|values
argument_list|()
control|)
block|{
name|result
operator|.
name|problems
operator|.
name|addAll
argument_list|(
name|checkGroup
argument_list|(
name|g
argument_list|,
name|result
operator|.
name|uuidToGroupMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|problems
return|;
block|}
block|}
comment|/** Checks the metadata for a single group for problems. */
DECL|method|checkGroup ( InternalGroup g, Map<AccountGroup.UUID, InternalGroup> byUUID)
specifier|private
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|checkGroup
parameter_list|(
name|InternalGroup
name|g
parameter_list|,
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|InternalGroup
argument_list|>
name|byUUID
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|problems
operator|.
name|addAll
argument_list|(
name|checkCycle
argument_list|(
name|g
argument_list|,
name|byUUID
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|byUUID
operator|.
name|get
argument_list|(
name|g
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
operator|==
literal|null
operator|&&
name|groupBackend
operator|.
name|get
argument_list|(
name|g
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|problems
operator|.
name|add
argument_list|(
name|error
argument_list|(
literal|"group %s (%s) has nonexistent owner group %s"
argument_list|,
name|g
operator|.
name|getName
argument_list|()
argument_list|,
name|g
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|g
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|subUuid
range|:
name|g
operator|.
name|getSubgroups
argument_list|()
control|)
block|{
if|if
condition|(
name|byUUID
operator|.
name|get
argument_list|(
name|subUuid
argument_list|)
operator|==
literal|null
operator|&&
name|groupBackend
operator|.
name|get
argument_list|(
name|subUuid
argument_list|)
operator|==
literal|null
condition|)
block|{
name|problems
operator|.
name|add
argument_list|(
name|error
argument_list|(
literal|"group %s (%s) has nonexistent subgroup %s"
argument_list|,
name|g
operator|.
name|getName
argument_list|()
argument_list|,
name|g
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|subUuid
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Account
operator|.
name|Id
name|id
range|:
name|g
operator|.
name|getMembers
argument_list|()
operator|.
name|asList
argument_list|()
control|)
block|{
name|AccountState
name|account
decl_stmt|;
try|try
block|{
name|account
operator|=
name|accounts
operator|.
name|get
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
name|problems
operator|.
name|add
argument_list|(
name|error
argument_list|(
literal|"group %s (%s) has member %s with invalid configuration: %s"
argument_list|,
name|g
operator|.
name|getName
argument_list|()
argument_list|,
name|g
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|id
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|account
operator|==
literal|null
condition|)
block|{
name|problems
operator|.
name|add
argument_list|(
name|error
argument_list|(
literal|"group %s (%s) has nonexistent member %s"
argument_list|,
name|g
operator|.
name|getName
argument_list|()
argument_list|,
name|g
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|problems
return|;
block|}
comment|/** checkCycle walks through root's subgroups recursively, and checks for cycles. */
DECL|method|checkCycle ( InternalGroup root, Map<AccountGroup.UUID, InternalGroup> byUUID)
specifier|private
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|checkCycle
parameter_list|(
name|InternalGroup
name|root
parameter_list|,
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|InternalGroup
argument_list|>
name|byUUID
parameter_list|)
block|{
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|InternalGroup
argument_list|>
name|todo
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|InternalGroup
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|todo
operator|.
name|add
argument_list|(
name|root
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|todo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|InternalGroup
name|t
init|=
name|todo
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|todo
operator|.
name|remove
argument_list|(
name|t
argument_list|)
expr_stmt|;
if|if
condition|(
name|seen
operator|.
name|contains
argument_list|(
name|t
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|seen
operator|.
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
comment|// We don't check for owner cycles, since those are normal in self-administered groups.
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|subUuid
range|:
name|t
operator|.
name|getSubgroups
argument_list|()
control|)
block|{
name|InternalGroup
name|g
init|=
name|byUUID
operator|.
name|get
argument_list|(
name|subUuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|g
argument_list|,
name|root
argument_list|)
condition|)
block|{
name|problems
operator|.
name|add
argument_list|(
name|warning
argument_list|(
literal|"group %s (%s) contains a cycle: %s (%s) points to it as subgroup."
argument_list|,
name|root
operator|.
name|getName
argument_list|()
argument_list|,
name|root
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|t
operator|.
name|getName
argument_list|()
argument_list|,
name|t
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|todo
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|problems
return|;
block|}
block|}
end_class

end_unit

