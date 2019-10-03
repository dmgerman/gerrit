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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
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
name|ImmutableSet
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
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Index types supported by the secondary index.  *  *<p>The explicitly known index types are Lucene (the default) and Elasticsearch.  *  *<p>The third supported index type is any other type String value, deemed as custom. This is for  * configuring index types that are internal or not to be disclosed. Supporting custom index types  * allows to not break that case upon core implementation changes.  */
end_comment

begin_class
DECL|class|IndexType
specifier|public
class|class
name|IndexType
block|{
DECL|field|LUCENE
specifier|private
specifier|static
specifier|final
name|String
name|LUCENE
init|=
literal|"lucene"
decl_stmt|;
DECL|field|ELASTICSEARCH
specifier|private
specifier|static
specifier|final
name|String
name|ELASTICSEARCH
init|=
literal|"elasticsearch"
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|String
name|type
decl_stmt|;
DECL|method|IndexType (@ullable String type)
specifier|public
name|IndexType
parameter_list|(
annotation|@
name|Nullable
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
operator|==
literal|null
condition|?
name|getDefault
argument_list|()
else|:
name|type
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
DECL|method|getDefault ()
specifier|public
specifier|static
name|String
name|getDefault
parameter_list|()
block|{
return|return
name|LUCENE
return|;
block|}
DECL|method|getKnownTypes ()
specifier|public
specifier|static
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|getKnownTypes
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|LUCENE
argument_list|,
name|ELASTICSEARCH
argument_list|)
return|;
block|}
DECL|method|isLucene ()
specifier|public
name|boolean
name|isLucene
parameter_list|()
block|{
return|return
name|type
operator|.
name|equals
argument_list|(
name|LUCENE
argument_list|)
return|;
block|}
DECL|method|isElasticsearch ()
specifier|public
name|boolean
name|isElasticsearch
parameter_list|()
block|{
return|return
name|type
operator|.
name|equals
argument_list|(
name|ELASTICSEARCH
argument_list|)
return|;
block|}
DECL|method|isElasticsearch (String type)
specifier|public
specifier|static
name|boolean
name|isElasticsearch
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|toLowerCase
argument_list|()
operator|.
name|equals
argument_list|(
name|ELASTICSEARCH
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

