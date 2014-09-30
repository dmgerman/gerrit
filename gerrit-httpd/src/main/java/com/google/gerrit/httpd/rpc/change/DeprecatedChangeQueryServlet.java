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
DECL|package|com.google.gerrit.httpd.rpc.change
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
name|change
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|QueryProcessor
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
name|query
operator|.
name|change
operator|.
name|QueryProcessor
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
name|gson
operator|.
name|Gson
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
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|Singleton
DECL|class|DeprecatedChangeQueryServlet
specifier|public
class|class
name|DeprecatedChangeQueryServlet
extends|extends
name|HttpServlet
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
DECL|field|processor
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryProcessor
argument_list|>
name|processor
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeprecatedChangeQueryServlet (Provider<QueryProcessor> processor)
name|DeprecatedChangeQueryServlet
parameter_list|(
name|Provider
argument_list|<
name|QueryProcessor
argument_list|>
name|processor
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest req, HttpServletResponse rsp)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/json"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|QueryProcessor
name|p
init|=
name|processor
operator|.
name|get
argument_list|()
decl_stmt|;
name|OutputFormat
name|format
init|=
name|OutputFormat
operator|.
name|JSON
decl_stmt|;
try|try
block|{
name|format
operator|=
name|OutputFormat
operator|.
name|valueOf
argument_list|(
name|get
argument_list|(
name|req
argument_list|,
literal|"format"
argument_list|,
name|format
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|err
parameter_list|)
block|{
name|error
argument_list|(
name|rsp
argument_list|,
literal|"invalid format"
argument_list|)
expr_stmt|;
return|return;
block|}
switch|switch
condition|(
name|format
condition|)
block|{
case|case
name|JSON
case|:
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/json"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
break|break;
case|case
name|TEXT
case|:
name|rsp
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|setCharacterEncoding
argument_list|(
literal|"UTF-8"
argument_list|)
expr_stmt|;
break|break;
default|default:
name|error
argument_list|(
name|rsp
argument_list|,
literal|"invalid format"
argument_list|)
expr_stmt|;
return|return;
block|}
name|p
operator|.
name|setIncludeComments
argument_list|(
name|get
argument_list|(
name|req
argument_list|,
literal|"comments"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setIncludeCurrentPatchSet
argument_list|(
name|get
argument_list|(
name|req
argument_list|,
literal|"current-patch-set"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setIncludePatchSets
argument_list|(
name|get
argument_list|(
name|req
argument_list|,
literal|"patch-sets"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setIncludeApprovals
argument_list|(
name|get
argument_list|(
name|req
argument_list|,
literal|"all-approvals"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setIncludeFiles
argument_list|(
name|get
argument_list|(
name|req
argument_list|,
literal|"files"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setOutput
argument_list|(
name|rsp
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|format
argument_list|)
expr_stmt|;
name|p
operator|.
name|query
argument_list|(
name|get
argument_list|(
name|req
argument_list|,
literal|"q"
argument_list|,
literal|"status:open"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|error (HttpServletResponse rsp, String message)
specifier|private
specifier|static
name|void
name|error
parameter_list|(
name|HttpServletResponse
name|rsp
parameter_list|,
name|String
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|ErrorMessage
name|em
init|=
operator|new
name|ErrorMessage
argument_list|()
decl_stmt|;
name|em
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|ServletOutputStream
name|out
init|=
name|rsp
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
operator|new
name|Gson
argument_list|()
operator|.
name|toJson
argument_list|(
name|em
argument_list|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|get (HttpServletRequest req, String name, String val)
specifier|private
specifier|static
name|String
name|get
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|val
parameter_list|)
block|{
name|String
name|v
init|=
name|req
operator|.
name|getParameter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|val
return|;
block|}
return|return
name|v
return|;
block|}
DECL|method|get (HttpServletRequest req, String name, boolean val)
specifier|private
specifier|static
name|boolean
name|get
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|val
parameter_list|)
block|{
name|String
name|v
init|=
name|req
operator|.
name|getParameter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|val
return|;
block|}
return|return
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
return|;
block|}
DECL|class|ErrorMessage
specifier|public
specifier|static
class|class
name|ErrorMessage
block|{
DECL|field|type
specifier|public
specifier|final
name|String
name|type
init|=
literal|"error"
decl_stmt|;
DECL|field|message
specifier|public
name|String
name|message
decl_stmt|;
block|}
block|}
end_class

end_unit

