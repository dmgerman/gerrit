begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
package|;
end_package

begin_comment
comment|/** AsyncCallback supplied with HTTP response headers. */
end_comment

begin_interface
DECL|interface|HttpCallback
specifier|public
interface|interface
name|HttpCallback
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|onSuccess (HttpResponse<T> result)
name|void
name|onSuccess
parameter_list|(
name|HttpResponse
argument_list|<
name|T
argument_list|>
name|result
parameter_list|)
function_decl|;
DECL|method|onFailure (Throwable caught)
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

