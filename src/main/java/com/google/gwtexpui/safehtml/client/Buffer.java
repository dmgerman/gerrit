begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc.
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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
package|;
end_package

begin_interface
DECL|interface|Buffer
interface|interface
name|Buffer
block|{
DECL|method|append (boolean v)
name|void
name|append
parameter_list|(
name|boolean
name|v
parameter_list|)
function_decl|;
DECL|method|append (char v)
name|void
name|append
parameter_list|(
name|char
name|v
parameter_list|)
function_decl|;
DECL|method|append (int v)
name|void
name|append
parameter_list|(
name|int
name|v
parameter_list|)
function_decl|;
DECL|method|append (long v)
name|void
name|append
parameter_list|(
name|long
name|v
parameter_list|)
function_decl|;
DECL|method|append (float v)
name|void
name|append
parameter_list|(
name|float
name|v
parameter_list|)
function_decl|;
DECL|method|append (double v)
name|void
name|append
parameter_list|(
name|double
name|v
parameter_list|)
function_decl|;
DECL|method|append (String v)
name|void
name|append
parameter_list|(
name|String
name|v
parameter_list|)
function_decl|;
DECL|method|toString ()
name|String
name|toString
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

