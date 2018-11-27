begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.elasticsearch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonObject
import|;
end_import

begin_class
DECL|class|ElasticQueryAdapter
specifier|public
class|class
name|ElasticQueryAdapter
block|{
DECL|field|POST_V5_TYPE
specifier|static
specifier|final
name|String
name|POST_V5_TYPE
init|=
literal|"_doc"
decl_stmt|;
DECL|field|ignoreUnmapped
specifier|private
specifier|final
name|boolean
name|ignoreUnmapped
decl_stmt|;
DECL|field|usePostV5Type
specifier|private
specifier|final
name|boolean
name|usePostV5Type
decl_stmt|;
DECL|field|searchFilteringName
specifier|private
specifier|final
name|String
name|searchFilteringName
decl_stmt|;
DECL|field|indicesExistParam
specifier|private
specifier|final
name|String
name|indicesExistParam
decl_stmt|;
DECL|field|exactFieldType
specifier|private
specifier|final
name|String
name|exactFieldType
decl_stmt|;
DECL|field|stringFieldType
specifier|private
specifier|final
name|String
name|stringFieldType
decl_stmt|;
DECL|field|indexProperty
specifier|private
specifier|final
name|String
name|indexProperty
decl_stmt|;
DECL|field|versionDiscoveryUrl
specifier|private
specifier|final
name|String
name|versionDiscoveryUrl
decl_stmt|;
DECL|method|ElasticQueryAdapter (ElasticVersion version)
name|ElasticQueryAdapter
parameter_list|(
name|ElasticVersion
name|version
parameter_list|)
block|{
name|this
operator|.
name|ignoreUnmapped
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|usePostV5Type
operator|=
name|version
operator|.
name|isV6
argument_list|()
expr_stmt|;
name|this
operator|.
name|versionDiscoveryUrl
operator|=
name|version
operator|.
name|isV6
argument_list|()
condition|?
literal|"/%s*"
else|:
literal|"/%s*/_aliases"
expr_stmt|;
name|this
operator|.
name|searchFilteringName
operator|=
literal|"_source"
expr_stmt|;
name|this
operator|.
name|indicesExistParam
operator|=
literal|"?allow_no_indices=false"
expr_stmt|;
name|this
operator|.
name|exactFieldType
operator|=
literal|"keyword"
expr_stmt|;
name|this
operator|.
name|stringFieldType
operator|=
literal|"text"
expr_stmt|;
name|this
operator|.
name|indexProperty
operator|=
literal|"true"
expr_stmt|;
block|}
DECL|method|setIgnoreUnmapped (JsonObject properties)
name|void
name|setIgnoreUnmapped
parameter_list|(
name|JsonObject
name|properties
parameter_list|)
block|{
if|if
condition|(
name|ignoreUnmapped
condition|)
block|{
name|properties
operator|.
name|addProperty
argument_list|(
literal|"ignore_unmapped"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setType (JsonObject properties, String type)
specifier|public
name|void
name|setType
parameter_list|(
name|JsonObject
name|properties
parameter_list|,
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
operator|!
name|usePostV5Type
condition|)
block|{
name|properties
operator|.
name|addProperty
argument_list|(
literal|"_type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|searchFilteringName ()
specifier|public
name|String
name|searchFilteringName
parameter_list|()
block|{
return|return
name|searchFilteringName
return|;
block|}
DECL|method|indicesExistParam ()
name|String
name|indicesExistParam
parameter_list|()
block|{
return|return
name|indicesExistParam
return|;
block|}
DECL|method|exactFieldType ()
name|String
name|exactFieldType
parameter_list|()
block|{
return|return
name|exactFieldType
return|;
block|}
DECL|method|stringFieldType ()
name|String
name|stringFieldType
parameter_list|()
block|{
return|return
name|stringFieldType
return|;
block|}
DECL|method|indexProperty ()
name|String
name|indexProperty
parameter_list|()
block|{
return|return
name|indexProperty
return|;
block|}
DECL|method|usePostV5Type ()
name|boolean
name|usePostV5Type
parameter_list|()
block|{
return|return
name|usePostV5Type
return|;
block|}
DECL|method|getType (String preV6Type)
name|String
name|getType
parameter_list|(
name|String
name|preV6Type
parameter_list|)
block|{
return|return
name|usePostV5Type
argument_list|()
condition|?
name|POST_V5_TYPE
else|:
name|preV6Type
return|;
block|}
DECL|method|getVersionDiscoveryUrl (String name)
name|String
name|getVersionDiscoveryUrl
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|versionDiscoveryUrl
argument_list|,
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

