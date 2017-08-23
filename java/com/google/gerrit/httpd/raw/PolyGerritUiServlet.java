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
name|common
operator|.
name|TimeUtil
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
name|FileSystems
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
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|FileTime
import|;
end_import

begin_class
DECL|class|PolyGerritUiServlet
class|class
name|PolyGerritUiServlet
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
DECL|field|NOW
specifier|private
specifier|static
specifier|final
name|FileTime
name|NOW
init|=
name|FileTime
operator|.
name|fromMillis
argument_list|(
name|TimeUtil
operator|.
name|nowMs
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|ui
specifier|private
specifier|final
name|Path
name|ui
decl_stmt|;
DECL|method|PolyGerritUiServlet (Cache<Path, Resource> cache, Path ui)
name|PolyGerritUiServlet
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
name|ui
parameter_list|)
block|{
name|super
argument_list|(
name|cache
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
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
block|{
return|return
name|ui
operator|.
name|resolve
argument_list|(
name|pathInfo
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getLastModifiedTime (Path p)
specifier|protected
name|FileTime
name|getLastModifiedTime
parameter_list|(
name|Path
name|p
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ui
operator|.
name|getFileSystem
argument_list|()
operator|.
name|equals
argument_list|(
name|FileSystems
operator|.
name|getDefault
argument_list|()
argument_list|)
condition|)
block|{
comment|// Assets are being served from disk, so we can trust the mtime.
return|return
name|super
operator|.
name|getLastModifiedTime
argument_list|(
name|p
argument_list|)
return|;
block|}
comment|// Assume this FileSystem is serving from a WAR. All WAR outputs from the build process have
comment|// mtimes of 1980/1/1, so we can't trust it, and return the initialization time of this class
comment|// instead.
return|return
name|NOW
return|;
block|}
block|}
end_class

end_unit

