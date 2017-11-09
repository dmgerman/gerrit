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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|common
operator|.
name|collect
operator|.
name|Iterables
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
name|Sets
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
name|Streams
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
name|GroupReference
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
name|errors
operator|.
name|NoSuchGroupException
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|AllUsersNameOnInitProvider
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitFlags
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroupMember
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
name|AccountGroupName
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
name|GerritPersonIdentProvider
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
name|AnonymousCowardNameProvider
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
name|SitePaths
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
name|MetaDataUpdate
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
name|group
operator|.
name|db
operator|.
name|GroupConfig
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
name|GroupNameNotes
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
name|Groups
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
name|InternalGroupUpdate
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
name|java
operator|.
name|io
operator|.
name|File
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
name|nio
operator|.
name|file
operator|.
name|Path
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
name|stream
operator|.
name|Stream
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
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|FileRepository
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
name|PersonIdent
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
name|lib
operator|.
name|RepositoryCache
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
name|util
operator|.
name|FS
import|;
end_import

begin_comment
comment|/**  * A database accessor for calls related to groups.  *  *<p>All calls which read or write group related details to the database<strong>during  * init</strong> (either ReviewDb or NoteDb) are gathered here. For non-init cases, use {@code  * Groups} or {@code GroupsUpdate} instead.  *  *<p>All methods of this class refer to<em>internal</em> groups.  */
end_comment

begin_class
DECL|class|GroupsOnInit
specifier|public
class|class
name|GroupsOnInit
block|{
DECL|field|flags
specifier|private
specifier|final
name|InitFlags
name|flags
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|allUsers
specifier|private
specifier|final
name|String
name|allUsers
decl_stmt|;
DECL|field|readFromNoteDb
specifier|private
specifier|final
name|boolean
name|readFromNoteDb
decl_stmt|;
DECL|field|writeGroupsToNoteDb
specifier|private
specifier|final
name|boolean
name|writeGroupsToNoteDb
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupsOnInit (InitFlags flags, SitePaths site, AllUsersNameOnInitProvider allUsers)
specifier|public
name|GroupsOnInit
parameter_list|(
name|InitFlags
name|flags
parameter_list|,
name|SitePaths
name|site
parameter_list|,
name|AllUsersNameOnInitProvider
name|allUsers
parameter_list|)
block|{
name|this
operator|.
name|flags
operator|=
name|flags
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|allUsers
operator|=
name|allUsers
operator|.
name|get
argument_list|()
expr_stmt|;
name|readFromNoteDb
operator|=
name|flags
operator|.
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"readGroupsFromNoteDb"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// TODO(aliceks): Remove this flag when all other necessary TODOs for writing groups to NoteDb
comment|// have been addressed.
comment|// Don't flip this flag in a production setting! We only added it to spread the implementation
comment|// of groups in NoteDb among several changes which are gradually merged.
name|writeGroupsToNoteDb
operator|=
name|flags
operator|.
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"writeGroupsToNoteDb"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the {@code AccountGroup} for the specified {@code GroupReference}.    *    * @param db the {@code ReviewDb} instance to use for lookups    * @param groupReference the {@code GroupReference} of the group    * @return the {@code InternalGroup} represented by the {@code GroupReference}    * @throws OrmException if the group couldn't be retrieved from ReviewDb    * @throws IOException if an error occurs while reading from NoteDb    * @throws ConfigInvalidException if the data in NoteDb is in an incorrect format    * @throws NoSuchGroupException if a group with such a name doesn't exist    */
DECL|method|getExistingGroup (ReviewDb db, GroupReference groupReference)
specifier|public
name|InternalGroup
name|getExistingGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|GroupReference
name|groupReference
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|readFromNoteDb
condition|)
block|{
return|return
name|getExistingGroupFromNoteDb
argument_list|(
name|groupReference
argument_list|)
return|;
block|}
return|return
name|getExistingGroupFromReviewDb
argument_list|(
name|db
argument_list|,
name|groupReference
argument_list|)
return|;
block|}
DECL|method|getExistingGroupFromNoteDb (GroupReference groupReference)
specifier|private
name|InternalGroup
name|getExistingGroupFromNoteDb
parameter_list|(
name|GroupReference
name|groupReference
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|NoSuchGroupException
block|{
name|File
name|allUsersRepoPath
init|=
name|getPathToAllUsersRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|allUsersRepoPath
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|Repository
name|allUsersRepo
init|=
operator|new
name|FileRepository
argument_list|(
name|allUsersRepoPath
argument_list|)
init|)
block|{
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
name|groupReference
operator|.
name|getUUID
argument_list|()
decl_stmt|;
name|GroupConfig
name|groupConfig
init|=
name|GroupConfig
operator|.
name|loadForGroup
argument_list|(
name|allUsersRepo
argument_list|,
name|groupUuid
argument_list|)
decl_stmt|;
return|return
name|groupConfig
operator|.
name|getLoadedGroup
argument_list|()
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|NoSuchGroupException
argument_list|(
name|groupReference
operator|.
name|getUUID
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupReference
operator|.
name|getUUID
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|getExistingGroupFromReviewDb ( ReviewDb db, GroupReference groupReference)
specifier|private
specifier|static
name|InternalGroup
name|getExistingGroupFromReviewDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|GroupReference
name|groupReference
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
name|String
name|groupName
init|=
name|groupReference
operator|.
name|getName
argument_list|()
decl_stmt|;
name|AccountGroupName
name|accountGroupName
init|=
name|db
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|groupName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|accountGroupName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupName
argument_list|)
throw|;
block|}
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
name|accountGroupName
operator|.
name|getId
argument_list|()
decl_stmt|;
name|AccountGroup
name|group
init|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupName
argument_list|)
throw|;
block|}
return|return
name|Groups
operator|.
name|asInternalGroup
argument_list|(
name|db
argument_list|,
name|group
argument_list|)
return|;
block|}
comment|/**    * Returns {@code GroupReference}s for all internal groups.    *    * @param db the {@code ReviewDb} instance to use for lookups    * @return a stream of the {@code GroupReference}s of all internal groups    * @throws OrmException if an error occurs while reading from ReviewDb    * @throws IOException if an error occurs while reading from NoteDb    * @throws ConfigInvalidException if the data in NoteDb is in an incorrect format    */
DECL|method|getAllGroupReferences (ReviewDb db)
specifier|public
name|Stream
argument_list|<
name|GroupReference
argument_list|>
name|getAllGroupReferences
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|readFromNoteDb
condition|)
block|{
name|File
name|allUsersRepoPath
init|=
name|getPathToAllUsersRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|allUsersRepoPath
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|Repository
name|allUsersRepo
init|=
operator|new
name|FileRepository
argument_list|(
name|allUsersRepoPath
argument_list|)
init|)
block|{
return|return
name|GroupNameNotes
operator|.
name|loadAllGroupReferences
argument_list|(
name|allUsersRepo
argument_list|)
operator|.
name|stream
argument_list|()
return|;
block|}
block|}
return|return
name|Stream
operator|.
name|empty
argument_list|()
return|;
block|}
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|all
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|group
lambda|->
operator|new
name|GroupReference
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
name|group
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Adds an account as member to a group. The account is only added as a new member if it isn't    * already a member of the group.    *    *<p><strong>Note</strong>: This method doesn't check whether the account exists! It also doesn't    * update the account index!    *    * @param db the {@code ReviewDb} instance to update    * @param groupUuid the UUID of the group    * @param account the account to add    * @throws OrmException if an error occurs while reading/writing from/to ReviewDb    * @throws NoSuchGroupException if the specified group doesn't exist    */
DECL|method|addGroupMember (ReviewDb db, AccountGroup.UUID groupUuid, Account account)
specifier|public
name|void
name|addGroupMember
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|addGroupMemberInReviewDb
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|,
name|account
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|writeGroupsToNoteDb
condition|)
block|{
return|return;
block|}
name|addGroupMemberInNoteDb
argument_list|(
name|groupUuid
argument_list|,
name|account
argument_list|)
expr_stmt|;
block|}
DECL|method|addGroupMemberInReviewDb ( ReviewDb db, AccountGroup.UUID groupUuid, Account.Id accountId)
specifier|private
specifier|static
name|void
name|addGroupMemberInReviewDb
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
name|AccountGroup
name|group
init|=
name|getExistingGroup
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
name|group
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|isMember
argument_list|(
name|db
argument_list|,
name|groupId
argument_list|,
name|accountId
argument_list|)
condition|)
block|{
return|return;
block|}
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|insert
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|AccountGroupMember
argument_list|(
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|accountId
argument_list|,
name|groupId
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getExistingGroup (ReviewDb db, AccountGroup.UUID groupUuid)
specifier|private
specifier|static
name|AccountGroup
name|getExistingGroup
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
name|List
argument_list|<
name|AccountGroup
argument_list|>
name|accountGroups
init|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|byUUID
argument_list|(
name|groupUuid
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
name|accountGroups
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|accountGroups
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|accountGroups
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupUuid
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|OrmDuplicateKeyException
argument_list|(
literal|"Duplicate group UUID "
operator|+
name|groupUuid
argument_list|)
throw|;
block|}
block|}
DECL|method|isMember (ReviewDb db, AccountGroup.Id groupId, Account.Id accountId)
specifier|private
specifier|static
name|boolean
name|isMember
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroupMember
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|accountId
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
return|return
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|addGroupMemberInNoteDb (AccountGroup.UUID groupUuid, Account account)
specifier|private
name|void
name|addGroupMemberInNoteDb
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|NoSuchGroupException
block|{
name|File
name|allUsersRepoPath
init|=
name|getPathToAllUsersRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|allUsersRepoPath
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|Repository
name|repository
init|=
operator|new
name|FileRepository
argument_list|(
name|allUsersRepoPath
argument_list|)
init|)
block|{
name|addGroupMemberInNoteDb
argument_list|(
name|repository
argument_list|,
name|groupUuid
argument_list|,
name|account
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addGroupMemberInNoteDb ( Repository repository, AccountGroup.UUID groupUuid, Account account)
specifier|private
name|void
name|addGroupMemberInNoteDb
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Account
name|account
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|NoSuchGroupException
block|{
name|GroupConfig
name|groupConfig
init|=
name|GroupConfig
operator|.
name|loadForGroup
argument_list|(
name|repository
argument_list|,
name|groupUuid
argument_list|)
decl_stmt|;
name|InternalGroup
name|group
init|=
name|groupConfig
operator|.
name|getLoadedGroup
argument_list|()
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|NoSuchGroupException
argument_list|(
name|groupUuid
argument_list|)
argument_list|)
decl_stmt|;
name|InternalGroupUpdate
name|groupUpdate
init|=
name|getMemberAdditionUpdate
argument_list|(
name|account
argument_list|)
decl_stmt|;
name|groupConfig
operator|.
name|setGroupUpdate
argument_list|(
name|groupUpdate
argument_list|,
name|accountId
lambda|->
name|getAccountNameEmail
argument_list|(
name|account
argument_list|,
name|accountId
argument_list|)
argument_list|,
name|AccountGroup
operator|.
name|UUID
operator|::
name|get
argument_list|)
expr_stmt|;
name|commit
argument_list|(
name|repository
argument_list|,
name|groupConfig
argument_list|,
name|group
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Nullable
DECL|method|getPathToAllUsersRepository ()
specifier|private
name|File
name|getPathToAllUsersRepository
parameter_list|()
block|{
name|Path
name|basePath
init|=
name|site
operator|.
name|resolve
argument_list|(
name|flags
operator|.
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"basePath"
argument_list|)
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|basePath
operator|!=
literal|null
argument_list|,
literal|"gerrit.basePath must be configured"
argument_list|)
expr_stmt|;
return|return
name|RepositoryCache
operator|.
name|FileKey
operator|.
name|resolve
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
name|allUsers
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
return|;
block|}
DECL|method|getMemberAdditionUpdate (Account account)
specifier|private
specifier|static
name|InternalGroupUpdate
name|getMemberAdditionUpdate
parameter_list|(
name|Account
name|account
parameter_list|)
block|{
return|return
name|InternalGroupUpdate
operator|.
name|builder
argument_list|()
operator|.
name|setMemberModification
argument_list|(
name|members
lambda|->
name|Sets
operator|.
name|union
argument_list|(
name|members
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|getAccountNameEmail (Account knownAccount, Account.Id someAccountId)
specifier|private
name|String
name|getAccountNameEmail
parameter_list|(
name|Account
name|knownAccount
parameter_list|,
name|Account
operator|.
name|Id
name|someAccountId
parameter_list|)
block|{
if|if
condition|(
name|knownAccount
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|someAccountId
argument_list|)
condition|)
block|{
name|String
name|anonymousCowardName
init|=
operator|new
name|AnonymousCowardNameProvider
argument_list|(
name|flags
operator|.
name|cfg
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|knownAccount
operator|.
name|getNameEmail
argument_list|(
name|anonymousCowardName
argument_list|)
return|;
block|}
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|someAccountId
argument_list|)
return|;
block|}
DECL|method|commit (Repository repository, GroupConfig groupConfig, Timestamp groupCreatedOn)
specifier|private
name|void
name|commit
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|GroupConfig
name|groupConfig
parameter_list|,
name|Timestamp
name|groupCreatedOn
parameter_list|)
throws|throws
name|IOException
block|{
name|PersonIdent
name|personIdent
init|=
operator|new
name|PersonIdent
argument_list|(
operator|new
name|GerritPersonIdentProvider
argument_list|(
name|flags
operator|.
name|cfg
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
name|groupCreatedOn
argument_list|)
decl_stmt|;
try|try
init|(
name|MetaDataUpdate
name|metaDataUpdate
init|=
name|createMetaDataUpdate
argument_list|(
name|repository
argument_list|,
name|personIdent
argument_list|)
init|)
block|{
name|groupConfig
operator|.
name|commit
argument_list|(
name|metaDataUpdate
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createMetaDataUpdate (Repository repository, PersonIdent personIdent)
specifier|private
name|MetaDataUpdate
name|createMetaDataUpdate
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|PersonIdent
name|personIdent
parameter_list|)
block|{
name|MetaDataUpdate
name|metaDataUpdate
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|allUsers
argument_list|)
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|metaDataUpdate
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|personIdent
argument_list|)
expr_stmt|;
name|metaDataUpdate
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|personIdent
argument_list|)
expr_stmt|;
return|return
name|metaDataUpdate
return|;
block|}
block|}
end_class

end_unit

