begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
operator|.
name|RestApiServlet
operator|.
name|JSON_MAGIC
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
operator|.
name|RestApiServlet
operator|.
name|SC_UNPROCESSABLE_ENTITY
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_BAD_REQUEST
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_CONFLICT
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_CREATED
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_FORBIDDEN
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_METHOD_NOT_ALLOWED
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_MOVED_TEMPORARILY
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_NO_CONTENT
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_OK
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_PRECONDITION_FAILED
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_class
DECL|class|RestResponse
specifier|public
class|class
name|RestResponse
extends|extends
name|HttpResponse
block|{
DECL|method|RestResponse (org.apache.http.HttpResponse response)
name|RestResponse
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
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
DECL|method|getReader ()
specifier|public
name|Reader
name|getReader
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|IOException
block|{
if|if
condition|(
name|reader
operator|==
literal|null
operator|&&
name|response
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reader
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
expr_stmt|;
name|reader
operator|.
name|skip
argument_list|(
name|JSON_MAGIC
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
return|return
name|reader
return|;
block|}
DECL|method|assertStatus (int status)
specifier|public
name|void
name|assertStatus
parameter_list|(
name|int
name|status
parameter_list|)
throws|throws
name|Exception
block|{
name|assertWithMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Expected status code %d"
argument_list|,
name|status
argument_list|)
argument_list|)
operator|.
name|that
argument_list|(
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
DECL|method|assertOK ()
specifier|public
name|void
name|assertOK
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_OK
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNotFound ()
specifier|public
name|void
name|assertNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
DECL|method|assertConflict ()
specifier|public
name|void
name|assertConflict
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_CONFLICT
argument_list|)
expr_stmt|;
block|}
DECL|method|assertForbidden ()
specifier|public
name|void
name|assertForbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_FORBIDDEN
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNoContent ()
specifier|public
name|void
name|assertNoContent
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_NO_CONTENT
argument_list|)
expr_stmt|;
block|}
DECL|method|assertBadRequest ()
specifier|public
name|void
name|assertBadRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_BAD_REQUEST
argument_list|)
expr_stmt|;
block|}
DECL|method|assertUnprocessableEntity ()
specifier|public
name|void
name|assertUnprocessableEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_UNPROCESSABLE_ENTITY
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMethodNotAllowed ()
specifier|public
name|void
name|assertMethodNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_METHOD_NOT_ALLOWED
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCreated ()
specifier|public
name|void
name|assertCreated
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_CREATED
argument_list|)
expr_stmt|;
block|}
DECL|method|assertPreconditionFailed ()
specifier|public
name|void
name|assertPreconditionFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_PRECONDITION_FAILED
argument_list|)
expr_stmt|;
block|}
DECL|method|assertTemporaryRedirect (String path)
specifier|public
name|void
name|assertTemporaryRedirect
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
name|assertStatus
argument_list|(
name|SC_MOVED_TEMPORARILY
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|getHeader
argument_list|(
literal|"Location"
argument_list|)
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

