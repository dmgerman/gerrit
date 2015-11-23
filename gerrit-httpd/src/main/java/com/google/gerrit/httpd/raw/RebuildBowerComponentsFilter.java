begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
package|;
end_package

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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
operator|.
name|BuckUtils
operator|.
name|BuildFailureException
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
name|launcher
operator|.
name|GerritLauncher
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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
DECL|class|RebuildBowerComponentsFilter
class|class
name|RebuildBowerComponentsFilter
implements|implements
name|Filter
block|{
DECL|field|TARGET
specifier|private
specifier|static
specifier|final
name|String
name|TARGET
init|=
literal|"//polygerrit-ui:polygerrit_components"
decl_stmt|;
DECL|field|gen
specifier|private
specifier|final
name|Path
name|gen
decl_stmt|;
DECL|field|root
specifier|private
specifier|final
name|Path
name|root
decl_stmt|;
DECL|field|zip
specifier|private
specifier|final
name|Path
name|zip
decl_stmt|;
DECL|method|RebuildBowerComponentsFilter (Path buckOut)
name|RebuildBowerComponentsFilter
parameter_list|(
name|Path
name|buckOut
parameter_list|)
block|{
name|gen
operator|=
name|buckOut
operator|.
name|resolve
argument_list|(
literal|"gen"
argument_list|)
expr_stmt|;
name|root
operator|=
name|buckOut
operator|.
name|getParent
argument_list|()
expr_stmt|;
name|zip
operator|=
name|BowerComponentsServlet
operator|.
name|getZipPath
argument_list|(
name|buckOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest sreq, ServletResponse sres, FilterChain chain)
specifier|public
specifier|synchronized
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|sreq
parameter_list|,
name|ServletResponse
name|sres
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|HttpServletResponse
name|res
init|=
operator|(
name|HttpServletResponse
operator|)
name|sres
decl_stmt|;
try|try
block|{
name|BuckUtils
operator|.
name|build
argument_list|(
name|root
argument_list|,
name|gen
argument_list|,
name|TARGET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BuildFailureException
name|e
parameter_list|)
block|{
name|BuckUtils
operator|.
name|displayFailure
argument_list|(
name|TARGET
argument_list|,
name|e
operator|.
name|why
argument_list|,
name|res
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|zip
argument_list|)
condition|)
block|{
name|String
name|msg
init|=
literal|"`buck build` did not produce "
operator|+
name|zip
operator|.
name|toAbsolutePath
argument_list|()
decl_stmt|;
name|BuckUtils
operator|.
name|displayFailure
argument_list|(
name|TARGET
argument_list|,
name|msg
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
name|GerritLauncher
operator|.
name|reloadZipFileSystem
argument_list|(
name|zip
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doFilter
argument_list|(
name|sreq
argument_list|,
name|sres
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

