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
DECL|package|com.google.gerrit.lucene
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|lucene
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|Analyzer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|AnalyzerWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|charfilter
operator|.
name|MappingCharFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|charfilter
operator|.
name|NormalizeCharMap
import|;
end_import

begin_comment
comment|/**  * This analyzer can be used to provide custom char mappings.  *  *<p>Example usage:  *  *<pre class="prettyprint">{@code  * Map<String,String> customMapping = new HashMap<>();  * customMapping.put("_", " ");  * customMapping.put(".", " ");  *  * CustomMappingAnalyzer analyzer =  *   new CustomMappingAnalyzer(new StandardAnalyzer(version), customMapping);  * }</pre>  */
end_comment

begin_class
DECL|class|CustomMappingAnalyzer
specifier|public
class|class
name|CustomMappingAnalyzer
extends|extends
name|AnalyzerWrapper
block|{
DECL|field|delegate
specifier|private
name|Analyzer
name|delegate
decl_stmt|;
DECL|field|customMappings
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|customMappings
decl_stmt|;
DECL|method|CustomMappingAnalyzer (Analyzer delegate, Map<String, String> customMappings)
specifier|public
name|CustomMappingAnalyzer
parameter_list|(
name|Analyzer
name|delegate
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|customMappings
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
operator|.
name|getReuseStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|customMappings
operator|=
name|customMappings
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getWrappedAnalyzer (String fieldName)
specifier|protected
name|Analyzer
name|getWrappedAnalyzer
parameter_list|(
name|String
name|fieldName
parameter_list|)
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|wrapReader (String fieldName, Reader reader)
specifier|protected
name|Reader
name|wrapReader
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|Reader
name|reader
parameter_list|)
block|{
name|NormalizeCharMap
operator|.
name|Builder
name|builder
init|=
operator|new
name|NormalizeCharMap
operator|.
name|Builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|customMappings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MappingCharFilter
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|,
name|reader
argument_list|)
return|;
block|}
block|}
end_class

end_unit

