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
DECL|package|com.google.gerrit.acceptance.rest.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|util
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
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assert_
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_FORBIDDEN
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_INTERNAL_SERVER_ERROR
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_METHOD_NOT_ALLOWED
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_NOT_FOUND
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
name|gerrit
operator|.
name|acceptance
operator|.
name|RestResponse
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
name|acceptance
operator|.
name|RestSession
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
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/** Helper to execute REST API calls using the HTTP client. */
end_comment

begin_class
annotation|@
name|Ignore
DECL|class|RestApiCallHelper
specifier|public
class|class
name|RestApiCallHelper
block|{
comment|/** @see #execute(RestSession, List, BeforeRestCall, String...) */
DECL|method|execute (RestSession restSession, List<RestCall> restCalls, String... args)
specifier|public
specifier|static
name|void
name|execute
parameter_list|(
name|RestSession
name|restSession
parameter_list|,
name|List
argument_list|<
name|RestCall
argument_list|>
name|restCalls
parameter_list|,
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|execute
argument_list|(
name|restSession
argument_list|,
name|restCalls
argument_list|,
parameter_list|()
lambda|->
block|{}
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
comment|/** @see #execute(RestSession, List, BeforeRestCall, String...) */
DECL|method|execute ( RestSession restSession, List<RestCall> restCalls, BeforeRestCall beforeRestCall, String... args)
specifier|public
specifier|static
name|void
name|execute
parameter_list|(
name|RestSession
name|restSession
parameter_list|,
name|List
argument_list|<
name|RestCall
argument_list|>
name|restCalls
parameter_list|,
name|BeforeRestCall
name|beforeRestCall
parameter_list|,
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|RestCall
name|restCall
range|:
name|restCalls
control|)
block|{
name|beforeRestCall
operator|.
name|run
argument_list|()
expr_stmt|;
name|execute
argument_list|(
name|restSession
argument_list|,
name|restCall
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * This method sends a request to a given REST endpoint and verifies that an implementation is    * found (no '404 Not Found' response) and that the request doesn't fail (no '500 Internal Server    * Error' response). It doesn't verify that the REST endpoint works correctly. This is okay since    * the purpose of the test is only to verify that the REST endpoint implementations are correctly    * bound.    */
DECL|method|execute (RestSession restSession, RestCall restCall, String... args)
specifier|public
specifier|static
name|void
name|execute
parameter_list|(
name|RestSession
name|restSession
parameter_list|,
name|RestCall
name|restCall
parameter_list|,
name|String
modifier|...
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|method
init|=
name|restCall
operator|.
name|httpMethod
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|restCall
operator|.
name|uri
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|RestResponse
name|response
decl_stmt|;
switch|switch
condition|(
name|restCall
operator|.
name|httpMethod
argument_list|()
condition|)
block|{
case|case
name|GET
case|:
name|response
operator|=
name|restSession
operator|.
name|get
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|PUT
case|:
name|response
operator|=
name|restSession
operator|.
name|put
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|POST
case|:
name|response
operator|=
name|restSession
operator|.
name|post
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
case|case
name|DELETE
case|:
name|response
operator|=
name|restSession
operator|.
name|delete
argument_list|(
name|uri
argument_list|)
expr_stmt|;
break|break;
default|default:
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"unsupported method: %s"
argument_list|,
name|restCall
operator|.
name|httpMethod
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
name|int
name|status
init|=
name|response
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|response
operator|.
name|hasContent
argument_list|()
condition|?
name|response
operator|.
name|getEntityContent
argument_list|()
else|:
literal|""
decl_stmt|;
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s %s returned %d: %s"
argument_list|,
name|method
argument_list|,
name|uri
argument_list|,
name|status
argument_list|,
name|body
argument_list|)
decl_stmt|;
if|if
condition|(
name|restCall
operator|.
name|expectedResponseCode
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|assertWithMessage
argument_list|(
name|msg
argument_list|)
operator|.
name|that
argument_list|(
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|restCall
operator|.
name|expectedResponseCode
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|restCall
operator|.
name|expectedMessage
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|assertWithMessage
argument_list|(
name|msg
argument_list|)
operator|.
name|that
argument_list|(
name|body
argument_list|)
operator|.
name|contains
argument_list|(
name|restCall
operator|.
name|expectedMessage
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|assertWithMessage
argument_list|(
name|msg
argument_list|)
operator|.
name|that
argument_list|(
name|status
argument_list|)
operator|.
name|isNotIn
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|SC_FORBIDDEN
argument_list|,
name|SC_NOT_FOUND
argument_list|,
name|SC_METHOD_NOT_ALLOWED
argument_list|)
argument_list|)
expr_stmt|;
name|assertWithMessage
argument_list|(
name|msg
argument_list|)
operator|.
name|that
argument_list|(
name|status
argument_list|)
operator|.
name|isLessThan
argument_list|(
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|FunctionalInterface
DECL|interface|BeforeRestCall
specifier|public
interface|interface
name|BeforeRestCall
block|{
DECL|method|run ()
name|void
name|run
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
block|}
end_class

end_unit
