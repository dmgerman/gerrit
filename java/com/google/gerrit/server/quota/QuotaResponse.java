begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.quota
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|quota
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
name|Streams
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
name|Optional
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

begin_class
annotation|@
name|AutoValue
DECL|class|QuotaResponse
specifier|public
specifier|abstract
class|class
name|QuotaResponse
block|{
DECL|enum|Status
specifier|public
enum|enum
name|Status
block|{
comment|/** The quota requests succeeded. */
DECL|enumConstant|OK
name|OK
block|,
comment|/**      * The quota succeeded, but was a no-op because the plugin does not enforce this quota group      * (equivalent to OK, but relevant for debugging).      */
DECL|enumConstant|NO_OP
name|NO_OP
block|,
comment|/**      * The requested quota could not be allocated. This status code is not used to indicate      * processing failures as these are propagated as {@code RuntimeException}s.      */
DECL|enumConstant|ERROR
name|ERROR
block|;
DECL|method|isOk ()
specifier|public
name|boolean
name|isOk
parameter_list|()
block|{
return|return
name|this
operator|==
name|OK
return|;
block|}
DECL|method|isError ()
specifier|public
name|boolean
name|isError
parameter_list|()
block|{
return|return
name|this
operator|==
name|ERROR
return|;
block|}
block|}
DECL|method|ok ()
specifier|public
specifier|static
name|QuotaResponse
name|ok
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_QuotaResponse
operator|.
name|Builder
argument_list|()
operator|.
name|status
argument_list|(
name|Status
operator|.
name|OK
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|noOp ()
specifier|public
specifier|static
name|QuotaResponse
name|noOp
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_QuotaResponse
operator|.
name|Builder
argument_list|()
operator|.
name|status
argument_list|(
name|Status
operator|.
name|NO_OP
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|error (String message)
specifier|public
specifier|static
name|QuotaResponse
name|error
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_QuotaResponse
operator|.
name|Builder
argument_list|()
operator|.
name|status
argument_list|(
name|Status
operator|.
name|ERROR
argument_list|)
operator|.
name|message
argument_list|(
name|message
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|status ()
specifier|public
specifier|abstract
name|Status
name|status
parameter_list|()
function_decl|;
DECL|method|message ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|message
parameter_list|()
function_decl|;
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
DECL|method|status (Status status)
specifier|public
specifier|abstract
name|QuotaResponse
operator|.
name|Builder
name|status
parameter_list|(
name|Status
name|status
parameter_list|)
function_decl|;
DECL|method|message (String message)
specifier|public
specifier|abstract
name|QuotaResponse
operator|.
name|Builder
name|message
parameter_list|(
name|String
name|message
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|QuotaResponse
name|build
parameter_list|()
function_decl|;
block|}
annotation|@
name|AutoValue
DECL|class|Aggregated
specifier|public
specifier|abstract
specifier|static
class|class
name|Aggregated
block|{
DECL|method|create (Collection<QuotaResponse> responses)
specifier|public
specifier|static
name|Aggregated
name|create
parameter_list|(
name|Collection
argument_list|<
name|QuotaResponse
argument_list|>
name|responses
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_QuotaResponse_Aggregated
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|responses
argument_list|)
argument_list|)
return|;
block|}
DECL|method|responses ()
specifier|protected
specifier|abstract
name|ImmutableList
argument_list|<
name|QuotaResponse
argument_list|>
name|responses
parameter_list|()
function_decl|;
DECL|method|hasError ()
specifier|public
name|boolean
name|hasError
parameter_list|()
block|{
return|return
name|responses
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|r
lambda|->
name|r
operator|.
name|status
argument_list|()
operator|.
name|isError
argument_list|()
argument_list|)
return|;
block|}
DECL|method|all ()
specifier|public
name|ImmutableList
argument_list|<
name|QuotaResponse
argument_list|>
name|all
parameter_list|()
block|{
return|return
name|responses
argument_list|()
return|;
block|}
DECL|method|ok ()
specifier|public
name|ImmutableList
argument_list|<
name|QuotaResponse
argument_list|>
name|ok
parameter_list|()
block|{
return|return
name|responses
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|r
lambda|->
name|r
operator|.
name|status
argument_list|()
operator|.
name|isOk
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|error ()
specifier|public
name|ImmutableList
argument_list|<
name|QuotaResponse
argument_list|>
name|error
parameter_list|()
block|{
return|return
name|responses
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|r
lambda|->
name|r
operator|.
name|status
argument_list|()
operator|.
name|isError
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|errorMessage ()
specifier|public
name|String
name|errorMessage
parameter_list|()
block|{
return|return
name|error
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|QuotaResponse
operator|::
name|message
argument_list|)
operator|.
name|flatMap
argument_list|(
name|Streams
operator|::
name|stream
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|", "
argument_list|)
argument_list|)
return|;
block|}
DECL|method|throwOnError ()
specifier|public
name|void
name|throwOnError
parameter_list|()
throws|throws
name|QuotaException
block|{
name|String
name|errorMessage
init|=
name|errorMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|errorMessage
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|QuotaException
argument_list|(
name|errorMessage
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

