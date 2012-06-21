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
name|base
operator|.
name|Strings
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
name|Lists
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
name|data
operator|.
name|ApprovalType
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
name|ApprovalTypes
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
name|SubmitRecord
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
name|reviewdb
operator|.
name|client
operator|.
name|ChangeMessage
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
name|client
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
name|OutputFormat
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
name|events
operator|.
name|AccountAttribute
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
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|NoSuchChangeException
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
name|query
operator|.
name|QueryParseException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|io
operator|.
name|Writer
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
name|Comparator
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

begin_class
DECL|class|ListChanges
specifier|public
class|class
name|ListChanges
block|{
DECL|field|imp
specifier|private
specifier|final
name|QueryProcessor
name|imp
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
DECL|field|approvalTypes
specifier|private
specifier|final
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|reverse
specifier|private
name|boolean
name|reverse
decl_stmt|;
DECL|field|accounts
specifier|private
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountAttribute
argument_list|>
name|accounts
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--format"
argument_list|,
name|metaVar
operator|=
literal|"FMT"
argument_list|,
name|usage
operator|=
literal|"Output display format"
argument_list|)
DECL|field|format
specifier|private
name|OutputFormat
name|format
init|=
name|OutputFormat
operator|.
name|TEXT
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--query"
argument_list|,
name|aliases
operator|=
block|{
literal|"-q"
block|}
argument_list|,
name|metaVar
operator|=
literal|"QUERY"
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|usage
operator|=
literal|"Query string"
argument_list|)
DECL|field|queries
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|queries
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--limit"
argument_list|,
name|aliases
operator|=
block|{
literal|"-n"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"Maximum number of results to return"
argument_list|)
DECL|method|setLimit (int limit)
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|imp
operator|.
name|setLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-P"
argument_list|,
name|metaVar
operator|=
literal|"SORTKEY"
argument_list|,
name|usage
operator|=
literal|"Previous changes before SORTKEY"
argument_list|)
DECL|method|setSortKeyAfter (String key)
name|void
name|setSortKeyAfter
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// Querying for the prior page of changes requires sortkey_after predicate.
comment|// Changes are shown most recent->least recent. The previous page of
comment|// results contains changes that were updated after the given key.
name|imp
operator|.
name|setSortkeyAfter
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|reverse
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-N"
argument_list|,
name|metaVar
operator|=
literal|"SORTKEY"
argument_list|,
name|usage
operator|=
literal|"Next changes after SORTKEY"
argument_list|)
DECL|method|setSortKeyBefore (String key)
name|void
name|setSortKeyBefore
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// Querying for the next page of changes requires sortkey_before predicate.
comment|// Changes are shown most recent->least recent. The next page contains
comment|// changes that were updated before the given key.
name|imp
operator|.
name|setSortkeyBefore
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|ListChanges (QueryProcessor qp, Provider<ReviewDb> db, ApprovalTypes at, CurrentUser u, ChangeControl.Factory cf)
name|ListChanges
parameter_list|(
name|QueryProcessor
name|qp
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|ApprovalTypes
name|at
parameter_list|,
name|CurrentUser
name|u
parameter_list|,
name|ChangeControl
operator|.
name|Factory
name|cf
parameter_list|)
block|{
name|this
operator|.
name|imp
operator|=
name|qp
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|approvalTypes
operator|=
name|at
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|u
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|cf
expr_stmt|;
name|accounts
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|OutputFormat
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
DECL|method|setFormat (OutputFormat fmt)
specifier|public
name|ListChanges
name|setFormat
parameter_list|(
name|OutputFormat
name|fmt
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|fmt
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|query (Writer out)
specifier|public
name|void
name|query
parameter_list|(
name|Writer
name|out
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
throws|,
name|IOException
block|{
if|if
condition|(
name|imp
operator|.
name|isDisabled
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"query disabled"
argument_list|)
throw|;
block|}
if|if
condition|(
name|queries
operator|==
literal|null
operator|||
name|queries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|queries
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"status:open"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|queries
operator|.
name|size
argument_list|()
operator|>
literal|10
condition|)
block|{
comment|// Hard-code a default maximum number of queries to prevent
comment|// users from submitting too much to the server in a single call.
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"limit of 10 queries"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
name|res
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|queries
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|query
range|:
name|queries
control|)
block|{
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changes
init|=
name|imp
operator|.
name|queryChanges
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|boolean
name|moreChanges
init|=
name|imp
operator|.
name|getLimit
argument_list|()
operator|>
literal|0
operator|&&
name|changes
operator|.
name|size
argument_list|()
operator|>
name|imp
operator|.
name|getLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|moreChanges
condition|)
block|{
if|if
condition|(
name|reverse
condition|)
block|{
name|changes
operator|=
name|changes
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|changes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|changes
operator|=
name|changes
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|imp
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ChangeData
operator|.
name|ensureChangeLoaded
argument_list|(
name|db
argument_list|,
name|changes
argument_list|)
expr_stmt|;
name|ChangeData
operator|.
name|ensureCurrentPatchSetLoaded
argument_list|(
name|db
argument_list|,
name|changes
argument_list|)
expr_stmt|;
name|ChangeData
operator|.
name|ensureCurrentApprovalsLoaded
argument_list|(
name|db
argument_list|,
name|changes
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|info
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|changes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|changes
control|)
block|{
name|info
operator|.
name|add
argument_list|(
name|toChangeInfo
argument_list|(
name|cd
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|moreChanges
operator|&&
operator|!
name|info
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|reverse
condition|)
block|{
name|info
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_moreChanges
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|info
operator|.
name|get
argument_list|(
name|info
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|_moreChanges
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|res
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|accounts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Account
name|account
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|accounts
argument_list|()
operator|.
name|get
argument_list|(
name|accounts
operator|.
name|keySet
argument_list|()
argument_list|)
control|)
block|{
name|AccountAttribute
name|a
init|=
name|accounts
operator|.
name|get
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|a
operator|.
name|name
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|account
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|format
operator|.
name|isJson
argument_list|()
condition|)
block|{
name|format
operator|.
name|newGson
argument_list|()
operator|.
name|toJson
argument_list|(
name|res
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
name|res
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
name|res
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|firstQuery
init|=
literal|true
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|info
range|:
name|res
control|)
block|{
if|if
condition|(
name|firstQuery
condition|)
block|{
name|firstQuery
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ChangeInfo
name|c
range|:
name|info
control|)
block|{
name|String
name|id
init|=
operator|new
name|Change
operator|.
name|Key
argument_list|(
name|c
operator|.
name|id
argument_list|)
operator|.
name|abbreviate
argument_list|()
decl_stmt|;
name|String
name|subject
init|=
name|c
operator|.
name|subject
decl_stmt|;
if|if
condition|(
name|subject
operator|.
name|length
argument_list|()
operator|+
name|id
operator|.
name|length
argument_list|()
operator|>
literal|80
condition|)
block|{
name|subject
operator|=
name|subject
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|80
operator|-
name|id
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|subject
operator|.
name|replace
argument_list|(
literal|'\n'
argument_list|,
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|toChangeInfo (ChangeData cd)
specifier|private
name|ChangeInfo
name|toChangeInfo
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
name|ChangeInfo
name|out
init|=
operator|new
name|ChangeInfo
argument_list|()
decl_stmt|;
name|Change
name|in
init|=
name|cd
operator|.
name|change
argument_list|(
name|db
argument_list|)
decl_stmt|;
name|out
operator|.
name|project
operator|=
name|in
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|out
operator|.
name|branch
operator|=
name|in
operator|.
name|getDest
argument_list|()
operator|.
name|getShortName
argument_list|()
expr_stmt|;
name|out
operator|.
name|topic
operator|=
name|in
operator|.
name|getTopic
argument_list|()
expr_stmt|;
name|out
operator|.
name|id
operator|=
name|in
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|out
operator|.
name|subject
operator|=
name|in
operator|.
name|getSubject
argument_list|()
expr_stmt|;
name|out
operator|.
name|status
operator|=
name|in
operator|.
name|getStatus
argument_list|()
expr_stmt|;
name|out
operator|.
name|owner
operator|=
name|asAccountAttribute
argument_list|(
name|in
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|created
operator|=
name|in
operator|.
name|getCreatedOn
argument_list|()
expr_stmt|;
name|out
operator|.
name|updated
operator|=
name|in
operator|.
name|getLastUpdatedOn
argument_list|()
expr_stmt|;
name|out
operator|.
name|_number
operator|=
name|in
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|out
operator|.
name|_sortkey
operator|=
name|in
operator|.
name|getSortKey
argument_list|()
expr_stmt|;
name|out
operator|.
name|starred
operator|=
name|user
operator|.
name|getStarredChanges
argument_list|()
operator|.
name|contains
argument_list|(
name|in
operator|.
name|getId
argument_list|()
argument_list|)
condition|?
literal|true
else|:
literal|null
expr_stmt|;
name|out
operator|.
name|reviewed
operator|=
name|in
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
operator|&&
name|isChangeReviewed
argument_list|(
name|cd
argument_list|)
condition|?
literal|true
else|:
literal|null
expr_stmt|;
name|out
operator|.
name|labels
operator|=
name|labelsFor
argument_list|(
name|cd
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
DECL|method|asAccountAttribute (Account.Id user)
specifier|private
name|AccountAttribute
name|asAccountAttribute
parameter_list|(
name|Account
operator|.
name|Id
name|user
parameter_list|)
block|{
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AccountAttribute
name|a
init|=
name|accounts
operator|.
name|get
argument_list|(
name|user
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
name|a
operator|=
operator|new
name|AccountAttribute
argument_list|()
expr_stmt|;
name|accounts
operator|.
name|put
argument_list|(
name|user
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
return|return
name|a
return|;
block|}
DECL|method|labelsFor (ChangeData cd)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|LabelInfo
argument_list|>
name|labelsFor
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
name|Change
name|in
init|=
name|cd
operator|.
name|change
argument_list|(
name|db
argument_list|)
decl_stmt|;
name|ChangeControl
name|ctl
init|=
name|cd
operator|.
name|changeControl
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctl
operator|==
literal|null
operator|||
name|ctl
operator|.
name|getCurrentUser
argument_list|()
operator|!=
name|user
condition|)
block|{
try|try
block|{
name|ctl
operator|=
name|changeControlFactory
operator|.
name|controlFor
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
name|PatchSet
name|ps
init|=
name|cd
operator|.
name|currentPatchSet
argument_list|(
name|db
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|LabelInfo
argument_list|>
name|labels
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|SubmitRecord
name|rec
range|:
name|ctl
operator|.
name|canSubmit
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|ps
argument_list|,
name|cd
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
control|)
block|{
if|if
condition|(
name|rec
operator|.
name|labels
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|SubmitRecord
operator|.
name|Label
name|r
range|:
name|rec
operator|.
name|labels
control|)
block|{
name|LabelInfo
name|p
init|=
name|labels
operator|.
name|get
argument_list|(
name|r
operator|.
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|_status
operator|.
name|compareTo
argument_list|(
name|r
operator|.
name|status
argument_list|)
operator|<
literal|0
condition|)
block|{
name|LabelInfo
name|n
init|=
operator|new
name|LabelInfo
argument_list|()
decl_stmt|;
name|n
operator|.
name|_status
operator|=
name|r
operator|.
name|status
expr_stmt|;
switch|switch
condition|(
name|r
operator|.
name|status
condition|)
block|{
case|case
name|OK
case|:
name|n
operator|.
name|approved
operator|=
name|asAccountAttribute
argument_list|(
name|r
operator|.
name|appliedBy
argument_list|)
expr_stmt|;
break|break;
case|case
name|REJECT
case|:
name|n
operator|.
name|rejected
operator|=
name|asAccountAttribute
argument_list|(
name|r
operator|.
name|appliedBy
argument_list|)
expr_stmt|;
break|break;
block|}
name|n
operator|.
name|optional
operator|=
name|n
operator|.
name|_status
operator|==
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|MAY
condition|?
literal|true
else|:
literal|null
expr_stmt|;
name|labels
operator|.
name|put
argument_list|(
name|r
operator|.
name|label
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Collection
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|LabelInfo
argument_list|>
name|e
range|:
name|labels
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|approved
operator|!=
literal|null
operator|||
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|rejected
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|ApprovalType
name|type
init|=
name|approvalTypes
operator|.
name|byLabel
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|||
name|type
operator|.
name|getMin
argument_list|()
operator|==
literal|null
operator|||
name|type
operator|.
name|getMax
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// Unknown or misconfigured type can't have intermediate scores.
continue|continue;
block|}
name|short
name|min
init|=
name|type
operator|.
name|getMin
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|short
name|max
init|=
name|type
operator|.
name|getMax
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|-
literal|1
operator|<=
name|min
operator|&&
name|max
operator|<=
literal|1
condition|)
block|{
comment|// Types with a range of -1..+1 can't have intermediate scores.
continue|continue;
block|}
if|if
condition|(
name|approvals
operator|==
literal|null
condition|)
block|{
name|approvals
operator|=
name|cd
operator|.
name|currentApprovals
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|approvals
control|)
block|{
name|short
name|val
init|=
name|psa
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|0
operator|&&
name|min
operator|<
name|val
operator|&&
name|val
operator|<
name|max
operator|&&
name|psa
operator|.
name|getCategoryId
argument_list|()
operator|.
name|equals
argument_list|(
name|type
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
literal|0
operator|<
name|val
condition|)
block|{
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|recommended
operator|=
name|asAccountAttribute
argument_list|(
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|value
operator|=
name|val
operator|!=
literal|1
condition|?
name|val
else|:
literal|null
expr_stmt|;
block|}
else|else
block|{
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|disliked
operator|=
name|asAccountAttribute
argument_list|(
name|psa
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|value
operator|=
name|val
operator|!=
operator|-
literal|1
condition|?
name|val
else|:
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|labels
return|;
block|}
DECL|method|isChangeReviewed (ChangeData cd)
specifier|private
name|boolean
name|isChangeReviewed
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|user
operator|instanceof
name|IdentifiedUser
condition|)
block|{
name|PatchSet
name|currentPatchSet
init|=
name|cd
operator|.
name|currentPatchSet
argument_list|(
name|db
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentPatchSet
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|messages
init|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|changeMessages
argument_list|()
operator|.
name|byPatchSet
argument_list|(
name|currentPatchSet
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
name|messages
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Sort messages to let the most recent ones at the beginning.
name|Collections
operator|.
name|sort
argument_list|(
name|messages
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ChangeMessage
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|ChangeMessage
name|a
parameter_list|,
name|ChangeMessage
name|b
parameter_list|)
block|{
return|return
name|b
operator|.
name|getWrittenOn
argument_list|()
operator|.
name|compareTo
argument_list|(
name|a
operator|.
name|getWrittenOn
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Account
operator|.
name|Id
name|currentUserId
init|=
operator|(
operator|(
name|IdentifiedUser
operator|)
name|user
operator|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|Account
operator|.
name|Id
name|changeOwnerId
init|=
name|cd
operator|.
name|change
argument_list|(
name|db
argument_list|)
operator|.
name|getOwner
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeMessage
name|cm
range|:
name|messages
control|)
block|{
if|if
condition|(
name|currentUserId
operator|.
name|equals
argument_list|(
name|cm
operator|.
name|getAuthor
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|changeOwnerId
operator|.
name|equals
argument_list|(
name|cm
operator|.
name|getAuthor
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|class|ChangeInfo
specifier|static
class|class
name|ChangeInfo
block|{
DECL|field|project
name|String
name|project
decl_stmt|;
DECL|field|branch
name|String
name|branch
decl_stmt|;
DECL|field|topic
name|String
name|topic
decl_stmt|;
DECL|field|id
name|String
name|id
decl_stmt|;
DECL|field|subject
name|String
name|subject
decl_stmt|;
DECL|field|status
name|Change
operator|.
name|Status
name|status
decl_stmt|;
DECL|field|created
name|Timestamp
name|created
decl_stmt|;
DECL|field|updated
name|Timestamp
name|updated
decl_stmt|;
DECL|field|starred
name|Boolean
name|starred
decl_stmt|;
DECL|field|reviewed
name|Boolean
name|reviewed
decl_stmt|;
DECL|field|_sortkey
name|String
name|_sortkey
decl_stmt|;
DECL|field|_number
name|int
name|_number
decl_stmt|;
DECL|field|owner
name|AccountAttribute
name|owner
decl_stmt|;
DECL|field|labels
name|Map
argument_list|<
name|String
argument_list|,
name|LabelInfo
argument_list|>
name|labels
decl_stmt|;
DECL|field|_moreChanges
name|Boolean
name|_moreChanges
decl_stmt|;
block|}
DECL|class|LabelInfo
specifier|static
class|class
name|LabelInfo
block|{
DECL|field|_status
specifier|transient
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
name|_status
decl_stmt|;
DECL|field|approved
name|AccountAttribute
name|approved
decl_stmt|;
DECL|field|rejected
name|AccountAttribute
name|rejected
decl_stmt|;
DECL|field|recommended
name|AccountAttribute
name|recommended
decl_stmt|;
DECL|field|disliked
name|AccountAttribute
name|disliked
decl_stmt|;
DECL|field|value
name|Short
name|value
decl_stmt|;
DECL|field|optional
name|Boolean
name|optional
decl_stmt|;
block|}
block|}
end_class

end_unit

