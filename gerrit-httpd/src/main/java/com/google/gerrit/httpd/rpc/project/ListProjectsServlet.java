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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|project
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
name|RestApiServlet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|OutputFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|ListProjects
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
name|Provider
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
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|Singleton
DECL|class|ListProjectsServlet
specifier|public
class|class
name|ListProjectsServlet
extends|extends
name|RestApiServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|paramParser
specifier|private
specifier|final
name|ParameterParser
name|paramParser
decl_stmt|;
DECL|field|factory
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListProjects
argument_list|>
name|factory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListProjectsServlet (ParameterParser paramParser, Provider<ListProjects> ls)
name|ListProjectsServlet
parameter_list|(
name|ParameterParser
name|paramParser
parameter_list|,
name|Provider
argument_list|<
name|ListProjects
argument_list|>
name|ls
parameter_list|)
block|{
name|this
operator|.
name|paramParser
operator|=
name|paramParser
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|ls
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest req, HttpServletResponse res)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
name|ListProjects
name|impl
init|=
name|factory
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|acceptsJson
argument_list|(
name|req
argument_list|)
condition|)
block|{
name|impl
operator|.
name|setFormat
argument_list|(
name|OutputFormat
operator|.
name|JSON_COMPACT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|paramParser
operator|.
name|parse
argument_list|(
name|impl
argument_list|,
name|req
argument_list|,
name|res
argument_list|)
condition|)
block|{
name|ByteArrayOutputStream
name|buf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|impl
operator|.
name|getFormat
argument_list|()
operator|.
name|isJson
argument_list|()
condition|)
block|{
name|res
operator|.
name|setContentType
argument_list|(
name|JSON_TYPE
argument_list|)
expr_stmt|;
name|buf
operator|.
name|write
argument_list|(
name|JSON_MAGIC
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|res
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
block|}
name|impl
operator|.
name|display
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCharacterEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|send
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|buf
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

