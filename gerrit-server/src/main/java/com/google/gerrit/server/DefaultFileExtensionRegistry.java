begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeType
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|MimeUtil
import|;
end_import

begin_import
import|import
name|eu
operator|.
name|medsea
operator|.
name|mimeutil
operator|.
name|detector
operator|.
name|MimeDetector
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_class
DECL|class|DefaultFileExtensionRegistry
specifier|public
class|class
name|DefaultFileExtensionRegistry
extends|extends
name|MimeDetector
block|{
DECL|field|INI
specifier|private
specifier|static
specifier|final
name|MimeType
name|INI
init|=
name|newMimeType
argument_list|(
literal|"text/x-ini"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
DECL|field|PYTHON
specifier|private
specifier|static
specifier|final
name|MimeType
name|PYTHON
init|=
name|newMimeType
argument_list|(
literal|"text/x-python"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
DECL|field|TYPES
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|MimeType
argument_list|>
name|TYPES
init|=
name|ImmutableMap
operator|.
expr|<
name|String
decl_stmt|,
name|MimeType
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
literal|".gitmodules"
argument_list|,
name|INI
argument_list|)
decl|.
name|put
argument_list|(
literal|"project.config"
argument_list|,
name|INI
argument_list|)
decl|.
name|put
argument_list|(
literal|"BUCK"
argument_list|,
name|PYTHON
argument_list|)
decl|.
name|put
argument_list|(
literal|"defs"
argument_list|,
name|newMimeType
argument_list|(
name|PYTHON
operator|.
name|toString
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"py"
argument_list|,
name|newMimeType
argument_list|(
name|PYTHON
operator|.
name|toString
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"go"
argument_list|,
name|newMimeType
argument_list|(
literal|"text/x-go"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"cxx"
argument_list|,
name|newMimeType
argument_list|(
literal|"text/x-c++src"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"hxx"
argument_list|,
name|newMimeType
argument_list|(
literal|"text/x-c++hdr"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"scala"
argument_list|,
name|newMimeType
argument_list|(
literal|"text/x-scala"
argument_list|,
literal|1
argument_list|)
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
DECL|method|newMimeType (String type, final int specificity)
specifier|private
specifier|static
name|MimeType
name|newMimeType
parameter_list|(
name|String
name|type
parameter_list|,
specifier|final
name|int
name|specificity
parameter_list|)
block|{
return|return
operator|new
name|MimeType
argument_list|(
name|type
argument_list|)
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|getSpecificity
parameter_list|()
block|{
return|return
name|specificity
return|;
block|}
block|}
return|;
block|}
static|static
block|{
for|for
control|(
name|MimeType
name|type
range|:
name|TYPES
operator|.
name|values
argument_list|()
control|)
block|{
name|MimeUtil
operator|.
name|addKnownMimeType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMimeTypesFileName (String name)
specifier|protected
name|Collection
argument_list|<
name|MimeType
argument_list|>
name|getMimeTypesFileName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|s
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|>=
literal|0
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|s
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|MimeType
name|type
init|=
name|TYPES
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|type
argument_list|)
return|;
block|}
name|int
name|d
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|d
condition|)
block|{
name|type
operator|=
name|TYPES
operator|.
name|get
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|d
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMimeTypesFile (File file)
specifier|protected
name|Collection
argument_list|<
name|MimeType
argument_list|>
name|getMimeTypesFile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
name|getMimeTypesFileName
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getMimeTypesURL (URL url)
specifier|protected
name|Collection
argument_list|<
name|MimeType
argument_list|>
name|getMimeTypesURL
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|getMimeTypesFileName
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getMimeTypesInputStream (InputStream arg0)
specifier|protected
name|Collection
argument_list|<
name|MimeType
argument_list|>
name|getMimeTypesInputStream
parameter_list|(
name|InputStream
name|arg0
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMimeTypesByteArray (byte[] arg0)
specifier|protected
name|Collection
argument_list|<
name|MimeType
argument_list|>
name|getMimeTypesByteArray
parameter_list|(
name|byte
index|[]
name|arg0
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

