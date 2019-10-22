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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|Throwables
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
name|Response
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
name|RestApiException
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
name|RestModifyView
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
name|RestResource
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
name|git
operator|.
name|LockFailureException
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
name|update
operator|.
name|RetryHelper
operator|.
name|ActionType
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_class
DECL|class|RetryingRestModifyView
specifier|public
specifier|abstract
class|class
name|RetryingRestModifyView
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|,
name|I
parameter_list|,
name|O
parameter_list|>
implements|implements
name|RestModifyView
argument_list|<
name|R
argument_list|,
name|I
argument_list|>
block|{
DECL|field|retryHelper
specifier|private
specifier|final
name|RetryHelper
name|retryHelper
decl_stmt|;
DECL|method|RetryingRestModifyView (RetryHelper retryHelper)
specifier|protected
name|RetryingRestModifyView
parameter_list|(
name|RetryHelper
name|retryHelper
parameter_list|)
block|{
name|this
operator|.
name|retryHelper
operator|=
name|retryHelper
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (R resource, I input)
specifier|public
specifier|final
name|Response
argument_list|<
name|O
argument_list|>
name|apply
parameter_list|(
name|R
name|resource
parameter_list|,
name|I
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
name|AtomicReference
argument_list|<
name|String
argument_list|>
name|traceId
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|RetryHelper
operator|.
name|Options
name|retryOptions
init|=
name|RetryHelper
operator|.
name|options
argument_list|()
operator|.
name|caller
argument_list|(
name|getClass
argument_list|()
argument_list|)
operator|.
name|retryWithTrace
argument_list|(
name|t
lambda|->
operator|!
operator|(
name|t
operator|instanceof
name|RestApiException
operator|)
argument_list|)
operator|.
name|onAutoTrace
argument_list|(
name|traceId
operator|::
name|set
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|retryHelper
operator|.
name|execute
argument_list|(
name|ActionType
operator|.
name|REST_REQUEST
argument_list|,
parameter_list|()
lambda|->
name|applyImpl
argument_list|(
name|resource
argument_list|,
name|input
argument_list|)
argument_list|,
name|retryOptions
argument_list|,
name|t
lambda|->
block|{
lambda|if (t instanceof UpdateException
argument_list|)
block|{
name|t
operator|=
name|t
operator|.
name|getCause
argument_list|()
block|;                 }
return|return
name|t
operator|instanceof
name|LockFailureException
return|;
block|}
block|)
function|.traceId
parameter_list|(
function|traceId.get
parameter_list|()
block|)
class|;
end_class

begin_expr_stmt
unit|} catch
operator|(
name|Exception
name|e
operator|)
block|{
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
argument_list|,
name|RestApiException
operator|.
name|class
argument_list|)
block|;
return|return
name|Response
operator|.
expr|<
name|O
operator|>
name|internalServerError
argument_list|(
name|e
argument_list|)
operator|.
name|traceId
argument_list|(
name|traceId
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
end_expr_stmt

begin_function_decl
unit|}    protected
DECL|method|applyImpl (R resource, I input)
specifier|abstract
name|Response
argument_list|<
name|O
argument_list|>
name|applyImpl
parameter_list|(
name|R
name|resource
parameter_list|,
name|I
name|input
parameter_list|)
throws|throws
name|Exception
function_decl|;
end_function_decl

unit|}
end_unit

