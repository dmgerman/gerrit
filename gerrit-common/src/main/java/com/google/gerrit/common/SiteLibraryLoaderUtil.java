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
name|FileFilter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_class
DECL|class|SiteLibraryLoaderUtil
specifier|public
specifier|final
class|class
name|SiteLibraryLoaderUtil
block|{
DECL|method|loadSiteLib (File libdir)
specifier|public
specifier|static
name|void
name|loadSiteLib
parameter_list|(
name|File
name|libdir
parameter_list|)
block|{
name|File
index|[]
name|jars
init|=
name|libdir
operator|.
name|listFiles
argument_list|(
operator|new
name|FileFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|path
parameter_list|)
block|{
name|String
name|name
init|=
name|path
operator|.
name|getName
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
name|path
operator|.
name|isFile
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|jars
operator|!=
literal|null
operator|&&
literal|0
operator|<
name|jars
operator|.
name|length
condition|)
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|jars
argument_list|,
operator|new
name|Comparator
argument_list|<
name|File
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|File
name|a
parameter_list|,
name|File
name|b
parameter_list|)
block|{
comment|// Sort by reverse last-modified time so newer JARs are first.
name|int
name|cmp
init|=
name|Long
operator|.
name|compare
argument_list|(
name|b
operator|.
name|lastModified
argument_list|()
argument_list|,
name|a
operator|.
name|lastModified
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|!=
literal|0
condition|)
block|{
return|return
name|cmp
return|;
block|}
return|return
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|IoUtil
operator|.
name|loadJARs
argument_list|(
name|jars
argument_list|)
expr_stmt|;
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

