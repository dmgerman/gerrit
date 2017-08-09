begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|ImmutableSortedMap
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
comment|/**  * Definition of an index over a Gerrit data type.  *  *<p>An<em>index</em> includes a set of schema definitions along with the specific implementations  * used to query the secondary index implementation in a running server. If you are just interested  * in the static definition of one or more schemas, see the implementations of {@link  * SchemaDefinitions}.  */
end_comment

begin_class
DECL|class|IndexDefinition
specifier|public
specifier|abstract
class|class
name|IndexDefinition
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|,
name|I
extends|extends
name|Index
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
parameter_list|>
block|{
DECL|interface|IndexFactory
specifier|public
interface|interface
name|IndexFactory
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|,
name|I
extends|extends
name|Index
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
parameter_list|>
block|{
DECL|method|create (Schema<V> schema)
name|I
name|create
parameter_list|(
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
parameter_list|)
function_decl|;
block|}
DECL|field|schemaDefs
specifier|private
specifier|final
name|SchemaDefinitions
argument_list|<
name|V
argument_list|>
name|schemaDefs
decl_stmt|;
DECL|field|indexCollection
specifier|private
specifier|final
name|IndexCollection
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|indexCollection
decl_stmt|;
DECL|field|indexFactory
specifier|private
specifier|final
name|IndexFactory
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|indexFactory
decl_stmt|;
DECL|field|siteIndexer
specifier|private
specifier|final
name|SiteIndexer
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|siteIndexer
decl_stmt|;
DECL|method|IndexDefinition ( SchemaDefinitions<V> schemaDefs, IndexCollection<K, V, I> indexCollection, IndexFactory<K, V, I> indexFactory, @Nullable SiteIndexer<K, V, I> siteIndexer)
specifier|protected
name|IndexDefinition
parameter_list|(
name|SchemaDefinitions
argument_list|<
name|V
argument_list|>
name|schemaDefs
parameter_list|,
name|IndexCollection
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|indexCollection
parameter_list|,
name|IndexFactory
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|indexFactory
parameter_list|,
annotation|@
name|Nullable
name|SiteIndexer
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|siteIndexer
parameter_list|)
block|{
name|this
operator|.
name|schemaDefs
operator|=
name|schemaDefs
expr_stmt|;
name|this
operator|.
name|indexCollection
operator|=
name|indexCollection
expr_stmt|;
name|this
operator|.
name|indexFactory
operator|=
name|indexFactory
expr_stmt|;
name|this
operator|.
name|siteIndexer
operator|=
name|siteIndexer
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
specifier|final
name|String
name|getName
parameter_list|()
block|{
return|return
name|schemaDefs
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getSchemas ()
specifier|public
specifier|final
name|ImmutableSortedMap
argument_list|<
name|Integer
argument_list|,
name|Schema
argument_list|<
name|V
argument_list|>
argument_list|>
name|getSchemas
parameter_list|()
block|{
return|return
name|schemaDefs
operator|.
name|getSchemas
argument_list|()
return|;
block|}
DECL|method|getLatest ()
specifier|public
specifier|final
name|Schema
argument_list|<
name|V
argument_list|>
name|getLatest
parameter_list|()
block|{
return|return
name|schemaDefs
operator|.
name|getLatest
argument_list|()
return|;
block|}
DECL|method|getIndexCollection ()
specifier|public
specifier|final
name|IndexCollection
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|getIndexCollection
parameter_list|()
block|{
return|return
name|indexCollection
return|;
block|}
DECL|method|getIndexFactory ()
specifier|public
specifier|final
name|IndexFactory
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|getIndexFactory
parameter_list|()
block|{
return|return
name|indexFactory
return|;
block|}
annotation|@
name|Nullable
DECL|method|getSiteIndexer ()
specifier|public
specifier|final
name|SiteIndexer
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|I
argument_list|>
name|getSiteIndexer
parameter_list|()
block|{
return|return
name|siteIndexer
return|;
block|}
block|}
end_class

end_unit

