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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|collect
operator|.
name|ImmutableSortedMap
operator|.
name|toImmutableSortedMap
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|naturalOrder
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
name|primitives
operator|.
name|Ints
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
name|server
operator|.
name|UsedAt
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_class
DECL|class|NoteDbSchemaVersions
specifier|public
class|class
name|NoteDbSchemaVersions
block|{
DECL|field|ALL
specifier|static
specifier|final
name|ImmutableSortedMap
argument_list|<
name|Integer
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|NoteDbSchemaVersion
argument_list|>
argument_list|>
name|ALL
init|=
comment|// List all supported NoteDb schema versions here.
name|Stream
operator|.
name|of
argument_list|(
name|Schema_180
operator|.
name|class
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableSortedMap
argument_list|(
name|naturalOrder
argument_list|()
argument_list|,
name|v
lambda|->
name|guessVersion
argument_list|(
name|v
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
name|v
lambda|->
name|v
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|FIRST
specifier|public
specifier|static
specifier|final
name|int
name|FIRST
init|=
name|ALL
operator|.
name|firstKey
argument_list|()
decl_stmt|;
DECL|field|LATEST
specifier|public
specifier|static
specifier|final
name|int
name|LATEST
init|=
name|ALL
operator|.
name|lastKey
argument_list|()
decl_stmt|;
comment|// TODO(dborowitz): Migrate delete-project plugin to use this implementation.
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|PLUGIN_DELETE_PROJECT
argument_list|)
DECL|method|guessVersion (Class<?> c)
specifier|public
specifier|static
name|Optional
argument_list|<
name|Integer
argument_list|>
name|guessVersion
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
name|String
name|prefix
init|=
literal|"Schema_"
decl_stmt|;
if|if
condition|(
operator|!
name|c
operator|.
name|getSimpleName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|Ints
operator|.
name|tryParse
argument_list|(
name|c
operator|.
name|getSimpleName
argument_list|()
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|get ( ImmutableSortedMap<Integer, Class<? extends NoteDbSchemaVersion>> schemaVersions, int i)
specifier|public
specifier|static
name|NoteDbSchemaVersion
name|get
parameter_list|(
name|ImmutableSortedMap
argument_list|<
name|Integer
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|NoteDbSchemaVersion
argument_list|>
argument_list|>
name|schemaVersions
parameter_list|,
name|int
name|i
parameter_list|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|NoteDbSchemaVersion
argument_list|>
name|clazz
init|=
name|schemaVersions
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|clazz
operator|!=
literal|null
argument_list|,
literal|"Schema version not found: %s"
argument_list|,
name|i
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|clazz
operator|.
name|getDeclaredConstructor
argument_list|()
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
decl||
name|IllegalAccessException
decl||
name|NoSuchMethodException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"failed to invoke constructor on "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

