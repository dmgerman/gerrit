begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.prettify.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
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
name|resources
operator|.
name|client
operator|.
name|TextResource
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
name|Composite
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
name|NamedFrame
import|;
end_import

begin_comment
comment|/**  * Creates a private JavaScript environment, typically inside an IFrame.  *<p>  * Instances must be created through {@code GWT.create(PrivateScopeImpl.class)}.  * A scope must remain attached to the primary document for its entire life.  * Behavior is undefined if a scope is detached and attached again later. It is  * best to attach the scope with {@code RootPanel.get().add(scope)} as soon as  * it has been created.  */
end_comment

begin_class
DECL|class|PrivateScopeImpl
specifier|public
class|class
name|PrivateScopeImpl
extends|extends
name|Composite
block|{
DECL|field|scopeId
specifier|private
specifier|static
name|int
name|scopeId
decl_stmt|;
DECL|field|scopeName
specifier|protected
specifier|final
name|String
name|scopeName
decl_stmt|;
DECL|method|PrivateScopeImpl ()
specifier|public
name|PrivateScopeImpl
parameter_list|()
block|{
name|scopeName
operator|=
name|nextScopeName
argument_list|()
expr_stmt|;
name|NamedFrame
name|frame
init|=
operator|new
name|NamedFrame
argument_list|(
name|scopeName
argument_list|)
decl_stmt|;
name|frame
operator|.
name|setUrl
argument_list|(
literal|"javascript:''"
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|frame
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|compile (TextResource js)
specifier|public
name|void
name|compile
parameter_list|(
name|TextResource
name|js
parameter_list|)
block|{
name|eval
argument_list|(
name|js
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|eval (String js)
specifier|public
name|void
name|eval
parameter_list|(
name|String
name|js
parameter_list|)
block|{
name|nativeEval
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|js
argument_list|)
expr_stmt|;
block|}
DECL|method|getContext ()
specifier|public
name|JavaScriptObject
name|getContext
parameter_list|()
block|{
return|return
name|nativeGetContext
argument_list|(
name|scopeName
argument_list|)
return|;
block|}
DECL|method|nextScopeName ()
specifier|private
specifier|static
name|String
name|nextScopeName
parameter_list|()
block|{
return|return
literal|"_PrivateScope"
operator|+
operator|(
operator|++
name|scopeId
operator|)
return|;
block|}
DECL|method|nativeEval (JavaScriptObject ctx, String js)
specifier|private
specifier|static
specifier|native
name|void
name|nativeEval
parameter_list|(
name|JavaScriptObject
name|ctx
parameter_list|,
name|String
name|js
parameter_list|)
comment|/*-{ ctx.eval(js); }-*/
function_decl|;
DECL|method|nativeGetContext (String scopeName)
specifier|private
specifier|static
specifier|native
name|JavaScriptObject
name|nativeGetContext
parameter_list|(
name|String
name|scopeName
parameter_list|)
comment|/*-{ return $wnd[scopeName]; }-*/
function_decl|;
block|}
end_class

end_unit

