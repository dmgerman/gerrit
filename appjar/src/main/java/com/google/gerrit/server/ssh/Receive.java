begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|spearce
operator|.
name|jgit
operator|.
name|transport
operator|.
name|PreReceiveHook
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|transport
operator|.
name|ReceivePack
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/** Receives change upload over SSH using the Git receive-pack protocol. */
end_comment

begin_class
DECL|class|Receive
class|class
name|Receive
extends|extends
name|AbstractGitCommand
block|{
DECL|field|NEW_CHANGE
specifier|private
specifier|static
specifier|final
name|String
name|NEW_CHANGE
init|=
literal|"refs/changes/new"
decl_stmt|;
DECL|field|NEW_PATCHSET
specifier|private
specifier|static
specifier|final
name|Pattern
name|NEW_PATCHSET
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^refs/changes/(?:[0-9][0-9]/)?([1-9][0-9]*)/new$"
argument_list|)
decl_stmt|;
DECL|field|reviewerEmail
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|reviewerEmail
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ccEmail
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|ccEmail
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|reviewerId
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewerId
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|ccId
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ccId
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|newChange
specifier|private
name|ReceiveCommand
name|newChange
decl_stmt|;
DECL|field|addByChange
specifier|private
specifier|final
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ReceiveCommand
argument_list|>
name|addByChange
init|=
operator|new
name|HashMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ReceiveCommand
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|addByCommit
specifier|private
specifier|final
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Change
argument_list|>
name|addByCommit
init|=
operator|new
name|HashMap
argument_list|<
name|ObjectId
argument_list|,
name|Change
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|changeCache
specifier|private
specifier|final
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Change
argument_list|>
name|changeCache
init|=
operator|new
name|HashMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Change
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|runImpl ()
specifier|protected
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
block|{
name|lookup
argument_list|(
name|db
argument_list|,
name|reviewerId
argument_list|,
literal|"reviewer"
argument_list|,
name|reviewerEmail
argument_list|)
expr_stmt|;
name|lookup
argument_list|(
name|db
argument_list|,
name|ccId
argument_list|,
literal|"cc"
argument_list|,
name|ccEmail
argument_list|)
expr_stmt|;
comment|// TODO verify user has signed a CLA for this project
specifier|final
name|ReceivePack
name|rp
init|=
operator|new
name|ReceivePack
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|rp
operator|.
name|setAllowCreates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setAllowDeletes
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setAllowNonFastForwards
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setCheckReceivedObjects
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setPreReceiveHook
argument_list|(
operator|new
name|PreReceiveHook
argument_list|()
block|{
specifier|public
name|void
name|onPreReceive
parameter_list|(
specifier|final
name|ReceivePack
name|arg0
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|ReceiveCommand
argument_list|>
name|commands
parameter_list|)
block|{
name|parseCommands
argument_list|(
name|db
argument_list|,
name|commands
argument_list|)
expr_stmt|;
if|if
condition|(
name|newChange
operator|!=
literal|null
condition|)
block|{
comment|// TODO create new change records
name|newChange
operator|.
name|setResult
argument_list|(
name|ReceiveCommand
operator|.
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ReceiveCommand
argument_list|>
name|e
range|:
name|addByChange
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// TODO Append new commits to existing changes
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|setResult
argument_list|(
name|ReceiveCommand
operator|.
name|Result
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|rp
operator|.
name|receive
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
DECL|method|lookup (final ReviewDb db, final Set<Account.Id> accountIds, final String addressType, final Set<String> emails)
specifier|private
name|void
name|lookup
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
parameter_list|,
specifier|final
name|String
name|addressType
parameter_list|,
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|emails
parameter_list|)
throws|throws
name|Failure
block|{
specifier|final
name|StringBuilder
name|errors
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
specifier|final
name|String
name|email
range|:
name|emails
control|)
block|{
specifier|final
name|List
argument_list|<
name|Account
argument_list|>
name|who
init|=
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|byPreferredEmail
argument_list|(
name|email
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
name|who
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|accountIds
operator|.
name|add
argument_list|(
name|who
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|who
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|errors
operator|.
name|append
argument_list|(
literal|"fatal: "
operator|+
name|addressType
operator|+
literal|" "
operator|+
name|email
operator|+
literal|" is not registered on Gerrit\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|errors
operator|.
name|append
argument_list|(
literal|"fatal: "
operator|+
name|addressType
operator|+
literal|" "
operator|+
name|email
operator|+
literal|" matches more than one account on Gerrit\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: cannot lookup reviewers, database is down"
argument_list|)
throw|;
block|}
if|if
condition|(
name|errors
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
name|errors
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|parseCommandLine (final String[] args)
specifier|protected
name|String
name|parseCommandLine
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Failure
block|{
name|int
name|argi
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|isGerrit
argument_list|()
condition|)
block|{
for|for
control|(
init|;
name|argi
operator|<
name|args
operator|.
name|length
operator|-
literal|1
condition|;
name|argi
operator|++
control|)
block|{
specifier|final
name|int
name|eq
init|=
name|args
index|[
name|argi
index|]
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
specifier|final
name|String
name|opt
decl_stmt|,
name|val
decl_stmt|;
if|if
condition|(
name|eq
operator|<
literal|0
condition|)
block|{
name|opt
operator|=
name|args
index|[
name|argi
index|]
expr_stmt|;
name|val
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|opt
operator|=
name|args
index|[
name|argi
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|eq
argument_list|)
expr_stmt|;
name|val
operator|=
name|args
index|[
name|argi
index|]
operator|.
name|substring
argument_list|(
name|eq
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|opt
operator|.
name|equals
argument_list|(
literal|"--reviewer"
argument_list|)
condition|)
block|{
name|reviewerEmail
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|opt
operator|.
name|equals
argument_list|(
literal|"--cc"
argument_list|)
condition|)
block|{
name|ccEmail
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
continue|continue;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|argi
operator|!=
name|args
operator|.
name|length
operator|-
literal|1
condition|)
block|{
throw|throw
name|usage
argument_list|()
throw|;
block|}
return|return
name|args
index|[
name|argi
index|]
return|;
block|}
DECL|method|usage ()
specifier|private
name|Failure
name|usage
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|m
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|"usage: "
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isGerrit
argument_list|()
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|" [--reviewer=email]*"
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|" [--cc=email]*"
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|append
argument_list|(
literal|" '/project.git'"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|parseCommands (final ReviewDb db, final Collection<ReceiveCommand> commands)
specifier|private
name|void
name|parseCommands
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|ReceiveCommand
argument_list|>
name|commands
parameter_list|)
block|{
for|for
control|(
specifier|final
name|ReceiveCommand
name|cmd
range|:
name|commands
control|)
block|{
if|if
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
operator|!=
name|ReceiveCommand
operator|.
name|Result
operator|.
name|NOT_ATTEMPTED
condition|)
block|{
comment|// Already rejected by the core receive process.
comment|//
continue|continue;
block|}
if|if
condition|(
name|cmd
operator|.
name|getType
argument_list|()
operator|!=
name|ReceiveCommand
operator|.
name|Type
operator|.
name|CREATE
condition|)
block|{
comment|// We only permit creates for refs which don't exist.
comment|//
name|reject
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|NEW_CHANGE
operator|.
name|equals
argument_list|(
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|)
condition|)
block|{
comment|// Permit exactly one new change request per push.
comment|//
if|if
condition|(
name|newChange
operator|!=
literal|null
condition|)
block|{
name|reject
argument_list|(
name|cmd
argument_list|,
literal|"duplicate request"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|newChange
operator|=
name|cmd
expr_stmt|;
continue|continue;
block|}
specifier|final
name|Matcher
name|m
init|=
name|NEW_PATCHSET
operator|.
name|matcher
argument_list|(
name|cmd
operator|.
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
comment|// The referenced change must exist and must still be open.
comment|//
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|Change
operator|.
name|Id
operator|.
name|fromString
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Change
name|changeEnt
decl_stmt|;
try|try
block|{
name|changeEnt
operator|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|reject
argument_list|(
name|cmd
argument_list|,
literal|"database error"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|changeEnt
operator|==
literal|null
condition|)
block|{
name|reject
argument_list|(
name|cmd
argument_list|,
literal|"change "
operator|+
name|changeId
operator|.
name|get
argument_list|()
operator|+
literal|" not found"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|changeEnt
operator|.
name|getStatus
argument_list|()
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|reject
argument_list|(
name|cmd
argument_list|,
literal|"change "
operator|+
name|changeId
operator|.
name|get
argument_list|()
operator|+
literal|" closed"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|addByChange
operator|.
name|containsKey
argument_list|(
name|changeId
argument_list|)
condition|)
block|{
name|reject
argument_list|(
name|cmd
argument_list|,
literal|"duplicate request"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|addByCommit
operator|.
name|containsKey
argument_list|(
name|cmd
operator|.
name|getNewId
argument_list|()
argument_list|)
condition|)
block|{
name|reject
argument_list|(
name|cmd
argument_list|,
literal|"duplicate request"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|addByChange
operator|.
name|put
argument_list|(
name|changeId
argument_list|,
name|cmd
argument_list|)
expr_stmt|;
name|addByCommit
operator|.
name|put
argument_list|(
name|cmd
operator|.
name|getNewId
argument_list|()
argument_list|,
name|changeEnt
argument_list|)
expr_stmt|;
name|changeCache
operator|.
name|put
argument_list|(
name|changeId
argument_list|,
name|changeEnt
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// Everything else is bogus as far as we are concerned.
comment|//
name|reject
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|reject (final ReceiveCommand cmd)
specifier|private
specifier|static
name|void
name|reject
parameter_list|(
specifier|final
name|ReceiveCommand
name|cmd
parameter_list|)
block|{
name|reject
argument_list|(
name|cmd
argument_list|,
literal|"prohibited by Gerrit"
argument_list|)
expr_stmt|;
block|}
DECL|method|reject (final ReceiveCommand cmd, final String why)
specifier|private
specifier|static
name|void
name|reject
parameter_list|(
specifier|final
name|ReceiveCommand
name|cmd
parameter_list|,
specifier|final
name|String
name|why
parameter_list|)
block|{
name|cmd
operator|.
name|setResult
argument_list|(
name|ReceiveCommand
operator|.
name|Result
operator|.
name|REJECTED_OTHER_REASON
argument_list|,
name|why
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

