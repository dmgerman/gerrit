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
name|Gerrit
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
name|ui
operator|.
name|FlowPanel
import|;
end_import

begin_class
DECL|class|Screen
specifier|public
class|class
name|Screen
extends|extends
name|FlowPanel
block|{
DECL|field|requiresSignIn
specifier|private
name|boolean
name|requiresSignIn
decl_stmt|;
DECL|method|Screen (final String heading)
specifier|public
name|Screen
parameter_list|(
specifier|final
name|String
name|heading
parameter_list|)
block|{
name|setStyleName
argument_list|(
literal|"gerrit-Screen"
argument_list|)
expr_stmt|;
specifier|final
name|Element
name|h
init|=
name|DOM
operator|.
name|createElement
argument_list|(
literal|"h1"
argument_list|)
decl_stmt|;
name|DOM
operator|.
name|setInnerText
argument_list|(
name|h
argument_list|,
name|heading
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|appendChild
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|h
argument_list|)
expr_stmt|;
block|}
comment|/** Set whether or not {@link Gerrit#isSignedIn()} must be true. */
DECL|method|setRequiresSignIn (final boolean b)
specifier|public
name|void
name|setRequiresSignIn
parameter_list|(
specifier|final
name|boolean
name|b
parameter_list|)
block|{
name|requiresSignIn
operator|=
name|b
expr_stmt|;
block|}
comment|/** Does {@link Gerrit#isSignedIn()} have to be true to be on this screen? */
DECL|method|isRequiresSignIn ()
specifier|public
name|boolean
name|isRequiresSignIn
parameter_list|()
block|{
return|return
name|requiresSignIn
return|;
block|}
block|}
end_class

end_unit

