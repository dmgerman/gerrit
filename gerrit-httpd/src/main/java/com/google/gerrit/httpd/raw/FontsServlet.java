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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|Cache
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

begin_class
DECL|class|FontsServlet
class|class
name|FontsServlet
extends|extends
name|ResourceServlet
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
DECL|field|zip
specifier|private
specifier|final
name|Path
name|zip
decl_stmt|;
DECL|field|fonts
specifier|private
specifier|final
name|Path
name|fonts
decl_stmt|;
DECL|method|FontsServlet (Cache<Path, Resource> cache, Path buckOut)
name|FontsServlet
parameter_list|(
name|Cache
argument_list|<
name|Path
argument_list|,
name|Resource
argument_list|>
name|cache
parameter_list|,
name|Path
name|buckOut
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|cache
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|zip
operator|=
name|getZipPath
argument_list|(
name|buckOut
argument_list|)
expr_stmt|;
if|if
condition|(
name|zip
operator|==
literal|null
operator|||
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|zip
argument_list|)
condition|)
block|{
name|fonts
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|fonts
operator|=
name|GerritLauncher
operator|.
name|newZipFileSystem
argument_list|(
name|zip
argument_list|)
operator|.
name|getPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getResourcePath (String pathInfo)
specifier|protected
name|Path
name|getResourcePath
parameter_list|(
name|String
name|pathInfo
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|fonts
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No fonts found: "
operator|+
name|zip
operator|+
literal|". Run `buck build //polygerrit-ui:fonts`?"
argument_list|)
throw|;
block|}
return|return
name|fonts
operator|.
name|resolve
argument_list|(
name|pathInfo
argument_list|)
return|;
block|}
DECL|method|getZipPath (Path buckOut)
specifier|private
specifier|static
name|Path
name|getZipPath
parameter_list|(
name|Path
name|buckOut
parameter_list|)
block|{
if|if
condition|(
name|buckOut
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|buckOut
operator|.
name|resolve
argument_list|(
literal|"gen"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"polygerrit-ui"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"fonts"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"fonts.zip"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

