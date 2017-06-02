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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|ImmutableSortedMap
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
name|Iterables
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
comment|/**  * Definitions of the various schema versions over a given Gerrit data type.  *  *<p>A<em>schema</em> is a description of the fields that are indexed over the given data type.  * This class contains all the versions of a schema defined over its data type, exposed as a map of  * version number to schema definition. If you are interested in the classes responsible for  * backend-specific runtime implementations, see the implementations of {@link IndexDefinition}.  */
end_comment

begin_class
DECL|class|SchemaDefinitions
specifier|public
specifier|abstract
class|class
name|SchemaDefinitions
parameter_list|<
name|V
parameter_list|>
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|schemas
specifier|private
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
name|schemas
decl_stmt|;
DECL|method|SchemaDefinitions (String name, Class<V> valueClass)
specifier|protected
name|SchemaDefinitions
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valueClass
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|checkNotNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|schemas
operator|=
name|SchemaUtil
operator|.
name|schemasFromClass
argument_list|(
name|getClass
argument_list|()
argument_list|,
name|valueClass
argument_list|)
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
name|name
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
name|schemas
return|;
block|}
DECL|method|get (int version)
specifier|public
specifier|final
name|Schema
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
name|int
name|version
parameter_list|)
block|{
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
init|=
name|schemas
operator|.
name|get
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|schema
operator|!=
literal|null
argument_list|,
literal|"Unrecognized %s schema version: %s"
argument_list|,
name|name
argument_list|,
name|version
argument_list|)
expr_stmt|;
return|return
name|schema
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
name|schemas
operator|.
name|lastEntry
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
annotation|@
name|Nullable
DECL|method|getPrevious ()
specifier|public
specifier|final
name|Schema
argument_list|<
name|V
argument_list|>
name|getPrevious
parameter_list|()
block|{
if|if
condition|(
name|schemas
operator|.
name|size
argument_list|()
operator|<=
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Iterables
operator|.
name|get
argument_list|(
name|schemas
operator|.
name|descendingMap
argument_list|()
operator|.
name|values
argument_list|()
argument_list|,
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

