begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

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
name|toList
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
name|toMap
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
name|Preconditions
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
name|Iterables
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackend
operator|.
name|RefFilterOptions
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
name|permissions
operator|.
name|PermissionBackendException
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
name|Collections
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
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|annotations
operator|.
name|NonNull
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
name|annotations
operator|.
name|Nullable
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
name|ObjectId
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
name|RefRename
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
name|RefUpdate
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
comment|/**  * Wrapper around {@link DelegateRefDatabase} that filters all refs using {@link  * com.google.gerrit.server.permissions.PermissionBackend}.  */
end_comment

begin_class
DECL|class|PermissionAwareReadOnlyRefDatabase
specifier|public
class|class
name|PermissionAwareReadOnlyRefDatabase
extends|extends
name|DelegateRefDatabase
block|{
DECL|field|forProject
specifier|private
specifier|final
name|PermissionBackend
operator|.
name|ForProject
name|forProject
decl_stmt|;
annotation|@
name|Inject
DECL|method|PermissionAwareReadOnlyRefDatabase ( Repository delegateRepository, PermissionBackend.ForProject forProject)
name|PermissionAwareReadOnlyRefDatabase
parameter_list|(
name|Repository
name|delegateRepository
parameter_list|,
name|PermissionBackend
operator|.
name|ForProject
name|forProject
parameter_list|)
block|{
name|super
argument_list|(
name|delegateRepository
argument_list|)
expr_stmt|;
name|this
operator|.
name|forProject
operator|=
name|forProject
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isNameConflicting (String name)
specifier|public
name|boolean
name|isNameConflicting
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"PermissionAwareReadOnlyRefDatabase is read-only"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|newUpdate (String name, boolean detach)
specifier|public
name|RefUpdate
name|newUpdate
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|detach
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"PermissionAwareReadOnlyRefDatabase is read-only"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|newRename (String fromName, String toName)
specifier|public
name|RefRename
name|newRename
parameter_list|(
name|String
name|fromName
parameter_list|,
name|String
name|toName
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"PermissionAwareReadOnlyRefDatabase is read-only"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|exactRef (String name)
specifier|public
name|Ref
name|exactRef
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Ref
name|ref
init|=
name|getDelegate
argument_list|()
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Collection
argument_list|<
name|Ref
argument_list|>
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|forProject
operator|.
name|filter
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|ref
argument_list|)
argument_list|,
name|getDelegate
argument_list|()
argument_list|,
name|RefFilterOptions
operator|.
name|defaults
argument_list|()
argument_list|)
expr_stmt|;
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
name|IOException
condition|)
block|{
throw|throw
operator|(
name|IOException
operator|)
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Preconditions
operator|.
name|checkState
argument_list|(
name|result
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|,
literal|"Only one element expected, but was: "
operator|+
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|result
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|getRefs (String prefix)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|getRefs
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
init|=
name|getDelegate
argument_list|()
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|refs
return|;
block|}
name|Collection
argument_list|<
name|Ref
argument_list|>
name|result
decl_stmt|;
try|try
block|{
comment|// The security filtering assumes to receive the same refMap, independently from the ref
comment|// prefix offset
name|result
operator|=
name|forProject
operator|.
name|filter
argument_list|(
name|refs
operator|.
name|values
argument_list|()
argument_list|,
name|getDelegate
argument_list|()
argument_list|,
name|RefFilterOptions
operator|.
name|defaults
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|""
argument_list|)
throw|;
block|}
return|return
name|buildPrefixRefMap
argument_list|(
name|prefix
argument_list|,
name|result
argument_list|)
return|;
block|}
DECL|method|buildPrefixRefMap (String prefix, Collection<Ref> refs)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|buildPrefixRefMap
parameter_list|(
name|String
name|prefix
parameter_list|,
name|Collection
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|)
block|{
name|int
name|prefixSlashPos
init|=
name|prefix
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|prefixSlashPos
operator|>
literal|0
condition|)
block|{
return|return
name|refs
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
parameter_list|(
name|Ref
name|ref
parameter_list|)
lambda|->
name|ref
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
name|prefixSlashPos
argument_list|)
argument_list|,
name|Function
operator|.
name|identity
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
name|refs
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|Ref
operator|::
name|getName
argument_list|,
name|r
lambda|->
name|r
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRefsByPrefix (String prefix)
specifier|public
name|List
argument_list|<
name|Ref
argument_list|>
name|getRefsByPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|coarseRefs
decl_stmt|;
name|int
name|lastSlash
init|=
name|prefix
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastSlash
operator|==
operator|-
literal|1
condition|)
block|{
name|coarseRefs
operator|=
name|getRefs
argument_list|(
name|ALL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|coarseRefs
operator|=
name|getRefs
argument_list|(
name|prefix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastSlash
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Ref
argument_list|>
name|result
decl_stmt|;
if|if
condition|(
name|lastSlash
operator|+
literal|1
operator|==
name|prefix
operator|.
name|length
argument_list|()
condition|)
block|{
name|result
operator|=
name|coarseRefs
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|p
init|=
name|prefix
operator|.
name|substring
argument_list|(
name|lastSlash
operator|+
literal|1
argument_list|)
decl_stmt|;
name|result
operator|=
name|coarseRefs
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|result
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NonNull
DECL|method|exactRef (String... refs)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|exactRef
parameter_list|(
name|String
modifier|...
name|refs
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|refs
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|refs
control|)
block|{
name|Ref
name|ref
init|=
name|exactRef
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|firstExactRef (String... refs)
specifier|public
name|Ref
name|firstExactRef
parameter_list|(
name|String
modifier|...
name|refs
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|String
name|name
range|:
name|refs
control|)
block|{
name|Ref
name|ref
init|=
name|exactRef
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
name|ref
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|NonNull
DECL|method|getTipsWithSha1 (ObjectId id)
specifier|public
name|Set
argument_list|<
name|Ref
argument_list|>
name|getTipsWithSha1
parameter_list|(
name|ObjectId
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|Set
argument_list|<
name|Ref
argument_list|>
name|unfiltered
init|=
name|super
operator|.
name|getTipsWithSha1
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Ref
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|unfiltered
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|unfiltered
control|)
block|{
if|if
condition|(
name|exactRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

