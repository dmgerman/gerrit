begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_comment
comment|/** Default result for {@link UiAction}s with no JavaScript. */
end_comment

begin_class
DECL|class|UiResult
specifier|public
class|class
name|UiResult
block|{
comment|/** Display an alert message to the user. */
DECL|method|alert (String message)
specifier|public
specifier|static
name|UiResult
name|alert
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|UiResult
name|r
init|=
operator|new
name|UiResult
argument_list|()
decl_stmt|;
name|r
operator|.
name|alert
operator|=
name|message
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/** Launch URL in a new window. */
DECL|method|openUrl (URI uri)
specifier|public
specifier|static
name|UiResult
name|openUrl
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
return|return
name|openUrl
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/** Launch URL in a new window. */
DECL|method|openUrl (String url)
specifier|public
specifier|static
name|UiResult
name|openUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|UiResult
name|r
init|=
operator|new
name|UiResult
argument_list|()
decl_stmt|;
name|r
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|r
operator|.
name|openWindow
operator|=
literal|true
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/** Redirect the browser to a new URL. */
DECL|method|redirectUrl (String url)
specifier|public
specifier|static
name|UiResult
name|redirectUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|UiResult
name|r
init|=
operator|new
name|UiResult
argument_list|()
decl_stmt|;
name|r
operator|.
name|url
operator|=
name|url
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/** Alert the user with a message. */
DECL|field|alert
specifier|protected
name|String
name|alert
decl_stmt|;
comment|/** If present redirect browser to this URL. */
DECL|field|url
specifier|protected
name|String
name|url
decl_stmt|;
comment|/** When true open {@link #url} in a new tab/window. */
DECL|field|openWindow
specifier|protected
name|Boolean
name|openWindow
decl_stmt|;
DECL|method|UiResult ()
specifier|private
name|UiResult
parameter_list|()
block|{}
block|}
end_class

end_unit

