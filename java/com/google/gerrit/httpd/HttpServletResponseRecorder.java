begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponseWrapper
import|;
end_import

begin_comment
comment|/**  * HttpServletResponse wrapper to allow response status code override.  *  *<p>Differently from the normal HttpServletResponse, this class allows multiple filters to  * override the response http status code.  */
end_comment

begin_class
DECL|class|HttpServletResponseRecorder
specifier|public
class|class
name|HttpServletResponseRecorder
extends|extends
name|HttpServletResponseWrapper
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|LOCATION_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|LOCATION_HEADER
init|=
literal|"Location"
decl_stmt|;
DECL|field|status
specifier|private
name|int
name|status
decl_stmt|;
DECL|field|statusMsg
specifier|private
name|String
name|statusMsg
init|=
literal|""
decl_stmt|;
DECL|field|headers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**    * Constructs a response recorder wrapping the given response.    *    * @param response the response to be wrapped    */
DECL|method|HttpServletResponseRecorder (HttpServletResponse response)
specifier|public
name|HttpServletResponseRecorder
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|)
block|{
name|super
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendError (int sc)
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|status
operator|=
name|sc
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendError (int sc, String msg)
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|status
operator|=
name|sc
expr_stmt|;
name|this
operator|.
name|statusMsg
operator|=
name|msg
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendRedirect (String location)
specifier|public
name|void
name|sendRedirect
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|status
operator|=
name|SC_MOVED_TEMPORARILY
expr_stmt|;
name|setHeader
argument_list|(
name|LOCATION_HEADER
argument_list|,
name|location
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setHeader (String name, String value)
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"all"
block|,
literal|"MissingOverride"
block|}
argument_list|)
comment|// @Override is omitted for backwards compatibility with servlet-api 2.5
comment|// TODO: Remove @SuppressWarnings and add @Override when Google upgrades
comment|//       to servlet-api 3.1
DECL|method|getStatus ()
specifier|public
name|int
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
DECL|method|play ()
name|void
name|play
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|status
operator|!=
literal|0
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Replaying %s %s"
argument_list|,
name|status
argument_list|,
name|statusMsg
argument_list|)
expr_stmt|;
if|if
condition|(
name|status
operator|==
name|SC_MOVED_TEMPORARILY
condition|)
block|{
name|super
operator|.
name|sendRedirect
argument_list|(
name|headers
operator|.
name|get
argument_list|(
name|LOCATION_HEADER
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|sendError
argument_list|(
name|status
argument_list|,
name|statusMsg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

