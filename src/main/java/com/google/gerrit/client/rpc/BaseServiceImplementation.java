begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
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
name|ProjectCache
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
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
name|client
operator|.
name|reviewdb
operator|.
name|ProjectRight
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
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
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

begin_comment
comment|/** Support for services which require a {@link ReviewDb} instance. */
end_comment

begin_class
DECL|class|BaseServiceImplementation
specifier|public
class|class
name|BaseServiceImplementation
block|{
comment|/**    * Executes<code>action.run</code> with an active ReviewDb connection.    *<p>    * A database handle is automatically opened and closed around the action's    * {@link Action#run(ReviewDb)} method. OrmExceptions are caught and passed    * into the onFailure method of the callback.    *     * @param<T> type of result the callback expects.    * @param callback the callback that will receive the result.    * @param action the action logic to perform.    */
DECL|method|run (final AsyncCallback<T> callback, final Action<T> action)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|void
name|run
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|,
specifier|final
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|)
block|{
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|Common
operator|.
name|getSchemaFactory
argument_list|()
operator|.
name|open
argument_list|()
decl_stmt|;
specifier|final
name|T
name|r
decl_stmt|;
try|try
block|{
name|r
operator|=
name|action
operator|.
name|run
argument_list|(
name|db
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
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|onSuccess
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|Failure
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|CorruptEntityException
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NoSuchEntityException
condition|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
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
catch|catch
parameter_list|(
name|Failure
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Throws NoSuchEntityException if the caller cannot access the project. */
DECL|method|assertCanRead (final Change change)
specifier|public
specifier|static
name|void
name|assertCanRead
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|)
throws|throws
name|Failure
block|{
if|if
condition|(
operator|!
name|canRead
argument_list|(
name|change
argument_list|)
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
block|}
comment|/** Throws NoSuchEntityException if the caller cannot access the project. */
DECL|method|assertCanRead (final Project.NameKey projectKey)
specifier|public
specifier|static
name|void
name|assertCanRead
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectKey
parameter_list|)
throws|throws
name|Failure
block|{
if|if
condition|(
operator|!
name|canRead
argument_list|(
name|projectKey
argument_list|)
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
block|}
comment|/** Return true if the current user can read this change's project. */
DECL|method|canRead (final Change change)
specifier|public
specifier|static
name|boolean
name|canRead
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|)
block|{
return|return
name|change
operator|!=
literal|null
operator|&&
name|canRead
argument_list|(
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
return|;
block|}
comment|/** Return true if the current user can read this project, and its contents. */
DECL|method|canRead (final Project.NameKey projectKey)
specifier|public
specifier|static
name|boolean
name|canRead
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|projectKey
parameter_list|)
block|{
return|return
name|canRead
argument_list|(
name|Common
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|projectKey
argument_list|)
return|;
block|}
DECL|method|canRead (final Account.Id who, final Project.NameKey projectKey)
specifier|public
specifier|static
name|boolean
name|canRead
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|who
parameter_list|,
specifier|final
name|Project
operator|.
name|NameKey
name|projectKey
parameter_list|)
block|{
specifier|final
name|ProjectCache
operator|.
name|Entry
name|e
init|=
name|Common
operator|.
name|getProjectCache
argument_list|()
operator|.
name|get
argument_list|(
name|projectKey
argument_list|)
decl_stmt|;
return|return
name|canRead
argument_list|(
name|who
argument_list|,
name|e
argument_list|)
return|;
block|}
DECL|method|canRead (final Account.Id who, final ProjectCache.Entry e)
specifier|public
specifier|static
name|boolean
name|canRead
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|who
parameter_list|,
specifier|final
name|ProjectCache
operator|.
name|Entry
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
comment|// Unexpected, a project disappearing. But claim its not available.
comment|//
return|return
literal|false
return|;
block|}
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|myGroups
init|=
name|Common
operator|.
name|getGroupCache
argument_list|()
operator|.
name|getEffectiveGroups
argument_list|(
name|who
argument_list|)
decl_stmt|;
return|return
name|canPerform
argument_list|(
name|myGroups
argument_list|,
name|e
argument_list|,
name|ApprovalCategory
operator|.
name|READ
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|canPerform (final Set<AccountGroup.Id> myGroups, final ProjectCache.Entry e, final ApprovalCategory.Id actionId, final short requireValue, final boolean assumeOwner)
specifier|public
specifier|static
name|boolean
name|canPerform
parameter_list|(
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|myGroups
parameter_list|,
specifier|final
name|ProjectCache
operator|.
name|Entry
name|e
parameter_list|,
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|,
specifier|final
name|short
name|requireValue
parameter_list|,
specifier|final
name|boolean
name|assumeOwner
parameter_list|)
block|{
if|if
condition|(
name|assumeOwner
operator|&&
name|myGroups
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getProject
argument_list|()
operator|.
name|getOwnerGroupId
argument_list|()
argument_list|)
condition|)
block|{
comment|// Ownership implies full access.
comment|//
return|return
literal|true
return|;
block|}
name|int
name|val
init|=
name|Integer
operator|.
name|MIN_VALUE
decl_stmt|;
for|for
control|(
specifier|final
name|ProjectRight
name|pr
range|:
name|e
operator|.
name|getRights
argument_list|()
control|)
block|{
if|if
condition|(
name|actionId
operator|.
name|equals
argument_list|(
name|pr
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
operator|&&
name|myGroups
operator|.
name|contains
argument_list|(
name|pr
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|val
argument_list|<
literal|0
operator|&&
name|pr
operator|.
name|getMaxValue
operator|(
operator|)
argument_list|>
literal|0
condition|)
block|{
comment|// If one of the user's groups had denied them access, but
comment|// this group grants them access, prefer the grant over
comment|// the denial. We have to break the tie somehow and we
comment|// prefer being "more open" to being "more closed".
comment|//
name|val
operator|=
name|pr
operator|.
name|getMaxValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Otherwise we use the largest value we can get.
comment|//
name|val
operator|=
name|Math
operator|.
name|max
argument_list|(
name|pr
operator|.
name|getMaxValue
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|val
operator|==
name|Integer
operator|.
name|MIN_VALUE
condition|)
block|{
for|for
control|(
specifier|final
name|ProjectRight
name|pr
range|:
name|Common
operator|.
name|getProjectCache
argument_list|()
operator|.
name|getWildcardRights
argument_list|()
control|)
block|{
if|if
condition|(
name|actionId
operator|.
name|equals
argument_list|(
name|pr
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
operator|&&
name|myGroups
operator|.
name|contains
argument_list|(
name|pr
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|val
operator|=
name|Math
operator|.
name|max
argument_list|(
name|pr
operator|.
name|getMaxValue
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|val
operator|>=
name|requireValue
return|;
block|}
comment|/** Exception whose cause is passed into onFailure. */
DECL|class|Failure
specifier|public
specifier|static
class|class
name|Failure
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|Failure (final Throwable why)
specifier|public
name|Failure
parameter_list|(
specifier|final
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|why
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Arbitrary action to run with a database connection. */
DECL|interface|Action
specifier|public
specifier|static
interface|interface
name|Action
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Perform this action, returning the onSuccess value.      *       * @param db an open database handle to be used by this connection.      * @return he value to pass to {@link AsyncCallback#onSuccess(Object)}.      * @throws OrmException any schema based action failed.      * @throws Failure cause is given to      *         {@link AsyncCallback#onFailure(Throwable)}.      */
DECL|method|run (ReviewDb db)
name|T
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
function_decl|;
block|}
block|}
end_class

end_unit

