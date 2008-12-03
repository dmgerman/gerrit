begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
name|AccountCache
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
name|AccountInfo
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
implements|implements
name|ChangeListService
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
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
name|schema
operator|=
name|rdf
expr_stmt|;
block|}
DECL|method|forAccount (Account.Id id, AsyncCallback<AccountDashboardInfo> callback)
specifier|public
name|void
name|forAccount
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
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
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|me
expr_stmt|;
block|}
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No Account.Id"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|AccountCache
name|accts
init|=
operator|new
name|AccountCache
argument_list|(
name|db
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|user
init|=
name|accts
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No such user"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|starred
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
name|starred
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
operator|new
name|AccountInfo
argument_list|(
name|user
argument_list|)
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
name|user
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|starred
argument_list|,
name|accts
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
name|user
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|starred
argument_list|,
name|accts
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onSuccess
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|myStarredChanges (final AsyncCallback<List<ChangeInfo>> callback)
specifier|public
name|void
name|myStarredChanges
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|List
argument_list|<
name|ChangeInfo
argument_list|>
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
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|AccountCache
name|accts
init|=
operator|new
name|AccountCache
argument_list|(
name|db
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
operator|new
name|HashSet
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
argument_list|()
decl_stmt|;
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
name|starred
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
name|callback
operator|.
name|onSuccess
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
name|accts
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
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
name|callback
operator|.
name|onSuccess
argument_list|(
name|VoidResult
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|myStarredChangeIds (final AsyncCallback<Set<Id>> callback)
specifier|public
name|void
name|myStarredChangeIds
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|Set
argument_list|<
name|Id
argument_list|>
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
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
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
name|callback
operator|.
name|onSuccess
argument_list|(
name|existing
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|list (final ResultSet<Change> rs, final Set<Change.Id> starred, final AccountCache accts)
specifier|private
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
name|AccountCache
name|accts
parameter_list|)
throws|throws
name|OrmException
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
argument_list|,
name|accts
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
block|}
end_class

end_unit

