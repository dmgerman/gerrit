begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|joining
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
name|ImmutableSetMultimap
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
name|Nullable
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
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
name|changes
operator|.
name|NotifyInfo
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
name|changes
operator|.
name|RecipientType
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
name|UnprocessableEntityException
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
name|Singleton
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
name|ArrayList
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
annotation|@
name|Singleton
DECL|class|NotifyResolver
specifier|public
class|class
name|NotifyResolver
block|{
annotation|@
name|AutoValue
DECL|class|Result
specifier|public
specifier|abstract
specifier|static
class|class
name|Result
block|{
DECL|method|none ()
specifier|public
specifier|static
name|Result
name|none
parameter_list|()
block|{
return|return
name|create
argument_list|(
name|NotifyHandling
operator|.
name|NONE
argument_list|)
return|;
block|}
DECL|method|all ()
specifier|public
specifier|static
name|Result
name|all
parameter_list|()
block|{
return|return
name|create
argument_list|(
name|NotifyHandling
operator|.
name|ALL
argument_list|)
return|;
block|}
DECL|method|create (NotifyHandling notifyHandling)
specifier|public
specifier|static
name|Result
name|create
parameter_list|(
name|NotifyHandling
name|notifyHandling
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|notifyHandling
argument_list|,
name|ImmutableSetMultimap
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
DECL|method|create ( NotifyHandling handling, ImmutableSetMultimap<RecipientType, Account.Id> recipients)
specifier|public
specifier|static
name|Result
name|create
parameter_list|(
name|NotifyHandling
name|handling
parameter_list|,
name|ImmutableSetMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|recipients
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_NotifyResolver_Result
argument_list|(
name|handling
argument_list|,
name|recipients
argument_list|)
return|;
block|}
DECL|method|handling ()
specifier|public
specifier|abstract
name|NotifyHandling
name|handling
parameter_list|()
function_decl|;
DECL|method|accounts ()
specifier|public
specifier|abstract
name|ImmutableSetMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accounts
parameter_list|()
function_decl|;
DECL|method|withHandling (NotifyHandling notifyHandling)
specifier|public
name|Result
name|withHandling
parameter_list|(
name|NotifyHandling
name|notifyHandling
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|notifyHandling
argument_list|,
name|accounts
argument_list|()
argument_list|)
return|;
block|}
DECL|method|shouldNotify ()
specifier|public
name|boolean
name|shouldNotify
parameter_list|()
block|{
return|return
operator|!
name|accounts
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|||
name|handling
argument_list|()
operator|.
name|compareTo
argument_list|(
name|NotifyHandling
operator|.
name|NONE
argument_list|)
operator|>
literal|0
return|;
block|}
block|}
DECL|field|accountResolver
specifier|private
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
annotation|@
name|Inject
DECL|method|NotifyResolver (AccountResolver accountResolver)
name|NotifyResolver
parameter_list|(
name|AccountResolver
name|accountResolver
parameter_list|)
block|{
name|this
operator|.
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
block|}
DECL|method|resolve ( NotifyHandling handling, @Nullable Map<RecipientType, NotifyInfo> notifyDetails)
specifier|public
name|Result
name|resolve
parameter_list|(
name|NotifyHandling
name|handling
parameter_list|,
annotation|@
name|Nullable
name|Map
argument_list|<
name|RecipientType
argument_list|,
name|NotifyInfo
argument_list|>
name|notifyDetails
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|requireNonNull
argument_list|(
name|handling
argument_list|)
expr_stmt|;
name|ImmutableSetMultimap
operator|.
name|Builder
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|b
init|=
name|ImmutableSetMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|notifyDetails
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|RecipientType
argument_list|,
name|NotifyInfo
argument_list|>
name|e
range|:
name|notifyDetails
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|b
operator|.
name|putAll
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|find
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|accounts
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Result
operator|.
name|create
argument_list|(
name|handling
argument_list|,
name|b
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|method|find (@ullable List<String> inputs)
specifier|private
name|ImmutableList
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|find
parameter_list|(
annotation|@
name|Nullable
name|List
argument_list|<
name|String
argument_list|>
name|inputs
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|inputs
operator|==
literal|null
operator|||
name|inputs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|r
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|problems
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|inputs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|nameOrEmail
range|:
name|inputs
control|)
block|{
try|try
block|{
name|r
operator|.
name|add
argument_list|(
name|accountResolver
operator|.
name|resolve
argument_list|(
name|nameOrEmail
argument_list|)
operator|.
name|asUnique
argument_list|()
operator|.
name|account
argument_list|()
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnprocessableEntityException
name|e
parameter_list|)
block|{
name|problems
operator|.
name|add
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|problems
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Some accounts that should be notified could not be resolved: "
operator|+
name|problems
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|"\n"
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|r
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

