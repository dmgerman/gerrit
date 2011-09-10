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
name|gerrit
operator|.
name|server
operator|.
name|AccessPath
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
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_comment
comment|/** Set the WebSession to {@link AccessPath#GIT}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ProjectAccessPathFilter
specifier|public
class|class
name|ProjectAccessPathFilter
implements|implements
name|Filter
block|{
DECL|field|session
specifier|private
specifier|final
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|session
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectAccessPathFilter (final Provider<WebSession> session)
name|ProjectAccessPathFilter
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|session
operator|.
name|get
argument_list|()
operator|.
name|setAccessPath
argument_list|(
name|AccessPath
operator|.
name|GIT
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{   }
block|}
end_class

end_unit

