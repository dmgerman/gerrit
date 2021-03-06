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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|LabelType
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
name|BranchNameKey
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
name|exceptions
operator|.
name|StorageException
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
name|access
operator|.
name|CoreOrPluginProjectPermission
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
name|access
operator|.
name|GlobalOrPluginPermission
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
name|conditions
operator|.
name|BooleanCondition
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|inject
operator|.
name|ImplementedBy
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
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * Checks authorization to perform an action on a project, reference, or change.  *  *<p>{@code check} methods should be used during action handlers to verify the user is allowed to  * exercise the specified permission. For convenience in implementation {@code check} methods throw  * {@link AuthException} if the permission is denied.  *  *<p>{@code test} methods should be used when constructing replies to the client and the result  * object needs to include a true/false hint indicating the user's ability to exercise the  * permission. This is suitable for configuring UI button state, but should not be relied upon to  * guard handlers before making state changes.  *  *<p>{@code PermissionBackend} is a singleton for the server, acting as a factory for lightweight  * request instances. Implementation classes may cache supporting data inside of {@link WithUser},  * {@link ForProject}, {@link ForRef}, and {@link ForChange} instances, in addition to storing  * within {@link CurrentUser} using a {@link com.google.gerrit.server.CurrentUser.PropertyKey}.  * {@link GlobalPermission} caching for {@link WithUser} may best cached inside {@link CurrentUser}  * as {@link WithUser} instances are frequently created.  *  *<p>Example use:  *  *<pre>  *   private final PermissionBackend permissions;  *   private final Provider<CurrentUser> user;  *  *   @Inject  *   Foo(PermissionBackend permissions, Provider<CurrentUser> user) {  *     this.permissions = permissions;  *     this.user = user;  *   }  *  *   public void apply(...) {  *     permissions.user(user).change(cd).check(ChangePermission.SUBMIT);  *   }  *  *   public UiAction.Description getDescription(ChangeResource rsrc) {  *     return new UiAction.Description()  *       .setLabel("Submit")  *       .setVisible(rsrc.permissions().testCond(ChangePermission.SUBMIT));  * }  *</pre>  */
end_comment

begin_class
annotation|@
name|ImplementedBy
argument_list|(
name|DefaultPermissionBackend
operator|.
name|class
argument_list|)
DECL|class|PermissionBackend
specifier|public
specifier|abstract
class|class
name|PermissionBackend
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
comment|/** Returns an instance scoped to the current user. */
DECL|method|currentUser ()
specifier|public
specifier|abstract
name|WithUser
name|currentUser
parameter_list|()
function_decl|;
comment|/**    * Returns an instance scoped to the specified user. Should be used in cases where the user could    * either be the issuer of the current request or an impersonated user. PermissionBackends that do    * not support impersonation can fail with an {@code IllegalStateException}.    *    *<p>If an instance scoped to the current user is desired, use {@code currentUser()} instead.    */
DECL|method|user (CurrentUser user)
specifier|public
specifier|abstract
name|WithUser
name|user
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
function_decl|;
comment|/**    * Returns an instance scoped to the provided user. Should be used in cases where the caller wants    * to check the permissions of a user who is not the issuer of the current request and not the    * target of impersonation.    *    *<p>Usage should be very limited as this can expose a group-oracle.    */
DECL|method|absentUser (Account.Id id)
specifier|public
specifier|abstract
name|WithUser
name|absentUser
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
function_decl|;
comment|/**    * Check whether this {@code PermissionBackend} respects the same global capabilities as the    * {@link DefaultPermissionBackend}.    *    *<p>If true, then it makes sense for downstream callers to refer to built-in Gerrit capability    * names in user-facing error messages, for example.    *    * @return whether this is the default permission backend.    */
DECL|method|usesDefaultCapabilities ()
specifier|public
name|boolean
name|usesDefaultCapabilities
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**    * Throw {@link ResourceNotFoundException} if this backend does not use the default global    * capabilities.    */
DECL|method|checkUsesDefaultCapabilities ()
specifier|public
name|void
name|checkUsesDefaultCapabilities
parameter_list|()
throws|throws
name|ResourceNotFoundException
block|{
if|if
condition|(
operator|!
name|usesDefaultCapabilities
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
literal|"Gerrit capabilities not used on this server"
argument_list|)
throw|;
block|}
block|}
comment|/**    * Bulk evaluate a set of {@link PermissionBackendCondition} for view handling.    *    *<p>Overridden implementations should call {@link PermissionBackendCondition#set(boolean)} to    * cache the result of {@code testOrFalse} in the condition for later evaluation. Caching the    * result will bypass the usual invocation of {@code testOrFalse}.    *    * @param conds conditions to consider.    */
DECL|method|bulkEvaluateTest (Set<PermissionBackendCondition> conds)
specifier|public
name|void
name|bulkEvaluateTest
parameter_list|(
name|Set
argument_list|<
name|PermissionBackendCondition
argument_list|>
name|conds
parameter_list|)
block|{
comment|// Do nothing by default. The default implementation of PermissionBackendCondition
comment|// delegates to the appropriate testOrFalse method in PermissionBackend.
block|}
comment|/** PermissionBackend scoped to a specific user. */
DECL|class|WithUser
specifier|public
specifier|abstract
specifier|static
class|class
name|WithUser
block|{
comment|/** Returns an instance scoped for the specified project. */
DECL|method|project (Project.NameKey project)
specifier|public
specifier|abstract
name|ForProject
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
function_decl|;
comment|/** Returns an instance scoped for the {@code ref}, and its parent project. */
DECL|method|ref (BranchNameKey ref)
specifier|public
name|ForRef
name|ref
parameter_list|(
name|BranchNameKey
name|ref
parameter_list|)
block|{
return|return
name|project
argument_list|(
name|ref
operator|.
name|project
argument_list|()
argument_list|)
operator|.
name|ref
argument_list|(
name|ref
operator|.
name|branch
argument_list|()
argument_list|)
return|;
block|}
comment|/** Returns an instance scoped for the change, and its destination ref and project. */
DECL|method|change (ChangeData cd)
specifier|public
name|ForChange
name|change
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
try|try
block|{
return|return
name|ref
argument_list|(
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|)
operator|.
name|change
argument_list|(
name|cd
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
return|return
name|FailedPermissionBackend
operator|.
name|change
argument_list|(
literal|"unavailable"
argument_list|,
name|e
argument_list|)
return|;
block|}
block|}
comment|/** Returns an instance scoped for the change, and its destination ref and project. */
DECL|method|change (ChangeNotes notes)
specifier|public
name|ForChange
name|change
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|ref
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|)
operator|.
name|change
argument_list|(
name|notes
argument_list|)
return|;
block|}
comment|/**      * Returns an instance scoped for the change loaded from index, and its destination ref and      * project. This method should only be used when database access is harmful and potentially      * stale data from the index is acceptable.      */
DECL|method|indexedChange (ChangeData cd, ChangeNotes notes)
specifier|public
name|ForChange
name|indexedChange
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|ref
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|)
operator|.
name|indexedChange
argument_list|(
name|cd
argument_list|,
name|notes
argument_list|)
return|;
block|}
comment|/** Verify scoped user can {@code perm}, throwing if denied. */
DECL|method|check (GlobalOrPluginPermission perm)
specifier|public
specifier|abstract
name|void
name|check
parameter_list|(
name|GlobalOrPluginPermission
name|perm
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
function_decl|;
comment|/**      * Verify scoped user can perform at least one listed permission.      *      *<p>If {@code any} is empty, the method completes normally and allows the caller to continue.      * Since no permissions were supplied to check, its assumed no permissions are necessary to      * continue with the caller's operation.      *      *<p>If the user has at least one of the permissions in {@code any}, the method completes      * normally, possibly without checking all listed permissions.      *      *<p>If {@code any} is non-empty and the user has none, {@link AuthException} is thrown for one      * of the failed permissions.      *      * @param any set of permissions to check.      */
DECL|method|checkAny (Set<GlobalOrPluginPermission> any)
specifier|public
name|void
name|checkAny
parameter_list|(
name|Set
argument_list|<
name|GlobalOrPluginPermission
argument_list|>
name|any
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|AuthException
block|{
for|for
control|(
name|Iterator
argument_list|<
name|GlobalOrPluginPermission
argument_list|>
name|itr
init|=
name|any
operator|.
name|iterator
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
try|try
block|{
name|check
argument_list|(
name|itr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|AuthException
name|err
parameter_list|)
block|{
if|if
condition|(
operator|!
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
name|err
throw|;
block|}
block|}
block|}
block|}
comment|/** Filter {@code permSet} to permissions scoped user might be able to perform. */
DECL|method|test (Collection<T> permSet)
specifier|public
specifier|abstract
parameter_list|<
name|T
extends|extends
name|GlobalOrPluginPermission
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|test
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|permSet
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
DECL|method|test (GlobalOrPluginPermission perm)
specifier|public
name|boolean
name|test
parameter_list|(
name|GlobalOrPluginPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
return|return
name|test
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|perm
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|perm
argument_list|)
return|;
block|}
DECL|method|testOrFalse (GlobalOrPluginPermission perm)
specifier|public
name|boolean
name|testOrFalse
parameter_list|(
name|GlobalOrPluginPermission
name|perm
parameter_list|)
block|{
try|try
block|{
return|return
name|test
argument_list|(
name|perm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot test %s; assuming false"
argument_list|,
name|perm
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|testCond (GlobalOrPluginPermission perm)
specifier|public
specifier|abstract
name|BooleanCondition
name|testCond
parameter_list|(
name|GlobalOrPluginPermission
name|perm
parameter_list|)
function_decl|;
comment|/**      * Filter a set of projects using {@code check(perm)}.      *      * @param perm required permission in a project to be included in result.      * @param projects candidate set of projects; may be empty.      * @return filtered set of {@code projects} where {@code check(perm)} was successful.      * @throws PermissionBackendException backend cannot access its internal state.      */
DECL|method|filter (ProjectPermission perm, Collection<Project.NameKey> projects)
specifier|public
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|filter
parameter_list|(
name|ProjectPermission
name|perm
parameter_list|,
name|Collection
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projects
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|requireNonNull
argument_list|(
name|perm
argument_list|,
literal|"ProjectPermission"
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|projects
argument_list|,
literal|"projects"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|allowed
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|projects
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|project
range|:
name|projects
control|)
block|{
try|try
block|{
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|check
argument_list|(
name|perm
argument_list|)
expr_stmt|;
name|allowed
operator|.
name|add
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
comment|// Do not include this project in allowed.
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
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
name|RepositoryNotFoundException
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Could not find repository of the project %s"
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Do not include this project because doesn't exist
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
return|return
name|allowed
return|;
block|}
block|}
comment|/** PermissionBackend scoped to a user and project. */
DECL|class|ForProject
specifier|public
specifier|abstract
specifier|static
class|class
name|ForProject
block|{
comment|/** Returns the fully qualified resource path that this instance is scoped to. */
DECL|method|resourcePath ()
specifier|public
specifier|abstract
name|String
name|resourcePath
parameter_list|()
function_decl|;
comment|/** Returns an instance scoped for {@code ref} in this project. */
DECL|method|ref (String ref)
specifier|public
specifier|abstract
name|ForRef
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
function_decl|;
comment|/** Returns an instance scoped for the change, and its destination ref and project. */
DECL|method|change (ChangeData cd)
specifier|public
name|ForChange
name|change
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
try|try
block|{
return|return
name|ref
argument_list|(
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
operator|.
name|change
argument_list|(
name|cd
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
return|return
name|FailedPermissionBackend
operator|.
name|change
argument_list|(
literal|"unavailable"
argument_list|,
name|e
argument_list|)
return|;
block|}
block|}
comment|/** Returns an instance scoped for the change, and its destination ref and project. */
DECL|method|change (ChangeNotes notes)
specifier|public
name|ForChange
name|change
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|ref
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
operator|.
name|change
argument_list|(
name|notes
argument_list|)
return|;
block|}
comment|/**      * Returns an instance scoped for the change loaded from index, and its destination ref and      * project. This method should only be used when database access is harmful and potentially      * stale data from the index is acceptable.      */
DECL|method|indexedChange (ChangeData cd, ChangeNotes notes)
specifier|public
name|ForChange
name|indexedChange
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
block|{
return|return
name|ref
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
argument_list|)
operator|.
name|indexedChange
argument_list|(
name|cd
argument_list|,
name|notes
argument_list|)
return|;
block|}
comment|/** Verify scoped user can {@code perm}, throwing if denied. */
DECL|method|check (CoreOrPluginProjectPermission perm)
specifier|public
specifier|abstract
name|void
name|check
parameter_list|(
name|CoreOrPluginProjectPermission
name|perm
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
function_decl|;
comment|/** Filter {@code permSet} to permissions scoped user might be able to perform. */
DECL|method|test (Collection<T> permSet)
specifier|public
specifier|abstract
parameter_list|<
name|T
extends|extends
name|CoreOrPluginProjectPermission
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|test
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|permSet
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
DECL|method|test (CoreOrPluginProjectPermission perm)
specifier|public
name|boolean
name|test
parameter_list|(
name|CoreOrPluginProjectPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
if|if
condition|(
name|perm
operator|instanceof
name|ProjectPermission
condition|)
block|{
return|return
name|test
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
operator|(
name|ProjectPermission
operator|)
name|perm
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|perm
argument_list|)
return|;
block|}
comment|// TODO(xchangcheng): implement for plugin defined project permissions.
return|return
literal|false
return|;
block|}
DECL|method|testOrFalse (CoreOrPluginProjectPermission perm)
specifier|public
name|boolean
name|testOrFalse
parameter_list|(
name|CoreOrPluginProjectPermission
name|perm
parameter_list|)
block|{
try|try
block|{
return|return
name|test
argument_list|(
name|perm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot test %s; assuming false"
argument_list|,
name|perm
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|testCond (CoreOrPluginProjectPermission perm)
specifier|public
specifier|abstract
name|BooleanCondition
name|testCond
parameter_list|(
name|CoreOrPluginProjectPermission
name|perm
parameter_list|)
function_decl|;
comment|/**      * Filter a list of references by visibility.      *      * @param refs a collection of references to filter.      * @param repo an open {@link Repository} handle for this instance's project      * @param opts further options for filtering.      * @return a partition of the provided refs that are visible to the user that this instance is      *     scoped to.      * @throws PermissionBackendException if failure consulting backend configuration.      */
DECL|method|filter ( Collection<Ref> refs, Repository repo, RefFilterOptions opts)
specifier|public
specifier|abstract
name|Collection
argument_list|<
name|Ref
argument_list|>
name|filter
parameter_list|(
name|Collection
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|RefFilterOptions
name|opts
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
block|}
comment|/** Options for filtering refs using {@link ForProject}. */
annotation|@
name|AutoValue
DECL|class|RefFilterOptions
specifier|public
specifier|abstract
specifier|static
class|class
name|RefFilterOptions
block|{
comment|/** Remove all NoteDb refs (refs/changes/*, refs/users/*, edit refs) from the result. */
DECL|method|filterMeta ()
specifier|public
specifier|abstract
name|boolean
name|filterMeta
parameter_list|()
function_decl|;
comment|/**      * Select only refs with names matching prefixes per {@link      * org.eclipse.jgit.lib.RefDatabase#getRefsByPrefix}.      */
DECL|method|prefixes ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|prefixes
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_PermissionBackend_RefFilterOptions
operator|.
name|Builder
argument_list|()
operator|.
name|setFilterMeta
argument_list|(
literal|false
argument_list|)
operator|.
name|setPrefixes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|""
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|setFilterMeta (boolean val)
specifier|public
specifier|abstract
name|Builder
name|setFilterMeta
parameter_list|(
name|boolean
name|val
parameter_list|)
function_decl|;
DECL|method|setPrefixes (List<String> prefixes)
specifier|public
specifier|abstract
name|Builder
name|setPrefixes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|RefFilterOptions
name|build
parameter_list|()
function_decl|;
block|}
DECL|method|defaults ()
specifier|public
specifier|static
name|RefFilterOptions
name|defaults
parameter_list|()
block|{
return|return
name|builder
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
comment|/** PermissionBackend scoped to a user, project and reference. */
DECL|class|ForRef
specifier|public
specifier|abstract
specifier|static
class|class
name|ForRef
block|{
comment|/** Returns a fully qualified resource path that this instance is scoped to. */
DECL|method|resourcePath ()
specifier|public
specifier|abstract
name|String
name|resourcePath
parameter_list|()
function_decl|;
comment|/** Returns an instance scoped to change. */
DECL|method|change (ChangeData cd)
specifier|public
specifier|abstract
name|ForChange
name|change
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
function_decl|;
comment|/** Returns an instance scoped to change. */
DECL|method|change (ChangeNotes notes)
specifier|public
specifier|abstract
name|ForChange
name|change
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
function_decl|;
comment|/**      * @return instance scoped to change loaded from index. This method should only be used when      *     database access is harmful and potentially stale data from the index is acceptable.      */
DECL|method|indexedChange (ChangeData cd, ChangeNotes notes)
specifier|public
specifier|abstract
name|ForChange
name|indexedChange
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
function_decl|;
comment|/** Verify scoped user can {@code perm}, throwing if denied. */
DECL|method|check (RefPermission perm)
specifier|public
specifier|abstract
name|void
name|check
parameter_list|(
name|RefPermission
name|perm
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
function_decl|;
comment|/** Filter {@code permSet} to permissions scoped user might be able to perform. */
DECL|method|test (Collection<RefPermission> permSet)
specifier|public
specifier|abstract
name|Set
argument_list|<
name|RefPermission
argument_list|>
name|test
parameter_list|(
name|Collection
argument_list|<
name|RefPermission
argument_list|>
name|permSet
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
DECL|method|test (RefPermission perm)
specifier|public
name|boolean
name|test
parameter_list|(
name|RefPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
return|return
name|test
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
name|perm
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|perm
argument_list|)
return|;
block|}
comment|/**      * Test if user may be able to perform the permission.      *      *<p>Similar to {@link #test(RefPermission)} except this method returns {@code false} instead      * of throwing an exception.      *      * @param perm the permission to test.      * @return true if the user might be able to perform the permission; false if the user may be      *     missing the necessary grants or state, or if the backend threw an exception.      */
DECL|method|testOrFalse (RefPermission perm)
specifier|public
name|boolean
name|testOrFalse
parameter_list|(
name|RefPermission
name|perm
parameter_list|)
block|{
try|try
block|{
return|return
name|test
argument_list|(
name|perm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot test %s; assuming false"
argument_list|,
name|perm
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|testCond (RefPermission perm)
specifier|public
specifier|abstract
name|BooleanCondition
name|testCond
parameter_list|(
name|RefPermission
name|perm
parameter_list|)
function_decl|;
block|}
comment|/** PermissionBackend scoped to a user, project, reference and change. */
DECL|class|ForChange
specifier|public
specifier|abstract
specifier|static
class|class
name|ForChange
block|{
comment|/** Returns the fully qualified resource path that this instance is scoped to. */
DECL|method|resourcePath ()
specifier|public
specifier|abstract
name|String
name|resourcePath
parameter_list|()
function_decl|;
comment|/** Verify scoped user can {@code perm}, throwing if denied. */
DECL|method|check (ChangePermissionOrLabel perm)
specifier|public
specifier|abstract
name|void
name|check
parameter_list|(
name|ChangePermissionOrLabel
name|perm
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
function_decl|;
comment|/** Filter {@code permSet} to permissions scoped user might be able to perform. */
DECL|method|test (Collection<T> permSet)
specifier|public
specifier|abstract
parameter_list|<
name|T
extends|extends
name|ChangePermissionOrLabel
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|test
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|permSet
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
DECL|method|test (ChangePermissionOrLabel perm)
specifier|public
name|boolean
name|test
parameter_list|(
name|ChangePermissionOrLabel
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
return|return
name|test
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|perm
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|perm
argument_list|)
return|;
block|}
comment|/**      * Test if user may be able to perform the permission.      *      *<p>Similar to {@link #test(ChangePermissionOrLabel)} except this method returns {@code false}      * instead of throwing an exception.      *      * @param perm the permission to test.      * @return true if the user might be able to perform the permission; false if the user may be      *     missing the necessary grants or state, or if the backend threw an exception.      */
DECL|method|testOrFalse (ChangePermissionOrLabel perm)
specifier|public
name|boolean
name|testOrFalse
parameter_list|(
name|ChangePermissionOrLabel
name|perm
parameter_list|)
block|{
try|try
block|{
return|return
name|test
argument_list|(
name|perm
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot test %s; assuming false"
argument_list|,
name|perm
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|testCond (ChangePermissionOrLabel perm)
specifier|public
specifier|abstract
name|BooleanCondition
name|testCond
parameter_list|(
name|ChangePermissionOrLabel
name|perm
parameter_list|)
function_decl|;
comment|/**      * Test which values of a label the user may be able to set.      *      * @param label definition of the label to test values of.      * @return set containing values the user may be able to use; may be empty if none.      * @throws PermissionBackendException if failure consulting backend configuration.      */
DECL|method|test (LabelType label)
specifier|public
name|Set
argument_list|<
name|LabelPermission
operator|.
name|WithValue
argument_list|>
name|test
parameter_list|(
name|LabelType
name|label
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
return|return
name|test
argument_list|(
name|valuesOf
argument_list|(
name|requireNonNull
argument_list|(
name|label
argument_list|,
literal|"LabelType"
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Test which values of a group of labels the user may be able to set.      *      * @param types definition of the labels to test values of.      * @return set containing values the user may be able to use; may be empty if none.      * @throws PermissionBackendException if failure consulting backend configuration.      */
DECL|method|testLabels (Collection<LabelType> types)
specifier|public
name|Set
argument_list|<
name|LabelPermission
operator|.
name|WithValue
argument_list|>
name|testLabels
parameter_list|(
name|Collection
argument_list|<
name|LabelType
argument_list|>
name|types
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|requireNonNull
argument_list|(
name|types
argument_list|,
literal|"LabelType"
argument_list|)
expr_stmt|;
return|return
name|test
argument_list|(
name|types
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
parameter_list|(
name|t
parameter_list|)
lambda|->
name|valuesOf
argument_list|(
name|t
argument_list|)
operator|.
name|stream
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|valuesOf (LabelType label)
specifier|private
specifier|static
name|Set
argument_list|<
name|LabelPermission
operator|.
name|WithValue
argument_list|>
name|valuesOf
parameter_list|(
name|LabelType
name|label
parameter_list|)
block|{
return|return
name|label
operator|.
name|getValues
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
parameter_list|(
name|v
parameter_list|)
lambda|->
operator|new
name|LabelPermission
operator|.
name|WithValue
argument_list|(
name|label
argument_list|,
name|v
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

