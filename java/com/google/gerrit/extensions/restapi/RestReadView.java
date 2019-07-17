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
comment|/**  * RestView to read a resource without modification.  *  *<p>RestReadViews are invoked by the HTTP GET method.  *  * @param<R> type of resource the view reads.  */
end_comment

begin_interface
DECL|interface|RestReadView
specifier|public
interface|interface
name|RestReadView
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
extends|extends
name|RestView
argument_list|<
name|R
argument_list|>
block|{
comment|/**    * Process the view operation by reading from the resource.    *    *<p>The value of the returned response is automatically converted to JSON unless it is a {@link    * BinaryResult}.    *    *<p>The returned response defines the status code that is returned to the client. For    * RestReadViews this is usually {@code 200 OK}, but other 2XX or 3XX status codes are also    * possible (e.g. {@link Response.Redirect} can be returned for {@code 302 Found}).    *    *<p>Throwing a subclass of {@link RestApiException} results in a 4XX response to the client. For    * any other exception the client will get a {@code 500 Internal Server Error} response.    *    * @param resource resource to read    * @return response to return to the client    * @throws AuthException the caller is not permitted to access this view.    * @throws BadRequestException the request was incorrectly specified and cannot be handled by this    *     view.    * @throws ResourceConflictException the resource state does not permit this view to make the    *     changes at this time.    * @throws Exception the implementation of the view failed. The exception will be logged and HTTP    *     500 Internal Server Error will be returned to the client.    */
DECL|method|apply (R resource)
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|R
name|resource
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

