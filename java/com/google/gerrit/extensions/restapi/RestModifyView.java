begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
package|;
end_package

begin_comment
comment|/**  * RestView that supports accepting input and changing a resource.  *  *<p>The input must be supplied as JSON as the body of the HTTP request. Modify views can be  * invoked by any HTTP method that is not {@code GET}, which includes {@code POST}, {@code PUT},  * {@code DELETE}.  *  * @param<R> type of the resource the view modifies.  * @param<I> type of input the JSON parser will parse the input into.  */
end_comment

begin_interface
DECL|interface|RestModifyView
specifier|public
interface|interface
name|RestModifyView
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|,
name|I
parameter_list|>
extends|extends
name|RestView
argument_list|<
name|R
argument_list|>
block|{
comment|/**    * Process the view operation by altering the resource.    *    *<p>The value of the returned response is automatically converted to JSON unless it is a {@link    * BinaryResult}.    *    *<p>The returned response defines the status code that is returned to the client. For    * RestModifyViews this is usually {@code 200 OK}, but other 2XX or 3XX status codes are also    * possible (e.g. {@code 202 Accepted} if a background task was scheduled, {@code 204 No Content}    * if no content is returned, {@code 302 Found} for a redirect).    *    *<p>Throwing a subclass of {@link RestApiException} results in a 4XX response to the client. For    * any other exception the client will get a {@code 500 Internal Server Error} response.    *    * @param resource resource to modify    * @param input input after parsing from request    * @return response to return to the client    * @throws AuthException the caller is not permitted to access this view.    * @throws BadRequestException the request was incorrectly specified and cannot be handled by this    *     view.    * @throws ResourceConflictException the resource state does not permit this view to make the    *     changes at this time.    * @throws Exception the implementation of the view failed. The exception will be logged and HTTP    *     500 Internal Server Error will be returned to the client.    */
DECL|method|apply (R resource, I input)
name|Response
argument_list|<
name|?
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
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|Exception
function_decl|;
block|}
end_interface

end_unit

