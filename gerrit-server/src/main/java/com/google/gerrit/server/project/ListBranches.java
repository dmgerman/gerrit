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
name|common
operator|.
name|base
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
name|Sets
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
name|common
operator|.
name|ActionInfo
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
name|common
operator|.
name|WebLinkInfo
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
name|registration
operator|.
name|DynamicMap
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
name|BadRequestException
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|RestView
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
name|webui
operator|.
name|UiAction
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
name|RefNames
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
name|WebLinks
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
name|webui
operator|.
name|UiActions
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
name|util
operator|.
name|Providers
import|;
end_import

begin_import
import|import
name|dk
operator|.
name|brics
operator|.
name|automaton
operator|.
name|RegExp
import|;
end_import

begin_import
import|import
name|dk
operator|.
name|brics
operator|.
name|automaton
operator|.
name|RunAutomaton
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
name|Constants
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
name|RefDatabase
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
name|Locale
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
name|TreeMap
import|;
end_import

begin_class
DECL|class|ListBranches
specifier|public
class|class
name|ListBranches
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|branchViews
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|BranchResource
argument_list|>
argument_list|>
name|branchViews
decl_stmt|;
DECL|field|webLinks
specifier|private
specifier|final
name|WebLinks
name|webLinks
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
literal|"maximum number of branches to list"
argument_list|)
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--start"
argument_list|,
name|aliases
operator|=
block|{
literal|"-s"
block|}
argument_list|,
name|metaVar
operator|=
literal|"CNT"
argument_list|,
name|usage
operator|=
literal|"number of branches to skip"
argument_list|)
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--match"
argument_list|,
name|aliases
operator|=
block|{
literal|"-m"
block|}
argument_list|,
name|metaVar
operator|=
literal|"MATCH"
argument_list|,
name|usage
operator|=
literal|"match branches substring"
argument_list|)
DECL|field|matchSubstring
specifier|private
name|String
name|matchSubstring
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--regex"
argument_list|,
name|aliases
operator|=
block|{
literal|"-r"
block|}
argument_list|,
name|metaVar
operator|=
literal|"REGEX"
argument_list|,
name|usage
operator|=
literal|"match branches regex"
argument_list|)
DECL|field|matchRegex
specifier|private
name|String
name|matchRegex
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListBranches (GitRepositoryManager repoManager, DynamicMap<RestView<BranchResource>> branchViews, WebLinks webLinks)
specifier|public
name|ListBranches
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|BranchResource
argument_list|>
argument_list|>
name|branchViews
parameter_list|,
name|WebLinks
name|webLinks
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|branchViews
operator|=
name|branchViews
expr_stmt|;
name|this
operator|.
name|webLinks
operator|=
name|webLinks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc)
specifier|public
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
throws|,
name|BadRequestException
block|{
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|branches
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|BranchInfo
name|headBranch
init|=
literal|null
decl_stmt|;
name|BranchInfo
name|configBranch
init|=
literal|null
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|targets
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
specifier|final
name|Repository
name|db
decl_stmt|;
try|try
block|{
name|db
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|noGitRepository
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
try|try
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|all
init|=
name|db
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|RefDatabase
operator|.
name|ALL
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|all
operator|.
name|containsKey
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
condition|)
block|{
comment|// The branch pointed to by HEAD doesn't exist yet, so getAllRefs
comment|// filtered it out. If we ask for it individually we can find the
comment|// underlying target and put it into the map anyway.
comment|//
try|try
block|{
name|Ref
name|head
init|=
name|db
operator|.
name|getRef
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
decl_stmt|;
if|if
condition|(
name|head
operator|!=
literal|null
condition|)
block|{
name|all
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|,
name|head
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore the failure reading HEAD.
block|}
block|}
for|for
control|(
specifier|final
name|Ref
name|ref
range|:
name|all
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|ref
operator|.
name|isSymbolic
argument_list|()
condition|)
block|{
name|targets
operator|.
name|add
argument_list|(
name|ref
operator|.
name|getTarget
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
specifier|final
name|Ref
name|ref
range|:
name|all
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|ref
operator|.
name|isSymbolic
argument_list|()
condition|)
block|{
comment|// A symbolic reference to another branch, instead of
comment|// showing the resolved value, show the name it references.
comment|//
name|String
name|target
init|=
name|ref
operator|.
name|getTarget
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|RefControl
name|targetRefControl
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|controlForRef
argument_list|(
name|target
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|targetRefControl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|target
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_HEADS
argument_list|)
condition|)
block|{
name|target
operator|=
name|target
operator|.
name|substring
argument_list|(
name|Constants
operator|.
name|R_HEADS
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|BranchInfo
name|b
init|=
operator|new
name|BranchInfo
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|target
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|Constants
operator|.
name|HEAD
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|headBranch
operator|=
name|b
expr_stmt|;
block|}
else|else
block|{
name|b
operator|.
name|setCanDelete
argument_list|(
name|targetRefControl
operator|.
name|canDelete
argument_list|()
argument_list|)
expr_stmt|;
name|branches
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
specifier|final
name|RefControl
name|refControl
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|controlForRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|refControl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
if|if
condition|(
name|ref
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_HEADS
argument_list|)
condition|)
block|{
name|branches
operator|.
name|add
argument_list|(
name|createBranchInfo
argument_list|(
name|ref
argument_list|,
name|refControl
argument_list|,
name|targets
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RefNames
operator|.
name|REFS_CONFIG
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|configBranch
operator|=
name|createBranchInfo
argument_list|(
name|ref
argument_list|,
name|refControl
argument_list|,
name|targets
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|branches
argument_list|,
operator|new
name|Comparator
argument_list|<
name|BranchInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|BranchInfo
name|a
parameter_list|,
specifier|final
name|BranchInfo
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|ref
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|ref
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|configBranch
operator|!=
literal|null
condition|)
block|{
name|branches
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|configBranch
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headBranch
operator|!=
literal|null
condition|)
block|{
name|branches
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|headBranch
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|filteredBranches
decl_stmt|;
if|if
condition|(
operator|(
name|matchSubstring
operator|!=
literal|null
operator|&&
operator|!
name|matchSubstring
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|matchRegex
operator|!=
literal|null
operator|&&
operator|!
name|matchRegex
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|filteredBranches
operator|=
name|filterBranches
argument_list|(
name|branches
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filteredBranches
operator|=
name|branches
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|filteredBranches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|int
name|end
init|=
name|filteredBranches
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
operator|&&
name|start
operator|+
name|limit
operator|<
name|end
condition|)
block|{
name|end
operator|=
name|start
operator|+
name|limit
expr_stmt|;
block|}
if|if
condition|(
name|start
operator|<=
name|end
condition|)
block|{
name|filteredBranches
operator|=
name|filteredBranches
operator|.
name|subList
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|filteredBranches
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|filteredBranches
return|;
block|}
DECL|method|filterBranches (List<BranchInfo> branches)
specifier|private
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|filterBranches
parameter_list|(
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|branches
parameter_list|)
throws|throws
name|BadRequestException
block|{
if|if
condition|(
name|matchSubstring
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|List
argument_list|<
name|BranchInfo
argument_list|>
operator|)
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|branches
argument_list|,
operator|new
name|Predicate
argument_list|<
name|BranchInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|apply
parameter_list|(
name|BranchInfo
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|ref
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
name|matchSubstring
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
operator|)
return|;
block|}
elseif|else
if|if
condition|(
name|matchRegex
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|matchRegex
operator|.
name|startsWith
argument_list|(
literal|"^"
argument_list|)
condition|)
block|{
name|matchRegex
operator|=
name|matchRegex
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|matchRegex
operator|.
name|endsWith
argument_list|(
literal|"$"
argument_list|)
operator|&&
operator|!
name|matchRegex
operator|.
name|endsWith
argument_list|(
literal|"\\$"
argument_list|)
condition|)
block|{
name|matchRegex
operator|=
name|matchRegex
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|matchRegex
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|matchRegex
operator|.
name|equals
argument_list|(
literal|".*"
argument_list|)
condition|)
block|{
return|return
name|branches
return|;
block|}
try|try
block|{
specifier|final
name|RunAutomaton
name|a
init|=
operator|new
name|RunAutomaton
argument_list|(
operator|new
name|RegExp
argument_list|(
name|matchRegex
argument_list|)
operator|.
name|toAutomaton
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
operator|(
name|List
argument_list|<
name|BranchInfo
argument_list|>
operator|)
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|branches
argument_list|,
operator|new
name|Predicate
argument_list|<
name|BranchInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|boolean
name|apply
parameter_list|(
name|BranchInfo
name|in
parameter_list|)
block|{
return|return
name|a
operator|.
name|run
argument_list|(
name|in
operator|.
name|ref
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|branches
return|;
block|}
DECL|method|createBranchInfo (Ref ref, RefControl refControl, Set<String> targets)
specifier|private
name|BranchInfo
name|createBranchInfo
parameter_list|(
name|Ref
name|ref
parameter_list|,
name|RefControl
name|refControl
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|targets
parameter_list|)
block|{
name|BranchInfo
name|info
init|=
operator|new
name|BranchInfo
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|?
name|ref
operator|.
name|getObjectId
argument_list|()
operator|.
name|name
argument_list|()
else|:
literal|null
argument_list|,
operator|!
name|targets
operator|.
name|contains
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|refControl
operator|.
name|canDelete
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|UiAction
operator|.
name|Description
name|d
range|:
name|UiActions
operator|.
name|from
argument_list|(
name|branchViews
argument_list|,
operator|new
name|BranchResource
argument_list|(
name|refControl
operator|.
name|getProjectControl
argument_list|()
argument_list|,
name|info
argument_list|)
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|refControl
operator|.
name|getCurrentUser
argument_list|()
argument_list|)
argument_list|)
control|)
block|{
if|if
condition|(
name|info
operator|.
name|actions
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|actions
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|info
operator|.
name|actions
operator|.
name|put
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|ActionInfo
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|webLinks
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
for|for
control|(
name|WebLinkInfo
name|link
range|:
name|webLinks
operator|.
name|getBranchLinks
argument_list|(
name|refControl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|link
operator|.
name|name
argument_list|)
operator|&&
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|link
operator|.
name|url
argument_list|)
condition|)
block|{
name|info
operator|.
name|webLinks
operator|.
name|add
argument_list|(
name|link
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|info
return|;
block|}
DECL|class|BranchInfo
specifier|public
specifier|static
class|class
name|BranchInfo
block|{
DECL|field|ref
specifier|public
name|String
name|ref
decl_stmt|;
DECL|field|revision
specifier|public
name|String
name|revision
decl_stmt|;
DECL|field|canDelete
specifier|public
name|Boolean
name|canDelete
decl_stmt|;
DECL|field|actions
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|actions
decl_stmt|;
DECL|field|webLinks
specifier|public
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
decl_stmt|;
DECL|method|BranchInfo (String ref, String revision, boolean canDelete)
specifier|public
name|BranchInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|revision
parameter_list|,
name|boolean
name|canDelete
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|canDelete
operator|=
name|canDelete
expr_stmt|;
block|}
DECL|method|setCanDelete (boolean canDelete)
name|void
name|setCanDelete
parameter_list|(
name|boolean
name|canDelete
parameter_list|)
block|{
name|this
operator|.
name|canDelete
operator|=
name|canDelete
condition|?
literal|true
else|:
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

