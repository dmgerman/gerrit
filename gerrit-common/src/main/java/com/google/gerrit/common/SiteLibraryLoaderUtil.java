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
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|collect
operator|.
name|ComparisonChain
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
name|collect
operator|.
name|Ordering
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|DirectoryStream
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
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|SiteLibraryLoaderUtil
specifier|public
specifier|final
class|class
name|SiteLibraryLoaderUtil
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SiteLibraryLoaderUtil
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|loadSiteLib (Path libdir)
specifier|public
specifier|static
name|void
name|loadSiteLib
parameter_list|(
name|Path
name|libdir
parameter_list|)
block|{
try|try
block|{
name|IoUtil
operator|.
name|loadJARs
argument_list|(
name|listJars
argument_list|(
name|libdir
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error scanning lib directory "
operator|+
name|libdir
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|lastModified (Path p)
specifier|private
specifier|static
name|long
name|lastModified
parameter_list|(
name|Path
name|p
parameter_list|)
block|{
try|try
block|{
return|return
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|p
argument_list|)
operator|.
name|toMillis
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|method|listJars (Path dir)
specifier|public
specifier|static
name|List
argument_list|<
name|Path
argument_list|>
name|listJars
parameter_list|(
name|Path
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
name|DirectoryStream
operator|.
name|Filter
argument_list|<
name|Path
argument_list|>
name|filter
init|=
operator|new
name|DirectoryStream
operator|.
name|Filter
argument_list|<
name|Path
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|Path
name|entry
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".zip"
argument_list|)
operator|)
operator|&&
name|Files
operator|.
name|isRegularFile
argument_list|(
name|entry
argument_list|)
return|;
block|}
block|}
decl_stmt|;
try|try
init|(
name|DirectoryStream
argument_list|<
name|Path
argument_list|>
name|jars
init|=
name|Files
operator|.
name|newDirectoryStream
argument_list|(
name|dir
argument_list|,
name|filter
argument_list|)
init|)
block|{
return|return
operator|new
name|Ordering
argument_list|<
name|Path
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Path
name|a
parameter_list|,
name|Path
name|b
parameter_list|)
block|{
comment|// Sort by reverse last-modified time so newer JARs are first.
return|return
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
name|lastModified
argument_list|(
name|b
argument_list|)
argument_list|,
name|lastModified
argument_list|(
name|a
argument_list|)
argument_list|)
operator|.
name|compare
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
operator|.
name|result
argument_list|()
return|;
block|}
block|}
operator|.
name|sortedCopy
argument_list|(
name|jars
argument_list|)
return|;
block|}
block|}
DECL|method|SiteLibraryLoaderUtil ()
specifier|private
name|SiteLibraryLoaderUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

