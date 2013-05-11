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
DECL|package|com.google.gerrit.httpd.rpc
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
package|;
end_package

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

begin_class
DECL|class|AuditedHttpServletResponse
class|class
name|AuditedHttpServletResponse
extends|extends
name|HttpServletResponseWrapper
implements|implements
name|HttpServletResponse
block|{
DECL|field|status
specifier|private
name|int
name|status
decl_stmt|;
DECL|method|AuditedHttpServletResponse (HttpServletResponse response)
name|AuditedHttpServletResponse
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
annotation|@
name|Override
DECL|method|setStatus (int sc)
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|)
block|{
name|super
operator|.
name|setStatus
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|sc
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|setStatus (int sc, String sm)
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|sm
parameter_list|)
block|{
name|super
operator|.
name|setStatus
argument_list|(
name|sc
argument_list|,
name|sm
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|sc
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
name|super
operator|.
name|sendError
argument_list|(
name|sc
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|sendError
argument_list|(
name|sc
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|sc
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
name|super
operator|.
name|sendRedirect
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|this
operator|.
name|status
operator|=
name|SC_MOVED_TEMPORARILY
expr_stmt|;
block|}
block|}
end_class

end_unit

