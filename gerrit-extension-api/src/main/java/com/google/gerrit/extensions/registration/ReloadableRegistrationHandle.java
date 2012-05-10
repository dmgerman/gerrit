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
DECL|package|com.google.gerrit.extensions.registration
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Key
import|;
end_import

begin_interface
DECL|interface|ReloadableRegistrationHandle
specifier|public
interface|interface
name|ReloadableRegistrationHandle
parameter_list|<
name|T
parameter_list|>
extends|extends
name|RegistrationHandle
block|{
DECL|method|getKey ()
specifier|public
name|Key
argument_list|<
name|T
argument_list|>
name|getKey
parameter_list|()
function_decl|;
DECL|method|replace (Key<T> key, T item)
specifier|public
name|RegistrationHandle
name|replace
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|T
name|item
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

