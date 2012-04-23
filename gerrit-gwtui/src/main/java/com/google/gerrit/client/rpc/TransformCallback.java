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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
import|;
end_import

begin_comment
comment|/** Transforms a value and passes it on to another callback. */
end_comment

begin_class
DECL|class|TransformCallback
specifier|public
specifier|abstract
class|class
name|TransformCallback
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
implements|implements
name|AsyncCallback
argument_list|<
name|I
argument_list|>
block|{
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
argument_list|<
name|O
argument_list|>
name|callback
decl_stmt|;
DECL|method|TransformCallback (AsyncCallback<O> callback)
specifier|protected
name|TransformCallback
parameter_list|(
name|AsyncCallback
argument_list|<
name|O
argument_list|>
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuccess (I result)
specifier|public
name|void
name|onSuccess
parameter_list|(
name|I
name|result
parameter_list|)
block|{
name|callback
operator|.
name|onSuccess
argument_list|(
name|transform
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable caught)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
DECL|method|transform (I result)
specifier|protected
specifier|abstract
name|O
name|transform
parameter_list|(
name|I
name|result
parameter_list|)
function_decl|;
block|}
end_class

end_unit

