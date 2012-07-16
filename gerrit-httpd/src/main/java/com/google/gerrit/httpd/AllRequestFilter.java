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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|servlet
operator|.
name|ServletModule
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
name|Iterator
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
comment|/** Filters all HTTP requests passing through the server. */
end_comment

begin_class
DECL|class|AllRequestFilter
specifier|public
specifier|abstract
class|class
name|AllRequestFilter
implements|implements
name|Filter
block|{
DECL|method|module ()
specifier|public
specifier|static
name|ServletModule
name|module
parameter_list|()
block|{
return|return
operator|new
name|ServletModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
name|DynamicSet
operator|.
name|setOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|AllRequestFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|filter
argument_list|(
literal|"/*"
argument_list|)
operator|.
name|through
argument_list|(
name|FilterProxy
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Singleton
DECL|class|FilterProxy
specifier|static
class|class
name|FilterProxy
implements|implements
name|Filter
block|{
DECL|field|filters
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|AllRequestFilter
argument_list|>
name|filters
decl_stmt|;
annotation|@
name|Inject
DECL|method|FilterProxy (DynamicSet<AllRequestFilter> filters)
name|FilterProxy
parameter_list|(
name|DynamicSet
argument_list|<
name|AllRequestFilter
argument_list|>
name|filters
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|=
name|filters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest req, ServletResponse res, final FilterChain last)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|,
specifier|final
name|FilterChain
name|last
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
specifier|final
name|Iterator
argument_list|<
name|AllRequestFilter
argument_list|>
name|itr
init|=
name|filters
operator|.
name|iterator
argument_list|()
decl_stmt|;
operator|new
name|FilterChain
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|req
parameter_list|,
name|ServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
if|if
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|itr
operator|.
name|next
argument_list|()
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|last
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
block|}
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|res
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
throws|throws
name|ServletException
block|{     }
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{     }
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
throws|throws
name|ServletException
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

