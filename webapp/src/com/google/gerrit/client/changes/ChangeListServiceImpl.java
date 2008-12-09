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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|data
operator|.
name|AccountDashboardInfo
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
name|data
operator|.
name|AccountInfoCacheFactory
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
name|data
operator|.
name|ChangeInfo
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
name|data
operator|.
name|MineStarredInfo
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
name|ChangeAccess
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
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
operator|.
name|StarredChange
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
operator|.
name|Id
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
name|rpc
operator|.
name|BaseServiceImplementation
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
name|rpc
operator|.
name|NoSuchEntityException
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
name|rpc
operator|.
name|RpcUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
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
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
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
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|Transaction
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
name|Set
import|;
end_import

begin_class
DECL|class|ChangeListServiceImpl
specifier|public
class|class
name|ChangeListServiceImpl
extends|extends
name|BaseServiceImplementation
implements|implements
name|ChangeListService
block|{
DECL|method|ChangeListServiceImpl (final SchemaFactory<ReviewDb> rdf)
specifier|public
name|ChangeListServiceImpl
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|rdf
parameter_list|)
block|{
name|super
argument_list|(
name|rdf
argument_list|)
expr_stmt|;
block|}
DECL|method|forAccount (final Account.Id id, final AsyncCallback<AccountDashboardInfo> callback)
specifier|public
name|void
name|forAccount
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|AccountDashboardInfo
argument_list|>
name|callback
parameter_list|)
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|RpcUtil
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|target
init|=
name|id
operator|!=
literal|null
condition|?
name|id
else|:
name|me
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|AccountDashboardInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|AccountDashboardInfo
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|AccountInfoCacheFactory
name|ac
init|=
operator|new
name|AccountInfoCacheFactory
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|user
init|=
name|ac
operator|.
name|get
argument_list|(
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|stars
init|=
name|starredBy
argument_list|(
name|db
argument_list|,
name|me
argument_list|)
decl_stmt|;
specifier|final
name|ChangeAccess
name|changes
init|=
name|db
operator|.
name|changes
argument_list|()
decl_stmt|;
specifier|final
name|AccountDashboardInfo
name|d
decl_stmt|;
name|d
operator|=
operator|new
name|AccountDashboardInfo
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|d
operator|.
name|setByOwner
argument_list|(
name|list
argument_list|(
name|changes
operator|.
name|byOwnerOpen
argument_list|(
name|target
argument_list|)
argument_list|,
name|stars
argument_list|,
name|ac
argument_list|)
argument_list|)
expr_stmt|;
name|d
operator|.
name|setClosed
argument_list|(
name|list
argument_list|(
name|changes
operator|.
name|byOwnerMerged
argument_list|(
name|target
argument_list|)
argument_list|,
name|stars
argument_list|,
name|ac
argument_list|)
argument_list|)
expr_stmt|;
name|d
operator|.
name|setAccounts
argument_list|(
name|ac
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|d
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|myStarredChanges (final AsyncCallback<MineStarredInfo> callback)
specifier|public
name|void
name|myStarredChanges
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|MineStarredInfo
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|MineStarredInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|MineStarredInfo
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|RpcUtil
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|AccountInfoCacheFactory
name|ac
init|=
operator|new
name|AccountInfoCacheFactory
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|user
init|=
name|ac
operator|.
name|get
argument_list|(
name|me
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
operator|new
name|NoSuchEntityException
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|MineStarredInfo
name|d
init|=
operator|new
name|MineStarredInfo
argument_list|(
name|me
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|starred
init|=
name|starredBy
argument_list|(
name|db
argument_list|,
name|me
argument_list|)
decl_stmt|;
name|d
operator|.
name|setStarred
argument_list|(
name|list
argument_list|(
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|starred
argument_list|)
argument_list|,
name|starred
argument_list|,
name|ac
argument_list|)
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|d
operator|.
name|getStarred
argument_list|()
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|ChangeInfo
name|o1
parameter_list|,
specifier|final
name|ChangeInfo
name|o2
parameter_list|)
block|{
comment|// TODO Sort starred changes by something other than just Id
return|return
name|o1
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|-
name|o2
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|d
operator|.
name|setAccounts
argument_list|(
name|ac
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|d
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|toggleStars (final ToggleStarRequest req, final AsyncCallback<VoidResult> callback)
specifier|public
name|void
name|toggleStars
parameter_list|(
specifier|final
name|ToggleStarRequest
name|req
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|VoidResult
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|RpcUtil
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|existing
init|=
name|starredBy
argument_list|(
name|db
argument_list|,
name|me
argument_list|)
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|StarredChange
argument_list|>
name|add
init|=
operator|new
name|ArrayList
argument_list|<
name|StarredChange
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|StarredChange
argument_list|>
name|remove
init|=
operator|new
name|ArrayList
argument_list|<
name|StarredChange
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|req
operator|.
name|getAddSet
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Change
operator|.
name|Id
name|id
range|:
name|req
operator|.
name|getAddSet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|existing
operator|.
name|contains
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|add
operator|.
name|add
argument_list|(
operator|new
name|StarredChange
argument_list|(
operator|new
name|StarredChange
operator|.
name|Key
argument_list|(
name|me
argument_list|,
name|id
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|req
operator|.
name|getRemoveSet
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Change
operator|.
name|Id
name|id
range|:
name|req
operator|.
name|getRemoveSet
argument_list|()
control|)
block|{
if|if
condition|(
name|existing
operator|.
name|contains
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|remove
operator|.
name|add
argument_list|(
operator|new
name|StarredChange
argument_list|(
operator|new
name|StarredChange
operator|.
name|Key
argument_list|(
name|me
argument_list|,
name|id
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|add
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|remove
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Transaction
name|txn
init|=
name|db
operator|.
name|beginTransaction
argument_list|()
decl_stmt|;
name|db
operator|.
name|starredChanges
argument_list|()
operator|.
name|insert
argument_list|(
name|add
argument_list|)
expr_stmt|;
name|db
operator|.
name|starredChanges
argument_list|()
operator|.
name|delete
argument_list|(
name|remove
argument_list|)
expr_stmt|;
name|txn
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
return|return
name|VoidResult
operator|.
name|INSTANCE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|myStarredChangeIds (final AsyncCallback<Set<Change.Id>> callback)
specifier|public
name|void
name|myStarredChangeIds
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|>
name|callback
parameter_list|)
block|{
name|run
argument_list|(
name|callback
argument_list|,
operator|new
name|Action
argument_list|<
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|Set
argument_list|<
name|Id
argument_list|>
name|run
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|starredBy
argument_list|(
name|db
argument_list|,
name|RpcUtil
operator|.
name|getAccountId
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|list (final ResultSet<Change> rs, final Set<Change.Id> starred, final AccountInfoCacheFactory accts)
specifier|private
specifier|static
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|list
parameter_list|(
specifier|final
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|rs
parameter_list|,
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|starred
parameter_list|,
specifier|final
name|AccountInfoCacheFactory
name|accts
parameter_list|)
block|{
specifier|final
name|ArrayList
argument_list|<
name|ChangeInfo
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Change
name|c
range|:
name|rs
control|)
block|{
specifier|final
name|ChangeInfo
name|ci
init|=
operator|new
name|ChangeInfo
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|ci
operator|.
name|setStarred
argument_list|(
name|starred
operator|.
name|contains
argument_list|(
name|ci
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|accts
operator|.
name|want
argument_list|(
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|add
argument_list|(
name|ci
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|starredBy (final ReviewDb db, final Account.Id me)
specifier|private
specifier|static
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|starredBy
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|me
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|existing
init|=
operator|new
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|me
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|StarredChange
name|sc
range|:
name|db
operator|.
name|starredChanges
argument_list|()
operator|.
name|byAccount
argument_list|(
name|me
argument_list|)
control|)
block|{
name|existing
operator|.
name|add
argument_list|(
name|sc
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|existing
return|;
block|}
block|}
end_class

end_unit

