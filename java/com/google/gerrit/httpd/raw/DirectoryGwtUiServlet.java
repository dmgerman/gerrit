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
DECL|class|DirectoryGwtUiServlet
class|class
name|DirectoryGwtUiServlet
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
DECL|method|DirectoryGwtUiServlet (Cache<Path, Resource> cache, Path unpackedWar, boolean dev)
name|DirectoryGwtUiServlet
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
name|unpackedWar
parameter_list|,
name|boolean
name|dev
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|cache
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ui
operator|=
name|unpackedWar
operator|.
name|resolve
argument_list|(
literal|"gerrit_ui"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|ui
argument_list|)
condition|)
block|{
name|Files
operator|.
name|createDirectory
argument_list|(
name|ui
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dev
condition|)
block|{
name|ui
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
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
block|{
comment|// Return initialization time of this class, since the GWT outputs from the
comment|// build process all have mtimes of 1980/1/1.
return|return
name|NOW
return|;
block|}
block|}
end_class

end_unit

