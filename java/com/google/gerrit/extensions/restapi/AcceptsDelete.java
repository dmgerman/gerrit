begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
comment|/**  * Optional interface for {@link RestCollection}.  *  *<p>This interface is used for 2 purposes:  *  *<ul>  *<li>to support {@code DELETE} directly on the collection itself  *<li>to support {@code DELETE} on a non-existing member of the collection (in order to create  *       that member)  *</ul>  *  *<p>This interface is not supported for root collections.  */
end_comment

begin_interface
DECL|interface|AcceptsDelete
specifier|public
interface|interface
name|AcceptsDelete
parameter_list|<
name|P
extends|extends
name|RestResource
parameter_list|>
block|{
comment|/**    * Handle    *    *<ul>    *<li>{@code DELETE} directly on the collection itself (in this case id is {@code null})    *<li>{@code DELETE} on a non-existing member of the collection (in this case id is not {@code    *       null})    *</ul>    *    * @param parent the collection    * @param id id of the non-existing collection member for which the {@code DELETE} request is    *     done, {@code null} if the {@code DELETE} request is done on the collection itself    * @return a view to handle the {@code DELETE} request    * @throws RestApiException the view cannot be constructed    */
DECL|method|delete (P parent, IdString id)
name|RestModifyView
argument_list|<
name|P
argument_list|,
name|?
argument_list|>
name|delete
parameter_list|(
name|P
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
block|}
end_interface

end_unit

