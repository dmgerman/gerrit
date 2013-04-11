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
DECL|package|com.google.gwtexpui.linker.server
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|linker
operator|.
name|server
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_comment
comment|/** A selection rule for a permutation property. */
end_comment

begin_interface
DECL|interface|Rule
specifier|public
interface|interface
name|Rule
block|{
comment|/** @return the property name, for example {@code "user.agent"}. */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**    * Compute the value for this property, given the current request.    *<p>    * This rule method must compute the proper permutation value, matching what    * the GWT module XML files use for this property. The rule may use any state    * available in the current servlet request.    *<p>    * If this method returns {@code null} server side selection will be aborted    * and selection for all properties will be handled on the client side by the    * {@code nocache.js} file.    *    * @param req the request    * @return the value for the property; null if the value cannot be determined    *         on the server side.    */
DECL|method|select (HttpServletRequest req)
specifier|public
name|String
name|select
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

