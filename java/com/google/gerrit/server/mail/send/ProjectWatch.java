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
DECL|package|com.google.gerrit.server.mail.send
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|send
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
name|data
operator|.
name|GroupDescription
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
name|entities
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
name|entities
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
name|entities
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|QueryParseException
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
name|mail
operator|.
name|Address
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
name|ProjectWatches
operator|.
name|NotifyType
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
name|ProjectWatches
operator|.
name|ProjectWatchKey
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
name|NotifyConfig
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|change
operator|.
name|ChangeQueryBuilder
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
name|change
operator|.
name|SingleGroupUser
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

begin_class
DECL|class|ProjectWatch
specifier|public
class|class
name|ProjectWatch
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
DECL|field|args
specifier|protected
specifier|final
name|EmailArguments
name|args
decl_stmt|;
DECL|field|projectState
specifier|protected
specifier|final
name|ProjectState
name|projectState
decl_stmt|;
DECL|field|project
specifier|protected
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|changeData
specifier|protected
specifier|final
name|ChangeData
name|changeData
decl_stmt|;
DECL|method|ProjectWatch ( EmailArguments args, Project.NameKey project, ProjectState projectState, ChangeData changeData)
specifier|public
name|ProjectWatch
parameter_list|(
name|EmailArguments
name|args
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ProjectState
name|projectState
parameter_list|,
name|ChangeData
name|changeData
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|projectState
operator|=
name|projectState
expr_stmt|;
name|this
operator|.
name|changeData
operator|=
name|changeData
expr_stmt|;
block|}
comment|/** Returns all watchers that are relevant */
DECL|method|getWatchers (NotifyType type, boolean includeWatchersFromNotifyConfig)
specifier|public
specifier|final
name|Watchers
name|getWatchers
parameter_list|(
name|NotifyType
name|type
parameter_list|,
name|boolean
name|includeWatchersFromNotifyConfig
parameter_list|)
block|{
name|Watchers
name|matching
init|=
operator|new
name|Watchers
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|projectWatchers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountState
name|a
range|:
name|args
operator|.
name|accountQueryProvider
operator|.
name|get
argument_list|()
operator|.
name|byWatchedProject
argument_list|(
name|project
argument_list|)
control|)
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
name|a
operator|.
name|account
argument_list|()
operator|.
name|id
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ProjectWatchKey
argument_list|,
name|ImmutableSet
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|e
range|:
name|a
operator|.
name|projectWatches
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|project
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|project
argument_list|()
argument_list|)
operator|&&
name|add
argument_list|(
name|matching
argument_list|,
name|accountId
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|type
argument_list|)
condition|)
block|{
comment|// We only want to prevent matching All-Projects if this filter hits
name|projectWatchers
operator|.
name|add
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|AccountState
name|a
range|:
name|args
operator|.
name|accountQueryProvider
operator|.
name|get
argument_list|()
operator|.
name|byWatchedProject
argument_list|(
name|args
operator|.
name|allProjectsName
argument_list|)
control|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ProjectWatchKey
argument_list|,
name|ImmutableSet
argument_list|<
name|NotifyType
argument_list|>
argument_list|>
name|e
range|:
name|a
operator|.
name|projectWatches
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|args
operator|.
name|allProjectsName
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|project
argument_list|()
argument_list|)
condition|)
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
name|a
operator|.
name|account
argument_list|()
operator|.
name|id
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|projectWatchers
operator|.
name|contains
argument_list|(
name|accountId
argument_list|)
condition|)
block|{
name|add
argument_list|(
name|matching
argument_list|,
name|accountId
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|includeWatchersFromNotifyConfig
condition|)
block|{
return|return
name|matching
return|;
block|}
for|for
control|(
name|ProjectState
name|state
range|:
name|projectState
operator|.
name|tree
argument_list|()
control|)
block|{
for|for
control|(
name|NotifyConfig
name|nc
range|:
name|state
operator|.
name|getConfig
argument_list|()
operator|.
name|getNotifyConfigs
argument_list|()
control|)
block|{
if|if
condition|(
name|nc
operator|.
name|isNotify
argument_list|(
name|type
argument_list|)
condition|)
block|{
try|try
block|{
name|add
argument_list|(
name|matching
argument_list|,
name|state
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|nc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Project %s has invalid notify %s filter \"%s\": %s"
argument_list|,
name|state
operator|.
name|getName
argument_list|()
argument_list|,
name|nc
operator|.
name|getName
argument_list|()
argument_list|,
name|nc
operator|.
name|getFilter
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|matching
return|;
block|}
DECL|class|Watchers
specifier|public
specifier|static
class|class
name|Watchers
block|{
DECL|class|List
specifier|static
class|class
name|List
block|{
DECL|field|accounts
specifier|protected
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accounts
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|emails
specifier|protected
specifier|final
name|Set
argument_list|<
name|Address
argument_list|>
name|emails
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|union (List... others)
specifier|private
specifier|static
name|List
name|union
parameter_list|(
name|List
modifier|...
name|others
parameter_list|)
block|{
name|List
name|union
init|=
operator|new
name|List
argument_list|()
decl_stmt|;
for|for
control|(
name|List
name|other
range|:
name|others
control|)
block|{
name|union
operator|.
name|accounts
operator|.
name|addAll
argument_list|(
name|other
operator|.
name|accounts
argument_list|)
expr_stmt|;
name|union
operator|.
name|emails
operator|.
name|addAll
argument_list|(
name|other
operator|.
name|emails
argument_list|)
expr_stmt|;
block|}
return|return
name|union
return|;
block|}
block|}
DECL|field|to
specifier|protected
specifier|final
name|List
name|to
init|=
operator|new
name|List
argument_list|()
decl_stmt|;
DECL|field|cc
specifier|protected
specifier|final
name|List
name|cc
init|=
operator|new
name|List
argument_list|()
decl_stmt|;
DECL|field|bcc
specifier|protected
specifier|final
name|List
name|bcc
init|=
operator|new
name|List
argument_list|()
decl_stmt|;
DECL|method|all ()
name|List
name|all
parameter_list|()
block|{
return|return
name|List
operator|.
name|union
argument_list|(
name|to
argument_list|,
name|cc
argument_list|,
name|bcc
argument_list|)
return|;
block|}
DECL|method|list (NotifyConfig.Header header)
name|List
name|list
parameter_list|(
name|NotifyConfig
operator|.
name|Header
name|header
parameter_list|)
block|{
switch|switch
condition|(
name|header
condition|)
block|{
case|case
name|TO
case|:
return|return
name|to
return|;
case|case
name|CC
case|:
return|return
name|cc
return|;
default|default:
case|case
name|BCC
case|:
return|return
name|bcc
return|;
block|}
block|}
block|}
DECL|method|add (Watchers matching, Project.NameKey projectName, NotifyConfig nc)
specifier|private
name|void
name|add
parameter_list|(
name|Watchers
name|matching
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|NotifyConfig
name|nc
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Checking watchers for notify config %s from project %s"
argument_list|,
name|nc
argument_list|,
name|projectName
argument_list|)
expr_stmt|;
for|for
control|(
name|GroupReference
name|groupRef
range|:
name|nc
operator|.
name|getGroups
argument_list|()
control|)
block|{
name|CurrentUser
name|user
init|=
operator|new
name|SingleGroupUser
argument_list|(
name|groupRef
operator|.
name|getUUID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|filterMatch
argument_list|(
name|user
argument_list|,
name|nc
operator|.
name|getFilter
argument_list|()
argument_list|)
condition|)
block|{
name|deliverToMembers
argument_list|(
name|matching
operator|.
name|list
argument_list|(
name|nc
operator|.
name|getHeader
argument_list|()
argument_list|)
argument_list|,
name|groupRef
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Added watchers for group %s"
argument_list|,
name|groupRef
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"The filter did not match for group %s; skip notification"
argument_list|,
name|groupRef
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|nc
operator|.
name|getAddresses
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|filterMatch
argument_list|(
literal|null
argument_list|,
name|nc
operator|.
name|getFilter
argument_list|()
argument_list|)
condition|)
block|{
name|matching
operator|.
name|list
argument_list|(
name|nc
operator|.
name|getHeader
argument_list|()
argument_list|)
operator|.
name|emails
operator|.
name|addAll
argument_list|(
name|nc
operator|.
name|getAddresses
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Added watchers for these addresses: %s"
argument_list|,
name|nc
operator|.
name|getAddresses
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"The filter did not match; skip notification for these addresses: %s"
argument_list|,
name|nc
operator|.
name|getAddresses
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|deliverToMembers (Watchers.List matching, AccountGroup.UUID startUUID)
specifier|private
name|void
name|deliverToMembers
parameter_list|(
name|Watchers
operator|.
name|List
name|matching
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|startUUID
parameter_list|)
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|q
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|seen
operator|.
name|add
argument_list|(
name|startUUID
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|startUUID
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|q
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|AccountGroup
operator|.
name|UUID
name|uuid
init|=
name|q
operator|.
name|remove
argument_list|(
name|q
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|GroupDescription
operator|.
name|Basic
name|group
init|=
name|args
operator|.
name|groupBackend
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
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
literal|"group %s not found, skip notification"
argument_list|,
name|uuid
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|group
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
condition|)
block|{
comment|// If the group has an email address, do not expand membership.
name|matching
operator|.
name|emails
operator|.
name|add
argument_list|(
operator|new
name|Address
argument_list|(
name|group
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"notify group email address %s; skip expanding to members"
argument_list|,
name|group
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
operator|(
name|group
operator|instanceof
name|GroupDescription
operator|.
name|Internal
operator|)
condition|)
block|{
comment|// Non-internal groups cannot be expanded by the server.
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"group %s is not an internal group, skip notification"
argument_list|,
name|uuid
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"adding the members of group %s as watchers"
argument_list|,
name|uuid
argument_list|)
expr_stmt|;
name|GroupDescription
operator|.
name|Internal
name|ig
init|=
operator|(
name|GroupDescription
operator|.
name|Internal
operator|)
name|group
decl_stmt|;
name|matching
operator|.
name|accounts
operator|.
name|addAll
argument_list|(
name|ig
operator|.
name|getMembers
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|m
range|:
name|ig
operator|.
name|getSubgroups
argument_list|()
control|)
block|{
if|if
condition|(
name|seen
operator|.
name|add
argument_list|(
name|m
argument_list|)
condition|)
block|{
name|q
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|add ( Watchers matching, Account.Id accountId, ProjectWatchKey key, Set<NotifyType> watchedTypes, NotifyType type)
specifier|private
name|boolean
name|add
parameter_list|(
name|Watchers
name|matching
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|ProjectWatchKey
name|key
parameter_list|,
name|Set
argument_list|<
name|NotifyType
argument_list|>
name|watchedTypes
parameter_list|,
name|NotifyType
name|type
parameter_list|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Checking project watch %s of account %s"
argument_list|,
name|key
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
name|IdentifiedUser
name|user
init|=
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|filterMatch
argument_list|(
name|user
argument_list|,
name|key
operator|.
name|filter
argument_list|()
argument_list|)
condition|)
block|{
comment|// If we are set to notify on this type, add the user.
comment|// Otherwise, still return true to stop notifications for this user.
if|if
condition|(
name|watchedTypes
operator|.
name|contains
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|matching
operator|.
name|bcc
operator|.
name|accounts
operator|.
name|add
argument_list|(
name|accountId
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Added account %s as watcher"
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"The filter did not match for account %s; skip notification"
argument_list|,
name|accountId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
comment|// Ignore broken filter expressions.
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Account %s has invalid filter in project watch %s: %s"
argument_list|,
name|accountId
argument_list|,
name|key
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|filterMatch (CurrentUser user, String filter)
specifier|private
name|boolean
name|filterMatch
parameter_list|(
name|CurrentUser
name|user
parameter_list|,
name|String
name|filter
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|ChangeQueryBuilder
name|qb
decl_stmt|;
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
name|qb
operator|=
name|args
operator|.
name|queryBuilder
operator|.
name|get
argument_list|()
operator|.
name|asUser
argument_list|(
name|args
operator|.
name|anonymousUser
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|qb
operator|=
name|args
operator|.
name|queryBuilder
operator|.
name|get
argument_list|()
operator|.
name|asUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|p
operator|=
name|qb
operator|.
name|is_visible
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|filter
operator|!=
literal|null
condition|)
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|filterPredicate
init|=
name|qb
operator|.
name|parse
argument_list|(
name|filter
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|p
operator|=
name|filterPredicate
expr_stmt|;
block|}
else|else
block|{
name|p
operator|=
name|Predicate
operator|.
name|and
argument_list|(
name|filterPredicate
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|asMatchable
argument_list|()
operator|.
name|match
argument_list|(
name|changeData
argument_list|)
return|;
block|}
block|}
end_class

end_unit

