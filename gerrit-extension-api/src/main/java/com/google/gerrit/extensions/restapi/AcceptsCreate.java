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
comment|/**  * Optional interface for {@link RestCollection}.  *<p>  * Collections that implement this interface can accept a {@code PUT} or  * {@code POST} when the parse method throws {@link ResourceNotFoundException}.  */
end_comment

begin_interface
DECL|interface|AcceptsCreate
specifier|public
interface|interface
name|AcceptsCreate
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|>
block|{
comment|/**    * Handle creation of a child resource.    *    * @param parent parent collection handle.    * @param id id of the resource being created.    * @return a view to perform the creation. The create method must embed the id    *         into the newly returned view object, as it will not be passed.    * @throws RestApiException the view cannot be constructed.    */
DECL|method|create (P parent, String id)
parameter_list|<
name|I
parameter_list|>
name|RestModifyView
argument_list|<
name|P
argument_list|,
name|I
argument_list|>
name|create
parameter_list|(
name|P
name|parent
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
block|}
end_interface

end_unit

