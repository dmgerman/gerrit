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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_comment
comment|/** Serve a single static file, regardless of path. */
end_comment

begin_class
DECL|class|SingleFileServlet
class|class
name|SingleFileServlet
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
DECL|field|path
specifier|private
specifier|final
name|Path
name|path
decl_stmt|;
DECL|method|SingleFileServlet (Cache<Path, Resource> cache, Path path, boolean refresh)
name|SingleFileServlet
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
name|path
parameter_list|,
name|boolean
name|refresh
parameter_list|)
block|{
name|super
argument_list|(
name|cache
argument_list|,
name|refresh
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|SingleFileServlet ( Cache<Path, Resource> cache, Path path, boolean refresh, boolean cacheOnClient)
name|SingleFileServlet
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
name|path
parameter_list|,
name|boolean
name|refresh
parameter_list|,
name|boolean
name|cacheOnClient
parameter_list|)
block|{
name|super
argument_list|(
name|cache
argument_list|,
name|refresh
argument_list|,
name|cacheOnClient
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
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
name|path
return|;
block|}
block|}
end_class

end_unit

