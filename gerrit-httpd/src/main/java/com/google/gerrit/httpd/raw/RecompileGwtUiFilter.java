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
name|gwtexpui
operator|.
name|linker
operator|.
name|server
operator|.
name|UserAgentRule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|InputStream
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
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipFile
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
DECL|class|RecompileGwtUiFilter
class|class
name|RecompileGwtUiFilter
implements|implements
name|Filter
block|{
DECL|field|gwtuiRecompile
specifier|private
specifier|final
name|boolean
name|gwtuiRecompile
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"gerrit.disable-gwtui-recompile"
argument_list|)
operator|==
literal|null
decl_stmt|;
DECL|field|rule
specifier|private
specifier|final
name|UserAgentRule
name|rule
init|=
operator|new
name|UserAgentRule
argument_list|()
decl_stmt|;
DECL|field|uaInitialized
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|uaInitialized
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|unpackedWar
specifier|private
specifier|final
name|Path
name|unpackedWar
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
DECL|field|lastTarget
specifier|private
name|String
name|lastTarget
decl_stmt|;
DECL|field|lastTime
specifier|private
name|long
name|lastTime
decl_stmt|;
DECL|method|RecompileGwtUiFilter (Path buckOut, Path unpackedWar)
name|RecompileGwtUiFilter
parameter_list|(
name|Path
name|buckOut
parameter_list|,
name|Path
name|unpackedWar
parameter_list|)
block|{
name|this
operator|.
name|unpackedWar
operator|=
name|unpackedWar
expr_stmt|;
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
block|}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse res, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|res
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|String
name|pkg
init|=
literal|"gerrit-gwtui"
decl_stmt|;
name|String
name|target
init|=
literal|"ui_"
operator|+
name|rule
operator|.
name|select
argument_list|(
operator|(
name|HttpServletRequest
operator|)
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|gwtuiRecompile
operator|||
operator|!
name|uaInitialized
operator|.
name|contains
argument_list|(
name|target
argument_list|)
condition|)
block|{
name|String
name|rule
init|=
literal|"//"
operator|+
name|pkg
operator|+
literal|":"
operator|+
name|target
decl_stmt|;
comment|// TODO(davido): instead of assuming specific Buck's internal
comment|// target directory for gwt_binary() artifacts, ask Buck for
comment|// the location of user agent permutation GWT zip, e. g.:
comment|// $ buck targets --show_output //gerrit-gwtui:ui_safari \
comment|//    | awk '{print $2}'
name|String
name|child
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s/__gwt_binary_%s__"
argument_list|,
name|pkg
argument_list|,
name|target
argument_list|)
decl_stmt|;
name|File
name|zip
init|=
name|gen
operator|.
name|resolve
argument_list|(
name|child
argument_list|)
operator|.
name|resolve
argument_list|(
name|target
operator|+
literal|".zip"
argument_list|)
operator|.
name|toFile
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
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
name|rule
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
name|rule
argument_list|,
name|e
operator|.
name|why
argument_list|,
operator|(
name|HttpServletResponse
operator|)
name|res
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|target
operator|.
name|equals
argument_list|(
name|lastTarget
argument_list|)
operator|||
name|lastTime
operator|!=
name|zip
operator|.
name|lastModified
argument_list|()
condition|)
block|{
name|lastTarget
operator|=
name|target
expr_stmt|;
name|lastTime
operator|=
name|zip
operator|.
name|lastModified
argument_list|()
expr_stmt|;
name|unpack
argument_list|(
name|zip
argument_list|,
name|unpackedWar
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|uaInitialized
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|doFilter
argument_list|(
name|request
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
block|{   }
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{   }
DECL|method|unpack (File srcwar, File dstwar)
specifier|private
specifier|static
name|void
name|unpack
parameter_list|(
name|File
name|srcwar
parameter_list|,
name|File
name|dstwar
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ZipFile
name|zf
init|=
operator|new
name|ZipFile
argument_list|(
name|srcwar
argument_list|)
init|)
block|{
specifier|final
name|Enumeration
argument_list|<
name|?
extends|extends
name|ZipEntry
argument_list|>
name|e
init|=
name|zf
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
specifier|final
name|ZipEntry
name|ze
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|ze
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ze
operator|.
name|isDirectory
argument_list|()
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"WEB-INF/"
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"META-INF/"
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"com/google/gerrit/launcher/"
argument_list|)
operator|||
name|name
operator|.
name|equals
argument_list|(
literal|"Main.class"
argument_list|)
condition|)
block|{
continue|continue;
block|}
specifier|final
name|File
name|rawtmp
init|=
operator|new
name|File
argument_list|(
name|dstwar
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|mkdir
argument_list|(
name|rawtmp
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|rawtmp
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
try|try
init|(
name|FileOutputStream
name|rawout
init|=
operator|new
name|FileOutputStream
argument_list|(
name|rawtmp
argument_list|)
init|;
name|InputStream
name|in
operator|=
name|zf
operator|.
name|getInputStream
argument_list|(
name|ze
argument_list|)
init|)
block|{
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|rawout
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|mkdir (File dir)
specifier|private
specifier|static
name|void
name|mkdir
parameter_list|(
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|mkdir
argument_list|(
name|dir
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dir
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot mkdir "
operator|+
name|dir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
name|dir
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

