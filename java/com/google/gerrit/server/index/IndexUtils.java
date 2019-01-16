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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|CHANGE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|LEGACY_ID
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|PROJECT
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
name|Function
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
name|ImmutableMap
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
name|index
operator|.
name|QueryOptions
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
name|project
operator|.
name|ProjectField
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
name|SitePaths
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
name|index
operator|.
name|account
operator|.
name|AccountField
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
name|index
operator|.
name|group
operator|.
name|GroupField
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
name|Set
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
name|ExecutionException
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
name|ConfigInvalidException
import|;
end_import

begin_class
DECL|class|IndexUtils
specifier|public
specifier|final
class|class
name|IndexUtils
block|{
DECL|field|CUSTOM_CHAR_MAPPING
specifier|public
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|CUSTOM_CHAR_MAPPING
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"_"
argument_list|,
literal|" "
argument_list|,
literal|"."
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
DECL|field|MAPPER
specifier|public
specifier|static
specifier|final
name|Function
argument_list|<
name|Exception
argument_list|,
name|IOException
argument_list|>
name|MAPPER
init|=
name|in
lambda|->
block|{
if|if
condition|(
name|in
operator|instanceof
name|IOException
condition|)
block|{
return|return
operator|(
name|IOException
operator|)
name|in
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|instanceof
name|ExecutionException
operator|&&
name|in
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IOException
condition|)
block|{
return|return
operator|(
name|IOException
operator|)
name|in
operator|.
name|getCause
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|IOException
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|setReady (SitePaths sitePaths, String name, int version, boolean ready)
specifier|public
specifier|static
name|void
name|setReady
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|version
parameter_list|,
name|boolean
name|ready
parameter_list|)
block|{
try|try
block|{
name|GerritIndexStatus
name|cfg
init|=
operator|new
name|GerritIndexStatus
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setReady
argument_list|(
name|name
argument_list|,
name|version
argument_list|,
name|ready
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getReady (SitePaths sitePaths, String name, int version)
specifier|public
specifier|static
name|boolean
name|getReady
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|version
parameter_list|)
block|{
try|try
block|{
name|GerritIndexStatus
name|cfg
init|=
operator|new
name|GerritIndexStatus
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
return|return
name|cfg
operator|.
name|getReady
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StorageException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|accountFields (QueryOptions opts)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|accountFields
parameter_list|(
name|QueryOptions
name|opts
parameter_list|)
block|{
return|return
name|accountFields
argument_list|(
name|opts
operator|.
name|fields
argument_list|()
argument_list|)
return|;
block|}
DECL|method|accountFields (Set<String> fields)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|accountFields
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|)
block|{
return|return
name|fields
operator|.
name|contains
argument_list|(
name|AccountField
operator|.
name|ID
operator|.
name|getName
argument_list|()
argument_list|)
condition|?
name|fields
else|:
name|Sets
operator|.
name|union
argument_list|(
name|fields
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|AccountField
operator|.
name|ID
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|changeFields (QueryOptions opts)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|changeFields
parameter_list|(
name|QueryOptions
name|opts
parameter_list|)
block|{
comment|// Ensure we request enough fields to construct a ChangeData. We need both
comment|// change ID and project, which can either come via the Change field or
comment|// separate fields.
name|Set
argument_list|<
name|String
argument_list|>
name|fs
init|=
name|opts
operator|.
name|fields
argument_list|()
decl_stmt|;
if|if
condition|(
name|fs
operator|.
name|contains
argument_list|(
name|CHANGE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// A Change is always sufficient.
return|return
name|fs
return|;
block|}
if|if
condition|(
name|fs
operator|.
name|contains
argument_list|(
name|PROJECT
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|fs
operator|.
name|contains
argument_list|(
name|LEGACY_ID
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|fs
return|;
block|}
return|return
name|Sets
operator|.
name|union
argument_list|(
name|fs
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|LEGACY_ID
operator|.
name|getName
argument_list|()
argument_list|,
name|PROJECT
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|groupFields (QueryOptions opts)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|groupFields
parameter_list|(
name|QueryOptions
name|opts
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|fs
init|=
name|opts
operator|.
name|fields
argument_list|()
decl_stmt|;
return|return
name|fs
operator|.
name|contains
argument_list|(
name|GroupField
operator|.
name|UUID
operator|.
name|getName
argument_list|()
argument_list|)
condition|?
name|fs
else|:
name|Sets
operator|.
name|union
argument_list|(
name|fs
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|GroupField
operator|.
name|UUID
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|describe (CurrentUser user)
specifier|public
specifier|static
name|String
name|describe
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
if|if
condition|(
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
return|return
name|user
operator|.
name|getAccountId
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
if|if
condition|(
name|user
operator|instanceof
name|SingleGroupUser
condition|)
block|{
return|return
literal|"group:"
operator|+
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|getKnownGroups
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|user
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|projectFields (QueryOptions opts)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|projectFields
parameter_list|(
name|QueryOptions
name|opts
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|fs
init|=
name|opts
operator|.
name|fields
argument_list|()
decl_stmt|;
return|return
name|fs
operator|.
name|contains
argument_list|(
name|ProjectField
operator|.
name|NAME
operator|.
name|getName
argument_list|()
argument_list|)
condition|?
name|fs
else|:
name|Sets
operator|.
name|union
argument_list|(
name|fs
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|ProjectField
operator|.
name|NAME
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|IndexUtils ()
specifier|private
name|IndexUtils
parameter_list|()
block|{
comment|// hide default constructor
block|}
block|}
end_class

end_unit

