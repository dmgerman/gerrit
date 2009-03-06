begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|ui
operator|.
name|Screen
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|DOM
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|Element
import|;
end_import

begin_comment
comment|/** Displays an error message letting the user know the page doesn't exist. */
end_comment

begin_class
DECL|class|NotFoundScreen
specifier|public
class|class
name|NotFoundScreen
extends|extends
name|Screen
block|{
DECL|method|NotFoundScreen ()
specifier|public
name|NotFoundScreen
parameter_list|()
block|{
name|super
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|notFoundTitle
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Element
name|body
init|=
name|DOM
operator|.
name|createDiv
argument_list|()
decl_stmt|;
name|DOM
operator|.
name|setInnerText
argument_list|(
name|body
argument_list|,
name|Gerrit
operator|.
name|C
operator|.
name|notFoundBody
argument_list|()
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|appendChild
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

