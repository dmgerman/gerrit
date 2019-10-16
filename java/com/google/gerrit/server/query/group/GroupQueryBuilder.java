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
DECL|package|com.google.gerrit.server.query.group
package|package
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
name|group
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|primitives
operator|.
name|Ints
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
name|index
operator|.
name|query
operator|.
name|LimitPredicate
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
name|QueryBuilder
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
name|index
operator|.
name|query
operator|.
name|QueryRequiresAuthException
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
name|AccountResolver
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
name|AccountResolver
operator|.
name|UnresolvableAccountException
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
name|GroupBackend
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
name|GroupBackends
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
name|GroupCache
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
name|group
operator|.
name|InternalGroup
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|ConfigInvalidException
import|;
end_import

begin_comment
comment|/** Parses a query string meant to be applied to group objects. */
end_comment

begin_class
DECL|class|GroupQueryBuilder
specifier|public
class|class
name|GroupQueryBuilder
extends|extends
name|QueryBuilder
argument_list|<
name|InternalGroup
argument_list|,
name|GroupQueryBuilder
argument_list|>
block|{
DECL|field|FIELD_UUID
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_UUID
init|=
literal|"uuid"
decl_stmt|;
DECL|field|FIELD_DESCRIPTION
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_DESCRIPTION
init|=
literal|"description"
decl_stmt|;
DECL|field|FIELD_INNAME
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_INNAME
init|=
literal|"inname"
decl_stmt|;
DECL|field|FIELD_NAME
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_NAME
init|=
literal|"name"
decl_stmt|;
DECL|field|FIELD_OWNER
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_OWNER
init|=
literal|"owner"
decl_stmt|;
DECL|field|FIELD_LIMIT
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_LIMIT
init|=
literal|"limit"
decl_stmt|;
DECL|field|mydef
specifier|private
specifier|static
specifier|final
name|QueryBuilder
operator|.
name|Definition
argument_list|<
name|InternalGroup
argument_list|,
name|GroupQueryBuilder
argument_list|>
name|mydef
init|=
operator|new
name|QueryBuilder
operator|.
name|Definition
argument_list|<>
argument_list|(
name|GroupQueryBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Arguments
specifier|public
specifier|static
class|class
name|Arguments
block|{
DECL|field|groupCache
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|groupBackend
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|accountResolver
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
annotation|@
name|Inject
DECL|method|Arguments (GroupCache groupCache, GroupBackend groupBackend, AccountResolver accountResolver)
name|Arguments
parameter_list|(
name|GroupCache
name|groupCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|AccountResolver
name|accountResolver
parameter_list|)
block|{
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
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
block|}
block|}
DECL|field|args
specifier|private
specifier|final
name|Arguments
name|args
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupQueryBuilder (Arguments args)
name|GroupQueryBuilder
parameter_list|(
name|Arguments
name|args
parameter_list|)
block|{
name|super
argument_list|(
name|mydef
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
annotation|@
name|Operator
DECL|method|uuid (String uuid)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|uuid
parameter_list|(
name|String
name|uuid
parameter_list|)
block|{
return|return
name|GroupPredicates
operator|.
name|uuid
argument_list|(
name|AccountGroup
operator|.
name|uuid
argument_list|(
name|uuid
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|description (String description)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|description
parameter_list|(
name|String
name|description
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|description
argument_list|)
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"description operator requires a value"
argument_list|)
throw|;
block|}
return|return
name|GroupPredicates
operator|.
name|description
argument_list|(
name|description
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|inname (String namePart)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|inname
parameter_list|(
name|String
name|namePart
parameter_list|)
block|{
if|if
condition|(
name|namePart
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|name
argument_list|(
name|namePart
argument_list|)
return|;
block|}
return|return
name|GroupPredicates
operator|.
name|inname
argument_list|(
name|namePart
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|name (String name)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|GroupPredicates
operator|.
name|name
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|owner (String owner)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|owner
parameter_list|(
name|String
name|owner
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
name|parseGroup
argument_list|(
name|owner
argument_list|)
decl_stmt|;
return|return
name|GroupPredicates
operator|.
name|owner
argument_list|(
name|groupUuid
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|is (String value)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|is
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
literal|"visibletoall"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|GroupPredicates
operator|.
name|isVisibleToAll
argument_list|()
return|;
block|}
throw|throw
name|error
argument_list|(
literal|"Invalid query"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|defaultField (String query)
specifier|protected
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|defaultField
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|QueryParseException
block|{
comment|// Adapt the capacity of this list when adding more default predicates.
name|List
argument_list|<
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|preds
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|preds
operator|.
name|add
argument_list|(
name|uuid
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|preds
operator|.
name|add
argument_list|(
name|name
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|preds
operator|.
name|add
argument_list|(
name|inname
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
name|preds
operator|.
name|add
argument_list|(
name|description
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|preds
operator|.
name|add
argument_list|(
name|owner
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
comment|// Skip.
block|}
return|return
name|Predicate
operator|.
name|or
argument_list|(
name|preds
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|member (String query)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|member
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accounts
init|=
name|parseAccount
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
argument_list|>
name|predicates
init|=
name|accounts
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|GroupPredicates
operator|::
name|member
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Predicate
operator|.
name|or
argument_list|(
name|predicates
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|subgroup (String query)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|subgroup
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
name|parseGroup
argument_list|(
name|query
argument_list|)
decl_stmt|;
return|return
name|GroupPredicates
operator|.
name|subgroup
argument_list|(
name|groupUuid
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|limit (String query)
specifier|public
name|Predicate
argument_list|<
name|InternalGroup
argument_list|>
name|limit
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|Integer
name|limit
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|limit
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"Invalid limit: "
operator|+
name|query
argument_list|)
throw|;
block|}
return|return
operator|new
name|LimitPredicate
argument_list|<>
argument_list|(
name|FIELD_LIMIT
argument_list|,
name|limit
argument_list|)
return|;
block|}
DECL|method|parseAccount (String nameOrEmail)
specifier|private
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|parseAccount
parameter_list|(
name|String
name|nameOrEmail
parameter_list|)
throws|throws
name|QueryParseException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
try|try
block|{
return|return
name|args
operator|.
name|accountResolver
operator|.
name|resolve
argument_list|(
name|nameOrEmail
argument_list|)
operator|.
name|asNonEmptyIdSet
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnresolvableAccountException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|isSelf
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|QueryRequiresAuthException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|QueryParseException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|parseGroup (String groupNameOrUuid)
specifier|private
name|AccountGroup
operator|.
name|UUID
name|parseGroup
parameter_list|(
name|String
name|groupNameOrUuid
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|group
init|=
name|args
operator|.
name|groupCache
operator|.
name|get
argument_list|(
name|AccountGroup
operator|.
name|uuid
argument_list|(
name|groupNameOrUuid
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|group
operator|.
name|get
argument_list|()
operator|.
name|getGroupUUID
argument_list|()
return|;
block|}
name|GroupReference
name|groupReference
init|=
name|GroupBackends
operator|.
name|findBestSuggestion
argument_list|(
name|args
operator|.
name|groupBackend
argument_list|,
name|groupNameOrUuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupReference
operator|==
literal|null
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"Group "
operator|+
name|groupNameOrUuid
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
return|return
name|groupReference
operator|.
name|getUUID
argument_list|()
return|;
block|}
block|}
end_class

end_unit

