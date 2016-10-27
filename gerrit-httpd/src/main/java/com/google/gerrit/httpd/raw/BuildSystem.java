begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|common
operator|.
name|escape
operator|.
name|Escaper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|html
operator|.
name|HtmlEscapers
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
name|server
operator|.
name|CacheHeaders
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
name|RawParseUtils
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
name|PrintWriter
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
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_interface
DECL|interface|BuildSystem
specifier|public
interface|interface
name|BuildSystem
block|{
comment|// Represents a label in either buck or bazel.
DECL|class|Label
class|class
name|Label
block|{
DECL|field|pkg
specifier|protected
specifier|final
name|String
name|pkg
decl_stmt|;
DECL|field|name
specifier|protected
specifier|final
name|String
name|name
decl_stmt|;
comment|// Regrettably, buck confounds rule names and artifact names,
comment|// and so we have to lug this along. Non-null only for Buck; in that case,
comment|// holds the path relative to buck-out/gen/
DECL|field|artifact
specifier|protected
specifier|final
name|String
name|artifact
decl_stmt|;
DECL|method|fullName ()
specifier|public
name|String
name|fullName
parameter_list|()
block|{
return|return
literal|"//"
operator|+
name|pkg
operator|+
literal|":"
operator|+
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|s
init|=
name|fullName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|artifact
argument_list|)
condition|)
block|{
name|s
operator|+=
literal|"("
operator|+
name|artifact
operator|+
literal|")"
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
comment|// Label in Buck style.
DECL|method|Label (String pkg, String name, String artifact)
name|Label
parameter_list|(
name|String
name|pkg
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|pkg
operator|=
name|pkg
expr_stmt|;
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
comment|// Label in Bazel style.
DECL|method|Label (String pkg, String name)
name|Label
parameter_list|(
name|String
name|pkg
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|pkg
argument_list|,
name|name
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|BuildFailureException
class|class
name|BuildFailureException
extends|extends
name|Exception
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
DECL|field|why
specifier|final
name|byte
index|[]
name|why
decl_stmt|;
DECL|method|BuildFailureException (byte[] why)
name|BuildFailureException
parameter_list|(
name|byte
index|[]
name|why
parameter_list|)
block|{
name|this
operator|.
name|why
operator|=
name|why
expr_stmt|;
block|}
DECL|method|display (String rule, HttpServletResponse res)
specifier|public
name|void
name|display
parameter_list|(
name|String
name|rule
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
name|res
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
name|res
operator|.
name|setContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCharacterEncoding
argument_list|(
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|CacheHeaders
operator|.
name|setNotCacheable
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|Escaper
name|html
init|=
name|HtmlEscapers
operator|.
name|htmlEscaper
argument_list|()
decl_stmt|;
try|try
init|(
name|PrintWriter
name|w
init|=
name|res
operator|.
name|getWriter
argument_list|()
init|)
block|{
name|w
operator|.
name|write
argument_list|(
literal|"<html><title>BUILD FAILED</title><body>"
argument_list|)
expr_stmt|;
name|w
operator|.
name|format
argument_list|(
literal|"<h1>%s FAILED</h1>"
argument_list|,
name|html
operator|.
name|escape
argument_list|(
name|rule
argument_list|)
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"<pre>"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
name|html
operator|.
name|escape
argument_list|(
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|why
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"</pre>"
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
literal|"</body></html>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** returns the command to build given target */
DECL|method|buildCommand (Label l)
name|String
name|buildCommand
parameter_list|(
name|Label
name|l
parameter_list|)
function_decl|;
comment|/** builds the given label. */
DECL|method|build (Label l)
name|void
name|build
parameter_list|(
name|Label
name|l
parameter_list|)
throws|throws
name|IOException
throws|,
name|BuildFailureException
function_decl|;
comment|/** returns the root relative path to the artifact for the given label */
DECL|method|targetPath (Label l)
name|Path
name|targetPath
parameter_list|(
name|Label
name|l
parameter_list|)
function_decl|;
comment|/** Label for the agent specific GWT zip. */
DECL|method|gwtZipLabel (String agent)
name|Label
name|gwtZipLabel
parameter_list|(
name|String
name|agent
parameter_list|)
function_decl|;
comment|/** Label for the polygerrit component zip. */
DECL|method|polygerritComponents ()
name|Label
name|polygerritComponents
parameter_list|()
function_decl|;
comment|/** Label for the fonts zip file. */
DECL|method|fontZipLabel ()
name|Label
name|fontZipLabel
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

