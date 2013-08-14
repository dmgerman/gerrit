begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|GroupDetail
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
name|AccountGroupById
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
name|assistedinject
operator|.
name|Assisted
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
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_class
DECL|class|GroupDetailFactory
specifier|public
class|class
name|GroupDetailFactory
implements|implements
name|Callable
argument_list|<
name|GroupDetail
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (AccountGroup.Id groupId)
name|GroupDetailFactory
name|create
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
function_decl|;
block|}
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|groupControl
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControl
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|aic
specifier|private
specifier|final
name|AccountInfoCacheFactory
name|aic
decl_stmt|;
DECL|field|gic
specifier|private
specifier|final
name|GroupInfoCacheFactory
name|gic
decl_stmt|;
DECL|field|groupId
specifier|private
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
decl_stmt|;
DECL|field|control
specifier|private
name|GroupControl
name|control
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupDetailFactory (final ReviewDb db, final GroupControl.Factory groupControl, final GroupCache groupCache, final GroupBackend groupBackend, final AccountInfoCacheFactory.Factory accountInfoCacheFactory, final GroupInfoCacheFactory.Factory groupInfoCacheFactory, @Assisted final AccountGroup.Id groupId)
name|GroupDetailFactory
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControl
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|GroupBackend
name|groupBackend
parameter_list|,
specifier|final
name|AccountInfoCacheFactory
operator|.
name|Factory
name|accountInfoCacheFactory
parameter_list|,
specifier|final
name|GroupInfoCacheFactory
operator|.
name|Factory
name|groupInfoCacheFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|groupControl
operator|=
name|groupControl
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|aic
operator|=
name|accountInfoCacheFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|this
operator|.
name|gic
operator|=
name|groupInfoCacheFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|GroupDetail
name|call
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
name|control
operator|=
name|groupControl
operator|.
name|validateFor
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
specifier|final
name|GroupDetail
name|detail
init|=
operator|new
name|GroupDetail
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|GroupDescription
operator|.
name|Basic
name|ownerGroup
init|=
name|groupBackend
operator|.
name|get
argument_list|(
name|group
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ownerGroup
operator|!=
literal|null
condition|)
block|{
name|detail
operator|.
name|setOwnerGroup
argument_list|(
name|GroupReference
operator|.
name|forGroup
argument_list|(
name|ownerGroup
argument_list|)
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|group
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|INTERNAL
case|:
name|detail
operator|.
name|setMembers
argument_list|(
name|loadMembers
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setIncludes
argument_list|(
name|loadIncludes
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|SYSTEM
case|:
break|break;
block|}
name|detail
operator|.
name|setAccounts
argument_list|(
name|aic
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setCanModify
argument_list|(
name|control
operator|.
name|isOwner
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|detail
return|;
block|}
DECL|method|loadMembers ()
specifier|private
name|List
argument_list|<
name|AccountGroupMember
argument_list|>
name|loadMembers
parameter_list|()
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|AccountGroupMember
argument_list|>
name|members
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroupMember
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroupMember
name|m
range|:
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|byGroup
argument_list|(
name|groupId
argument_list|)
control|)
block|{
if|if
condition|(
name|control
operator|.
name|canSeeMember
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
name|aic
operator|.
name|want
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|members
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|members
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AccountGroupMember
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|AccountGroupMember
name|o1
parameter_list|,
specifier|final
name|AccountGroupMember
name|o2
parameter_list|)
block|{
specifier|final
name|Account
name|a
init|=
name|aic
operator|.
name|get
argument_list|(
name|o1
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|b
init|=
name|aic
operator|.
name|get
argument_list|(
name|o2
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|n
argument_list|(
name|a
argument_list|)
operator|.
name|compareTo
argument_list|(
name|n
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|n
parameter_list|(
specifier|final
name|Account
name|a
parameter_list|)
block|{
name|String
name|n
init|=
name|a
operator|.
name|getFullName
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
operator|&&
name|n
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|n
return|;
block|}
name|n
operator|=
name|a
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
operator|&&
name|n
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|n
return|;
block|}
return|return
name|a
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|members
return|;
block|}
DECL|method|loadIncludes ()
specifier|private
name|List
argument_list|<
name|AccountGroupById
argument_list|>
name|loadIncludes
parameter_list|()
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|AccountGroupById
argument_list|>
name|groups
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroupById
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroupById
name|m
range|:
name|db
operator|.
name|accountGroupById
argument_list|()
operator|.
name|byGroup
argument_list|(
name|groupId
argument_list|)
control|)
block|{
if|if
condition|(
name|control
operator|.
name|canSeeGroup
argument_list|(
name|m
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
condition|)
block|{
name|gic
operator|.
name|want
argument_list|(
name|m
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
expr_stmt|;
name|groups
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|groups
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AccountGroupById
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|AccountGroupById
name|o1
parameter_list|,
specifier|final
name|AccountGroupById
name|o2
parameter_list|)
block|{
name|GroupDescription
operator|.
name|Basic
name|a
init|=
name|gic
operator|.
name|get
argument_list|(
name|o1
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
decl_stmt|;
name|GroupDescription
operator|.
name|Basic
name|b
init|=
name|gic
operator|.
name|get
argument_list|(
name|o2
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|n
argument_list|(
name|a
argument_list|)
operator|.
name|compareTo
argument_list|(
name|n
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|n
parameter_list|(
name|GroupDescription
operator|.
name|Basic
name|a
parameter_list|)
block|{
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|String
name|n
init|=
name|a
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|null
operator|&&
name|n
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|n
return|;
block|}
return|return
name|a
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|groups
return|;
block|}
block|}
end_class

end_unit

