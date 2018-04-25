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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|changes
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|DefaultInput
import|;
end_import

begin_comment
comment|/** Input for deleting a change message. */
end_comment

begin_class
DECL|class|DeleteChangeMessageInput
specifier|public
class|class
name|DeleteChangeMessageInput
block|{
comment|/** An optional reason for deleting the change message. */
DECL|field|reason
annotation|@
name|DefaultInput
specifier|public
name|String
name|reason
decl_stmt|;
DECL|method|DeleteChangeMessageInput ()
specifier|public
name|DeleteChangeMessageInput
parameter_list|()
block|{
name|this
operator|.
name|reason
operator|=
literal|""
expr_stmt|;
block|}
DECL|method|DeleteChangeMessageInput (String reason)
specifier|public
name|DeleteChangeMessageInput
parameter_list|(
name|String
name|reason
parameter_list|)
block|{
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
block|}
end_class

end_unit

