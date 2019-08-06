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
comment|/**  * RestView that supports accepting input and creating a resource.  *  *<p>The input must be supplied as JSON as the body of the HTTP request. Create views can be  * invoked by the HTTP methods {@code PUT} and {@code POST}.  *  *<p>The RestCreateView is only invoked when the parse method of the {@code RestCollection} throws  * {@link ResourceNotFoundException}, and hence the resource doesn't exist yet.  *  * @param<P> type of the parent resource  * @param<C> type of the child resource that is created  * @param<I> type of input the JSON parser will parse the input into.  */
end_comment

begin_interface
DECL|interface|RestCollectionCreateView
specifier|public
interface|interface
name|RestCollectionCreateView
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|,
name|C
extends|extends
name|RestResource
parameter_list|,
name|I
parameter_list|>
extends|extends
name|RestCollectionView
argument_list|<
name|P
argument_list|,
name|C
argument_list|,
name|I
argument_list|>
block|{
comment|/**    * Process the view operation by creating the resource.    *    *<p>The returned response defines the status code that is returned to the client. For    * RestCollectionCreateViews this is usually {@code 201 Created} because a resource is created,    * but other 2XX or 3XX status codes are also possible (e.g. {@link Response.Redirect} can be    * returned for {@code 302 Found}).    *    *<p>The value of the returned response is automatically converted to JSON unless it is a {@link    * BinaryResult}.    *    *<p>Throwing a subclass of {@link RestApiException} results in a 4XX response to the client. For    * any other exception the client will get a {@code 500 Internal Server Error} response.    *    * @param parentResource parent resource of the resource that should be created    * @param id the ID of the child resource that should be created    * @param input input after parsing from request.    * @return response to return to the client    * @throws RestApiException if the resource creation is rejected    * @throws Exception the implementation of the view failed. The exception will be logged and HTTP    *     500 Internal Server Error will be returned to the client.    */
DECL|method|apply (P parentResource, IdString id, I input)
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|P
name|parentResource
parameter_list|,
name|IdString
name|id
parameter_list|,
name|I
name|input
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

