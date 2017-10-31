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
comment|// limitations under the License.package com.google.gerrit.httpd.plugins;
end_comment

begin_package
DECL|package|com.google.gerrit.httpd.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|plugins
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
name|base
operator|.
name|Strings
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
name|HttpServletRequestWrapper
import|;
end_import

begin_class
DECL|class|ContextMapper
class|class
name|ContextMapper
block|{
DECL|field|PLUGINS_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PLUGINS_PREFIX
init|=
literal|"/plugins/"
decl_stmt|;
DECL|field|AUTHORIZED_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|AUTHORIZED_PREFIX
init|=
literal|"/a"
operator|+
name|PLUGINS_PREFIX
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|String
name|base
decl_stmt|;
DECL|field|authorizedBase
specifier|private
specifier|final
name|String
name|authorizedBase
decl_stmt|;
DECL|method|ContextMapper (String contextPath)
name|ContextMapper
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|base
operator|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|contextPath
argument_list|)
operator|+
name|PLUGINS_PREFIX
expr_stmt|;
name|authorizedBase
operator|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|contextPath
argument_list|)
operator|+
name|AUTHORIZED_PREFIX
expr_stmt|;
block|}
DECL|method|isAuthorizedCall (HttpServletRequest req)
specifier|private
specifier|static
name|boolean
name|isAuthorizedCall
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|req
operator|.
name|getServletPath
argument_list|()
argument_list|)
operator|&&
name|req
operator|.
name|getServletPath
argument_list|()
operator|.
name|startsWith
argument_list|(
name|AUTHORIZED_PREFIX
argument_list|)
return|;
block|}
DECL|method|create (HttpServletRequest req, String name)
name|HttpServletRequest
name|create
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
name|contextPath
init|=
operator|(
name|isAuthorizedCall
argument_list|(
name|req
argument_list|)
condition|?
name|authorizedBase
else|:
name|base
operator|)
operator|+
name|name
decl_stmt|;
return|return
operator|new
name|WrappedRequest
argument_list|(
name|req
argument_list|,
name|contextPath
argument_list|)
return|;
block|}
DECL|method|getFullPath (String name)
specifier|public
name|String
name|getFullPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|base
operator|+
name|name
return|;
block|}
DECL|class|WrappedRequest
specifier|private
specifier|static
class|class
name|WrappedRequest
extends|extends
name|HttpServletRequestWrapper
block|{
DECL|field|contextPath
specifier|private
specifier|final
name|String
name|contextPath
decl_stmt|;
DECL|field|pathInfo
specifier|private
specifier|final
name|String
name|pathInfo
decl_stmt|;
DECL|method|WrappedRequest (HttpServletRequest req, String contextPath)
specifier|private
name|WrappedRequest
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|String
name|contextPath
parameter_list|)
block|{
name|super
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
name|this
operator|.
name|pathInfo
operator|=
name|getRequestURI
argument_list|()
operator|.
name|substring
argument_list|(
name|contextPath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServletPath ()
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
annotation|@
name|Override
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
return|;
block|}
annotation|@
name|Override
DECL|method|getPathInfo ()
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
return|return
name|pathInfo
return|;
block|}
block|}
block|}
end_class

end_unit

