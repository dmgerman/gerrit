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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|ElasticVersion
operator|.
name|V6_7
import|;
end_import

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
DECL|field|V6_TYPE
specifier|static
specifier|final
name|String
name|V6_TYPE
init|=
literal|"_doc"
decl_stmt|;
DECL|field|INCLUDE_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|INCLUDE_TYPE
init|=
literal|"include_type_name=true"
decl_stmt|;
DECL|field|INDICES
specifier|private
specifier|static
specifier|final
name|String
name|INDICES
init|=
literal|"?allow_no_indices=false"
decl_stmt|;
DECL|field|useV5Type
specifier|private
specifier|final
name|boolean
name|useV5Type
decl_stmt|;
DECL|field|useV6Type
specifier|private
specifier|final
name|boolean
name|useV6Type
decl_stmt|;
DECL|field|omitType
specifier|private
specifier|final
name|boolean
name|omitType
decl_stmt|;
DECL|field|defaultNumberOfShards
specifier|private
specifier|final
name|int
name|defaultNumberOfShards
decl_stmt|;
DECL|field|searchFilteringName
specifier|private
specifier|final
name|String
name|searchFilteringName
decl_stmt|;
DECL|field|indicesExistParams
specifier|private
specifier|final
name|String
name|indicesExistParams
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
DECL|field|rawFieldsKey
specifier|private
specifier|final
name|String
name|rawFieldsKey
decl_stmt|;
DECL|field|versionDiscoveryUrl
specifier|private
specifier|final
name|String
name|versionDiscoveryUrl
decl_stmt|;
DECL|field|includeTypeNameParam
specifier|private
specifier|final
name|String
name|includeTypeNameParam
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
name|useV5Type
operator|=
operator|!
name|version
operator|.
name|isV6OrLater
argument_list|()
expr_stmt|;
name|this
operator|.
name|useV6Type
operator|=
name|version
operator|.
name|isV6
argument_list|()
expr_stmt|;
name|this
operator|.
name|omitType
operator|=
name|version
operator|.
name|isV7OrLater
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultNumberOfShards
operator|=
name|version
operator|.
name|isV7OrLater
argument_list|()
condition|?
literal|1
else|:
literal|5
expr_stmt|;
name|this
operator|.
name|versionDiscoveryUrl
operator|=
name|version
operator|.
name|isV6OrLater
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
name|indicesExistParams
operator|=
name|version
operator|.
name|isAtLeastMinorVersion
argument_list|(
name|V6_7
argument_list|)
condition|?
name|INDICES
operator|+
literal|"&"
operator|+
name|INCLUDE_TYPE
else|:
name|INDICES
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
name|this
operator|.
name|rawFieldsKey
operator|=
literal|"_source"
expr_stmt|;
name|this
operator|.
name|includeTypeNameParam
operator|=
name|version
operator|.
name|isAtLeastMinorVersion
argument_list|(
name|V6_7
argument_list|)
condition|?
literal|"?"
operator|+
name|INCLUDE_TYPE
else|:
literal|""
expr_stmt|;
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
name|useV5Type
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
DECL|method|indicesExistParams ()
name|String
name|indicesExistParams
parameter_list|()
block|{
return|return
name|indicesExistParams
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
DECL|method|rawFieldsKey ()
name|String
name|rawFieldsKey
parameter_list|()
block|{
return|return
name|rawFieldsKey
return|;
block|}
DECL|method|deleteToReplace ()
name|boolean
name|deleteToReplace
parameter_list|()
block|{
return|return
name|useV5Type
return|;
block|}
DECL|method|useV5Type ()
name|boolean
name|useV5Type
parameter_list|()
block|{
return|return
name|useV5Type
return|;
block|}
DECL|method|useV6Type ()
name|boolean
name|useV6Type
parameter_list|()
block|{
return|return
name|useV6Type
return|;
block|}
DECL|method|omitType ()
name|boolean
name|omitType
parameter_list|()
block|{
return|return
name|omitType
return|;
block|}
DECL|method|getDefaultNumberOfShards ()
name|int
name|getDefaultNumberOfShards
parameter_list|()
block|{
return|return
name|defaultNumberOfShards
return|;
block|}
DECL|method|getType ()
name|String
name|getType
parameter_list|()
block|{
return|return
name|getType
argument_list|(
literal|""
argument_list|)
return|;
block|}
DECL|method|getType (String type)
name|String
name|getType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|useV6Type
argument_list|()
condition|)
block|{
return|return
name|V6_TYPE
return|;
block|}
return|return
name|useV5Type
argument_list|()
condition|?
name|type
else|:
literal|""
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
DECL|method|includeTypeNameParam ()
name|String
name|includeTypeNameParam
parameter_list|()
block|{
return|return
name|includeTypeNameParam
return|;
block|}
block|}
end_class

end_unit

