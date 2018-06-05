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
DECL|field|ignoreUnmapped
specifier|private
specifier|final
name|boolean
name|ignoreUnmapped
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
DECL|field|keywordFieldType
specifier|private
specifier|final
name|String
name|keywordFieldType
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
name|version
operator|==
name|ElasticVersion
operator|.
name|V2_4
expr_stmt|;
switch|switch
condition|(
name|version
condition|)
block|{
case|case
name|V5_6
case|:
case|case
name|V6_2
case|:
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
name|keywordFieldType
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
break|break;
case|case
name|V2_4
case|:
default|default:
name|this
operator|.
name|searchFilteringName
operator|=
literal|"fields"
expr_stmt|;
name|this
operator|.
name|indicesExistParam
operator|=
literal|""
expr_stmt|;
name|this
operator|.
name|keywordFieldType
operator|=
literal|"string"
expr_stmt|;
name|this
operator|.
name|stringFieldType
operator|=
literal|"string"
expr_stmt|;
name|this
operator|.
name|indexProperty
operator|=
literal|"not_analyzed"
expr_stmt|;
break|break;
block|}
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
DECL|method|keywordFieldType ()
name|String
name|keywordFieldType
parameter_list|()
block|{
return|return
name|keywordFieldType
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
block|}
end_class

end_unit

