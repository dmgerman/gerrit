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
comment|/**  * RestView on a RestCollection that supports accepting input.  *  *<p>The input must be supplied as JSON as the body of the HTTP request. RestCollectionModifyViews  * can be invoked by the HTTP methods {@code POST} and {@code DELETE} ({@code DELETE} is only  * supported on child collections).  *  * @param<P> type of the parent resource  * @param<C> type of the child resource  * @param<I> type of input the JSON parser will parse the input into.  */
end_comment

begin_interface
DECL|interface|RestCollectionModifyView
specifier|public
interface|interface
name|RestCollectionModifyView
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
comment|/**    * Process the modification on the collection resource.    *    *<p>The value of the returned response is automatically converted to JSON unless it is a {@link    * BinaryResult}.    *    *<p>The returned response defines the status code that is returned to the client. For    * RestCollectionModifyViews this is usually {@code 200 OK}, but other 2XX or 3XX status codes are    * also possible (e.g. {@code 201 Created} if a resource was created, {@code 202 Accepted} if a    * background task was scheduled, {@code 204 No Content} if no content is returned, {@code 302    * Found} for a redirect).    *    *<p>Throwing a subclass of {@link RestApiException} results in a 4XX response to the client. For    * any other exception the client will get a {@code 500 Internal Server Error} response.    *    * @param parentResource the collection resource on which the modification is done    * @return response to return to the client    * @throws Exception the implementation of the view failed. The exception will be logged and HTTP    *     500 Internal Server Error will be returned to the client.    */
DECL|method|apply (P parentResource, I input)
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|P
name|parentResource
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

