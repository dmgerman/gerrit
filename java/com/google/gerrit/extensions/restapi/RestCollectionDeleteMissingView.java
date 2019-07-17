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
comment|/**  * RestView that supports accepting input and deleting a resource that is missing.  *  *<p>The RestDeleteMissingView solely exists to support a special case for creating a change edit  * by deleting a path in the non-existing change edit. This interface should not be used for new  * REST API's.  *  *<p>The input must be supplied as JSON as the body of the HTTP request. Delete views can be  * invoked by the HTTP method {@code DELETE}.  *  *<p>The RestDeleteMissingView is only invoked when the parse method of the {@code RestCollection}  * throws {@link ResourceNotFoundException}, and hence the resource doesn't exist yet.  *  * @param<P> type of the parent resource  * @param<C> type of the child resource that id deleted  * @param<I> type of input the JSON parser will parse the input into.  */
end_comment

begin_interface
DECL|interface|RestCollectionDeleteMissingView
specifier|public
interface|interface
name|RestCollectionDeleteMissingView
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
comment|/**    * Process the view operation by deleting the resource.    *    *<p>The returned response defines the status code that is returned to the client. For    * RestCollectionDeleteMissingViews this is usually {@code 204 No Content} because a resource is    * deleted, but other 2XX or 3XX status codes are also possible (e.g. {@code 200 OK}, {@code 302    * Found} for a redirect).    *    *<p>The returned response usually does not have any value (status code {@code 204 No Content}).    * If a value in the returned response is set it is automatically converted to JSON unless it is a    * {@link BinaryResult}.    *    *<p>Throwing a subclass of {@link RestApiException} results in a 4XX response to the client. For    * any other exception the client will get a {@code 500 Internal Server Error} response.    *    * @param parentResource parent resource of the resource that should be deleted    * @param id the ID of the child resource that should be deleted    * @param input input after parsing from request    * @return response to return to the client    * @throws RestApiException if the resource creation is rejected    * @throws Exception the implementation of the view failed. The exception will be logged and HTTP    *     500 Internal Server Error will be returned to the client.    */
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

