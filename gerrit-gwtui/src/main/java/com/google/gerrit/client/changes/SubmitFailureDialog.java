begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|ErrorDialog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonException
import|;
end_import

begin_class
DECL|class|SubmitFailureDialog
specifier|public
class|class
name|SubmitFailureDialog
extends|extends
name|ErrorDialog
block|{
DECL|method|isConflict (Throwable err)
specifier|public
specifier|static
name|boolean
name|isConflict
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
return|return
name|err
operator|instanceof
name|RemoteJsonException
operator|&&
literal|409
operator|==
operator|(
operator|(
name|RemoteJsonException
operator|)
name|err
operator|)
operator|.
name|getCode
argument_list|()
return|;
block|}
DECL|method|SubmitFailureDialog (String msg)
specifier|public
name|SubmitFailureDialog
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|msg
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|wikify
argument_list|()
argument_list|)
expr_stmt|;
name|setText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|submitFailed
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

