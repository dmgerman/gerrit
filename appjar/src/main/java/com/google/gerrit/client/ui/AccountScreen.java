begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
package|;
end_package

begin_comment
comment|/** A screen that requires the user to be signed-into their account. */
end_comment

begin_class
DECL|class|AccountScreen
specifier|public
class|class
name|AccountScreen
extends|extends
name|Screen
block|{
DECL|method|AccountScreen (final String heading)
specifier|public
name|AccountScreen
parameter_list|(
specifier|final
name|String
name|heading
parameter_list|)
block|{
name|super
argument_list|(
name|heading
argument_list|)
expr_stmt|;
name|setRequiresSignIn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|AccountScreen ()
specifier|public
name|AccountScreen
parameter_list|()
block|{
name|this
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

