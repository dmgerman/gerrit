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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|AccessSection
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
name|data
operator|.
name|Permission
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
name|PermissionRule
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
name|rules
operator|.
name|PrologEnvironment
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
name|AnonymousUser
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
name|config
operator|.
name|WildProjectName
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
name|git
operator|.
name|ProjectConfig
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
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|compiler
operator|.
name|CompileException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|JavaObjectTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|SymbolTerm
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
name|PushbackReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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

begin_comment
comment|/** Cached information on a project. */
end_comment

begin_class
DECL|class|ProjectState
specifier|public
class|class
name|ProjectState
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (ProjectConfig config)
name|ProjectState
name|create
parameter_list|(
name|ProjectConfig
name|config
parameter_list|)
function_decl|;
block|}
DECL|field|anonymousUser
specifier|private
specifier|final
name|AnonymousUser
name|anonymousUser
decl_stmt|;
DECL|field|wildProject
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|wildProject
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|AssistedFactory
name|projectControlFactory
decl_stmt|;
DECL|field|envFactory
specifier|private
specifier|final
name|PrologEnvironment
operator|.
name|Factory
name|envFactory
decl_stmt|;
DECL|field|gitMgr
specifier|private
specifier|final
name|GitRepositoryManager
name|gitMgr
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|ProjectConfig
name|config
decl_stmt|;
DECL|field|localOwners
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|localOwners
decl_stmt|;
comment|/** Last system time the configuration's revision was examined. */
DECL|field|lastCheckTime
specifier|private
specifier|transient
name|long
name|lastCheckTime
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectState (final AnonymousUser anonymousUser, final ProjectCache projectCache, @WildProjectName final Project.NameKey wildProject, final ProjectControl.AssistedFactory projectControlFactory, final PrologEnvironment.Factory envFactory, final GitRepositoryManager gitMgr, @Assisted final ProjectConfig config)
specifier|protected
name|ProjectState
parameter_list|(
specifier|final
name|AnonymousUser
name|anonymousUser
parameter_list|,
specifier|final
name|ProjectCache
name|projectCache
parameter_list|,
annotation|@
name|WildProjectName
specifier|final
name|Project
operator|.
name|NameKey
name|wildProject
parameter_list|,
specifier|final
name|ProjectControl
operator|.
name|AssistedFactory
name|projectControlFactory
parameter_list|,
specifier|final
name|PrologEnvironment
operator|.
name|Factory
name|envFactory
parameter_list|,
specifier|final
name|GitRepositoryManager
name|gitMgr
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|ProjectConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|anonymousUser
operator|=
name|anonymousUser
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|wildProject
operator|=
name|wildProject
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|envFactory
operator|=
name|envFactory
expr_stmt|;
name|this
operator|.
name|gitMgr
operator|=
name|gitMgr
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|lastCheckTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groups
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|()
decl_stmt|;
name|AccessSection
name|all
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|ALL
argument_list|)
decl_stmt|;
if|if
condition|(
name|all
operator|!=
literal|null
condition|)
block|{
name|Permission
name|owner
init|=
name|all
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|OWNER
argument_list|)
decl_stmt|;
if|if
condition|(
name|owner
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PermissionRule
name|rule
range|:
name|owner
operator|.
name|getRules
argument_list|()
control|)
block|{
name|GroupReference
name|ref
init|=
name|rule
operator|.
name|getGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|getUUID
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|groups
operator|.
name|add
argument_list|(
name|ref
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|localOwners
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|groups
argument_list|)
expr_stmt|;
block|}
DECL|method|needsRefresh (long generation)
name|boolean
name|needsRefresh
parameter_list|(
name|long
name|generation
parameter_list|)
block|{
if|if
condition|(
name|generation
operator|<=
literal|0
condition|)
block|{
return|return
name|isRevisionOutOfDate
argument_list|()
return|;
block|}
if|if
condition|(
name|lastCheckTime
operator|!=
name|generation
condition|)
block|{
name|lastCheckTime
operator|=
name|generation
expr_stmt|;
return|return
name|isRevisionOutOfDate
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|isRevisionOutOfDate ()
specifier|private
name|boolean
name|isRevisionOutOfDate
parameter_list|()
block|{
try|try
block|{
name|Repository
name|git
init|=
name|gitMgr
operator|.
name|openRepository
argument_list|(
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Ref
name|ref
init|=
name|git
operator|.
name|getRef
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
operator|||
name|ref
operator|.
name|getObjectId
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
operator|!
name|ref
operator|.
name|getObjectId
argument_list|()
operator|.
name|equals
argument_list|(
name|config
operator|.
name|getRevision
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|gone
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|/** @return Construct a new PrologEnvironment for the calling thread. */
DECL|method|newPrologEnvironment ()
specifier|public
name|PrologEnvironment
name|newPrologEnvironment
parameter_list|()
throws|throws
name|CompileException
block|{
comment|// TODO Replace this with a per-project ClassLoader to isolate rules.
name|PrologEnvironment
name|env
init|=
name|envFactory
operator|.
name|create
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
comment|//consult rules.pl at refs/meta/config branch for custom submit rules
name|String
name|rules
init|=
name|getConfig
argument_list|()
operator|.
name|getPrologRules
argument_list|()
decl_stmt|;
if|if
condition|(
name|rules
operator|!=
literal|null
condition|)
block|{
name|PushbackReader
name|in
init|=
operator|new
name|PushbackReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|rules
argument_list|)
argument_list|,
name|Prolog
operator|.
name|PUSHBACK_SIZE
argument_list|)
decl_stmt|;
name|JavaObjectTerm
name|streamObject
init|=
operator|new
name|JavaObjectTerm
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|env
operator|.
name|execute
argument_list|(
name|Prolog
operator|.
name|BUILTIN
argument_list|,
literal|"consult_stream"
argument_list|,
name|SymbolTerm
operator|.
name|intern
argument_list|(
literal|"rules.pl"
argument_list|)
argument_list|,
name|streamObject
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CompileException
argument_list|(
literal|"Cannot consult rules.pl "
operator|+
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|getConfig
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|env
return|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|getConfig
argument_list|()
operator|.
name|getProject
argument_list|()
return|;
block|}
DECL|method|getConfig ()
specifier|public
name|ProjectConfig
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
comment|/** Get the rights that pertain only to this project. */
DECL|method|getLocalAccessSections ()
specifier|public
name|Collection
argument_list|<
name|AccessSection
argument_list|>
name|getLocalAccessSections
parameter_list|()
block|{
return|return
name|getConfig
argument_list|()
operator|.
name|getAccessSections
argument_list|()
return|;
block|}
comment|/** Get the rights this project inherits. */
DECL|method|getInheritedAccessSections ()
specifier|public
name|Collection
argument_list|<
name|AccessSection
argument_list|>
name|getInheritedAccessSections
parameter_list|()
block|{
if|if
condition|(
name|isWildProject
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|AccessSection
argument_list|>
name|inherited
init|=
operator|new
name|ArrayList
argument_list|<
name|AccessSection
argument_list|>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|parent
init|=
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|seen
operator|.
name|add
argument_list|(
name|parent
argument_list|)
condition|)
block|{
name|ProjectState
name|s
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|inherited
operator|.
name|addAll
argument_list|(
name|s
operator|.
name|getLocalAccessSections
argument_list|()
argument_list|)
expr_stmt|;
name|parent
operator|=
name|s
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
comment|// Wild project is the parent, or the root of the tree
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|ProjectState
name|s
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|wildProject
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|inherited
operator|.
name|addAll
argument_list|(
name|s
operator|.
name|getLocalAccessSections
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|inherited
return|;
block|}
comment|/** Get both local and inherited access sections. */
DECL|method|getAllAccessSections ()
specifier|public
name|Collection
argument_list|<
name|AccessSection
argument_list|>
name|getAllAccessSections
parameter_list|()
block|{
name|List
argument_list|<
name|AccessSection
argument_list|>
name|all
init|=
operator|new
name|ArrayList
argument_list|<
name|AccessSection
argument_list|>
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|getLocalAccessSections
argument_list|()
argument_list|)
expr_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|getInheritedAccessSections
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|all
return|;
block|}
comment|/**    * @return all {@link AccountGroup}'s to which the owner privilege for    *         'refs/*' is assigned for this project (the local owners), if there    *         are no local owners the local owners of the nearest parent project    *         that has local owners are returned    */
DECL|method|getOwners ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getOwners
parameter_list|()
block|{
name|Project
operator|.
name|NameKey
name|parentName
init|=
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|localOwners
operator|.
name|isEmpty
argument_list|()
operator|||
name|parentName
operator|==
literal|null
operator|||
name|isWildProject
argument_list|()
condition|)
block|{
return|return
name|localOwners
return|;
block|}
name|ProjectState
name|parent
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|parentName
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getOwners
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
comment|/**    * @return all {@link AccountGroup}'s that are allowed to administrate the    *         complete project. This includes all groups to which the owner    *         privilege for 'refs/*' is assigned for this project (the local    *         owners) and all groups to which the owner privilege for 'refs/*' is    *         assigned for one of the parent projects (the inherited owners).    */
DECL|method|getAllOwners ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getAllOwners
parameter_list|()
block|{
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|owners
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|()
decl_stmt|;
name|owners
operator|.
name|addAll
argument_list|(
name|localOwners
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|parent
init|=
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|seen
operator|.
name|add
argument_list|(
name|parent
argument_list|)
condition|)
block|{
name|ProjectState
name|s
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|owners
operator|.
name|addAll
argument_list|(
name|s
operator|.
name|localOwners
argument_list|)
expr_stmt|;
name|parent
operator|=
name|s
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|owners
argument_list|)
return|;
block|}
DECL|method|controlForAnonymousUser ()
specifier|public
name|ProjectControl
name|controlForAnonymousUser
parameter_list|()
block|{
return|return
name|controlFor
argument_list|(
name|anonymousUser
argument_list|)
return|;
block|}
DECL|method|controlFor (final CurrentUser user)
specifier|public
name|ProjectControl
name|controlFor
parameter_list|(
specifier|final
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
name|projectControlFactory
operator|.
name|create
argument_list|(
name|user
argument_list|,
name|this
argument_list|)
return|;
block|}
DECL|method|isWildProject ()
specifier|private
name|boolean
name|isWildProject
parameter_list|()
block|{
return|return
name|wildProject
operator|.
name|equals
argument_list|(
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

