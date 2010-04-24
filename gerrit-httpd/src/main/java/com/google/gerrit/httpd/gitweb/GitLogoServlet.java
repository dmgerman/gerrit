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
DECL|package|com.google.gerrit.httpd.gitweb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|gitweb
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
name|httpd
operator|.
name|GitWebConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|IO
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
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
name|HttpServlet
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
name|HttpServletRequest
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

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|Singleton
DECL|class|GitLogoServlet
class|class
name|GitLogoServlet
extends|extends
name|HttpServlet
block|{
DECL|field|raw
specifier|private
specifier|final
name|byte
index|[]
name|raw
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitLogoServlet (final GitWebConfig gitWebConfig)
name|GitLogoServlet
parameter_list|(
specifier|final
name|GitWebConfig
name|gitWebConfig
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|png
decl_stmt|;
if|if
condition|(
name|gitWebConfig
operator|.
name|getGitLogoPNG
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|png
operator|=
name|IO
operator|.
name|readFully
argument_list|(
name|gitWebConfig
operator|.
name|getGitLogoPNG
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|png
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|png
operator|=
literal|null
expr_stmt|;
block|}
name|raw
operator|=
name|png
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (final HttpServletRequest req, final HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|raw
operator|!=
literal|null
condition|)
block|{
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"image/png"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setContentLength
argument_list|(
name|raw
operator|.
name|length
argument_list|)
expr_stmt|;
specifier|final
name|ServletOutputStream
name|os
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|os
operator|.
name|write
argument_list|(
name|raw
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

